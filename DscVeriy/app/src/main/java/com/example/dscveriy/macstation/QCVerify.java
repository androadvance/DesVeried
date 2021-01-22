package com.example.dscveriy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParserException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class QCVerify extends AppCompatActivity {

    CheckBox cb_pass,cb_fail;
    EditText eT_date,eT_remarks;
    Button btn_Submit;
    DatePickerDialog datePickerDialog;
    public static ImageView img_preview = null;
    Button btn_captureimage;
    private String pictureFilePath;
    static final int REQUEST_PICTURE_CAPTURE = 1;
    byte[] imagebyteArray;
    private static final String NAMESPACE = "";
    byte[] qc_image = new byte[0];
    Bitmap bitmap;
    String sno = "",IssueTypeInEnglish = "", IssueTypeInTamil = "";
    String qcpass = "",qcfail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_c_verify);

        cb_pass = findViewById(R.id.cb_pass);
        cb_fail = findViewById(R.id.cb_fail);
        eT_date = findViewById(R.id.eT_date);
        eT_remarks = findViewById(R.id.eT_remarks);
        btn_Submit = findViewById(R.id.btn_Submit);
        img_preview = findViewById(R.id.imageview);
        btn_captureimage = findViewById(R.id.btn_captureimage);

        Intent intent = getIntent();

        sno = intent.getStringExtra("sno");
        IssueTypeInEnglish = intent.getStringExtra("IssueTypeInEnglish");
        IssueTypeInTamil = intent.getStringExtra("IssueTypeInTamil");

        // current date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());
        eT_date.setText(currentDateandTime);

        cb_pass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    cb_fail.setChecked(false);
                }
            }
        });

        cb_fail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    cb_pass.setChecked(false);
                }
            }
        });



        btn_captureimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    sendTakePictureIntent();
                }
            }
        });


        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cb_pass.isChecked()){
                    qcpass = "Ok";
                    qcpass = "true";
                    qcfail = "false";
                }
                if (cb_fail.isChecked()){
                    qcfail = "Not Ok";
                    qcfail = "false";
                    qcpass = "false";
                }

                if (imagebyteArray == null) {
                    Toast.makeText(getApplicationContext(), "Please take a Photo", Toast.LENGTH_LONG).show();
                } else if (!cb_pass.isChecked() && !cb_fail.isChecked()){
                    Toast.makeText(getApplicationContext(), "Please select QC result", Toast.LENGTH_LONG).show();
                }  else {
                    startWork();
                    myactivity();
                }
            }
        });
    }

    private void startWork() {
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(WorkManager13.class)
                .setInputData(createInputData())
                .setInitialDelay(2, TimeUnit.SECONDS).build();
        WorkManager.getInstance(this).enqueue(oneTimeWorkRequest);
    }

    private Data createInputData() {
        Data data = new Data.Builder()
                .putString("imagePath", pictureFilePath)
                .putString("ok", qcpass)
                .putString("notok", qcfail)
                .putString("date", eT_date.getText().toString())
                .putString("remarks", eT_remarks.getText().toString())
                .putString("sno", sno)
                .build();
        return data;
    }

    public void myactivity() {

        Intent intent = new Intent(QCVerify.this, WorkStation.class);
        startActivity(intent);
    }



    private void sendTakePictureIntent() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {

            File pictureFile = null;
            try {
                pictureFile = getPictureFile();
            } catch (IOException ex) {
                Toast.makeText(this,
                        "Photo file can't be created, please try again",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (pictureFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "",
                        pictureFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                    // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    cameraIntent.setClipData(ClipData.newRawUri("", photoURI));
                    cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                cameraIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
                cameraIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
                cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);
            }
        }
    }

    private File getPictureFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile = "" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile, ".jpg", storageDir);
        pictureFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PICTURE_CAPTURE && resultCode == RESULT_OK) {
            if (pictureFilePath != null) {
                File imgFile = new File(pictureFilePath);
                if (imgFile.exists()) {
                    Bitmap image = BitmapFactory.decodeFile(imgFile.getPath());
                    Bitmap resizeBitmap = resize(image, image.getWidth() / 3, image.getHeight() / 3, imgFile.getPath());
                    image = null;
                    img_preview.setImageBitmap(resizeBitmap);

                    try {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        resizeBitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                        imagebyteArray = stream.toByteArray();
                        resizeBitmap = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight, String photoPath) {
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
            switch (orientation) {

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


    public class u_WorkStationwithBarcode extends AsyncTask<String, String, String> {

        SweetAlertDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new SweetAlertDialog(QCVerify.this, SweetAlertDialog.PROGRESS_TYPE);
            p.setTitleText("Loading, Please Wait...");
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String methodname = "";
            String URL = "";

            String responsetring = "";
            try {

                Bitmap bm = bitmap;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                qc_image = baos.toByteArray();

            } catch (Exception er) {

            }
            try {
                SoapObject request = new SoapObject(NAMESPACE, methodname);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                MarshalBase64 marshal = new MarshalBase64();
                marshal.register(envelope);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                request.addProperty("image", qc_image);
                request.addProperty("isQcPassed", params[0]);
                request.addProperty("isQcFailed", params[1]);
                request.addProperty("QcfailedRemarks", params[2]);
                request.addProperty("QcCheckUser", params[3]);
                request.addProperty("datetime", params[4]);
                request.addProperty("sno", params[5]);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                try {
                    androidHttpTransport.call(NAMESPACE + methodname, envelope);
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
            return responsetring;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            p.dismiss();

            if (result.equalsIgnoreCase("success")) {
                Intent intent1 = new Intent(QCVerify.this, WorkStation.class);
                startActivity(intent1);
            } else {
                new SweetAlertDialog(QCVerify.this, SweetAlertDialog.WARNING_TYPE).setContentText(result).show();
            }
        }
    }
}

