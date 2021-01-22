package com.example.dscveriy;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class BackGroundImageUpload extends Worker {

    private static final String NAMESPACE = "";
    public byte[] imagebyteArray1;

    public BackGroundImageUpload(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        byte[] imagebyteArray = new byte[0];

        String imagePath = getInputData().getString("imagePath");

        File imgFile = new  File(imagePath);
        if(imgFile.exists()){
            Bitmap image = BitmapFactory.decodeFile(imgFile.getPath());
            Bitmap resizeBitmap = resize(image, image.getWidth() / 3, image.getHeight() / 3,imgFile.getPath());
            image = null;
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                resizeBitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
                imagebyteArray=  stream.toByteArray();
                resizeBitmap=null;
            } catch (Exception e) {
                e.printStackTrace();
                imagebyteArray=null;
            }
        } else {
            imagebyteArray=null;
        }

        imagebyteArray1 = new byte[0];
        String imagePath1 = getInputData().getString("imagePath1");
        File imgFile1 = new  File(imagePath1);

        if(imgFile1.exists()){
            Bitmap image = BitmapFactory.decodeFile(imgFile1.getPath());
            Bitmap resizeBitmap = resize(image, image.getWidth() / 3, image.getHeight() / 3,imgFile1.getPath());
            image = null;
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                resizeBitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
                imagebyteArray1=  stream.toByteArray();
                resizeBitmap=null;
            } catch (Exception e) {
                e.printStackTrace();
                imagebyteArray1=null;
            }
        } else {
            imagebyteArray1=null;
        }

        String empno = getInputData().getString("empno");
        String empname = getInputData().getString("empname");
        String unitname = getInputData().getString("unitname");
        String orgdesc = getInputData().getString("orgdesc");
        String workingdesc = getInputData().getString("workingdesc");
        String salary = getInputData().getString("salary");
        String remarks = getInputData().getString("remarks");
        String ischange = getInputData().getString("ischange");
        String stfname = getInputData().getString("stfname");
        String audituser = getInputData().getString("audituser");

        if (ischange.equalsIgnoreCase("")){
            ischange = "false";
        }

        String URL = "";

        String responsetring = "";

        try {
            SoapObject request = new SoapObject(NAMESPACE, "methodname");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            MarshalBase64 marshal=new MarshalBase64();
            marshal.register(envelope);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            request.addProperty("empimagefile", imagebyteArray);
            request.addProperty("empworkfile", imagebyteArray1);
            request.addProperty("empno", empno);
            request.addProperty("empname", empname);
            request.addProperty("unitname", unitname);
            request.addProperty("orgdesc", orgdesc);
            request.addProperty("workingdesc", workingdesc);
            request.addProperty("salary", salary);
            request.addProperty("remarks", remarks);
            request.addProperty("isChange", ischange);
            request.addProperty("stfname", stfname);
            request.addProperty("audituser", audituser);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            try {
                androidHttpTransport.call(NAMESPACE + "methodname", envelope);
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }

            SoapPrimitive response;
            try {
                response = (SoapPrimitive) envelope.getResponse();
                responsetring = response.toString();
            } catch (SoapFault e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        showNotification("apname", "Image uploaded successfully");

        return Result.success();
    }

    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight,String photoPath) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            ExifInterface ei = null;
            try {
                ei = new ExifInterface(photoPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap rotatedBitmap = null;
            switch(orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(image, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(image, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(image, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = image;
            }
            return rotatedBitmap;
        } else {
            return image;
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private void showNotification(String title, String task){
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel("", "", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(),"")
                .setContentTitle(title)
                .setContentText(task)
                .setSmallIcon(R.mipmap.ic_launcher);
        notificationManager.notify(1, notification.build());
    }
}
