package com.example.dscveriy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;

import android.content.ClipData;
import android.content.Context;
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
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
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
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ImageUpload extends AppCompatActivity {
    private static final String NAME_SPACE = "";
    Toolbar toolbar;
    EditText remarks;
    Button btn_captureimage, btn_save;
    public static ImageView img_preview = null;
    static final int REQUEST_PICTURE_CAPTURE = 1;
    private String pictureFilePath;
    public static Context ctx;
    Bitmap bitmap;
    byte[] imagebyteArray = new byte[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Update Status");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        getSupportActionBar();

        ctx = this;

        img_preview = findViewById(R.id.imageview);
        btn_captureimage = findViewById(R.id.capture);

        remarks = findViewById(R.id.remarks);
        btn_save = findViewById(R.id.save);

        remarks.setSelected(false);


        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            btn_captureimage.setEnabled(false);
        }


        final String str_status = getIntent().getExtras().getString("status");
        final String str_empcode = getIntent().getExtras().getString("empcode");
        final String str_machineid = getIntent().getExtras().getString("machineid");
        final String str_unitname = getIntent().getExtras().getString("unitname");

        btn_captureimage.setOnClickListener(capture);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imagebyteArray == null) {
                    Toast.makeText(ImageUpload.this, "Please capture photo", Toast.LENGTH_SHORT).show();
                } else if (remarks.getText().toString().isEmpty()) {
                    Toast.makeText(ImageUpload.this, "Please Enter Remarks", Toast.LENGTH_SHORT).show();
                } else {
                    String[] result = {str_empcode, str_machineid, str_unitname,
                            str_status, LoginActivity.username, remarks.getText().toString()};
                    (new I_VerifyPunchTemplate()).execute(result);

                }
            }
        });
    }

    public class I_VerifyPunchTemplate extends AsyncTask<String, String, String> {

        SweetAlertDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new SweetAlertDialog(ImageUpload.this, SweetAlertDialog.PROGRESS_TYPE);
            p.setTitleText("Loading, Please Wait...");
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String methodname = "";
            String URL = "";

            try {
                SoapObject request = new SoapObject(NAME_SPACE, methodname);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                MarshalBase64 marshal = new MarshalBase64();
                marshal.register(envelope);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                request.addProperty("empcode", params[0]);
                request.addProperty("MachineID", params[1]);
                request.addProperty("Unitname", params[2]);
                request.addProperty("VerifyStatus", params[3]);
                request.addProperty("VerifiedUser", params[4]);
                request.addProperty("VerifyRemarks", params[5]);
                request.addProperty("verifyfile", imagebyteArray);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                try {
                    androidHttpTransport.call(NAME_SPACE + methodname, envelope);
                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();

                }

                SoapPrimitive response;
                try {
                    response = (SoapPrimitive) envelope.getResponse();
                    return response.toString();
                } catch (SoapFault e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            p.dismiss();
            try {
                JSONArray jsonArray1 = new JSONArray(result);
                if (jsonArray1.getJSONObject(0).getString("col1").equalsIgnoreCase("warning")) {
                    //Toast.makeText(ImageUpload.this, jsonArray1.getJSONObject(0).getString("col2"), Toast.LENGTH_SHORT).show();
                    Helper.dialog_warning(ImageUpload.this, "Warning", jsonArray1.getJSONObject(0).getString("col2"), "Ok");

                } else if (jsonArray1.getJSONObject(0).getString("col1").equalsIgnoreCase("success")) {
                    Toast.makeText(ImageUpload.this, "success", Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    Helper.dialog_warning(ImageUpload.this, "Warning", "Verification failed", "Ok");
                }
            } catch (JSONException e) {
                e.printStackTrace();
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

    private View.OnClickListener capture = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                sendTakePictureIntent();
            }
        }
    };

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
                    cameraIntent.setClipData(ClipData.newRawUri("", photoURI));
                    cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
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
            File imgFile = new File(pictureFilePath);
            if (imgFile.exists()) {
                img_preview.setImageURI(Uri.fromFile(imgFile));
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), Uri.fromFile(imgFile));

                    Bitmap image = BitmapFactory.decodeFile(imgFile.getPath());
                    Bitmap resizeBitmap = resize(image, image.getWidth() / 3, image.getHeight() / 3, imgFile.getPath());
                    image = null;
                    try {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        resizeBitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                        imagebyteArray = stream.toByteArray();
                        resizeBitmap = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                        imagebyteArray = null;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}