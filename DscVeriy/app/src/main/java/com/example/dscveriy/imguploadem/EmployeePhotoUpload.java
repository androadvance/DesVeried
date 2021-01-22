package com.example.dscveriy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EmployeePhotoUpload extends AppCompatActivity {

    private static final String NAME_SPACE = "";
    String url = "";
    Toolbar toolbar;
    Spinner spr_unitname;
    AutoCompleteTextView AC_Empno;
    EditText eT_adharno, eT_Name, eT_Dept, eT_doj;
    ImageView img_clear, img_aadhar, img_emp;
    ImageButton img_search;
    Button btn_save;
    TextView tv_adhar, tv_emp;
    ArrayList<String> arrayList;
    ArrayAdapter<String> spin_adapter;
    public JSONArray jsonArray = null;
    String[] s_employeeno;
    private static ArrayAdapter<String> empno = null;
    ArrayList<String> empnolist = new ArrayList<String>();
    public static Context ctx;
    static final int REQUEST_PICTURE_CAPTURE = 1;
    private String pictureFilePath, pictureFilepath1;
    Bitmap bitmap, bitmap1;
    String fullScreenInd, fullScreenInd1;
    String PhotoPath = "", AadhaarPhotoPath = "";
    Button btn_new;
    LinearLayout linearlayout4,linear2;
    TextView tv_name,tv_dept,tv_doj;
    String str_PhotoPath;
    String str_AadhaarPhotoPath;

    byte[] adh_data = new byte[0];
    byte[] emp_data = new byte[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_photo_upload);

        eT_adharno = findViewById(R.id.adharno);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Employee Photo Upload");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        getSupportActionBar();

        ctx = this;

        tv_adhar = findViewById(R.id.tv_adhar);
        tv_emp = findViewById(R.id.tv_emp);
        spr_unitname = findViewById(R.id.spinner);
        AC_Empno = findViewById(R.id.empno);
        eT_Name = findViewById(R.id.name);
        eT_Dept = findViewById(R.id.dept);
        eT_doj = findViewById(R.id.dateofjoin);
        img_clear = findViewById(R.id.clear);
        img_aadhar = findViewById(R.id.img_adharphoto);
        img_emp = findViewById(R.id.img_empphoto);
        img_search = findViewById(R.id.iBSearch);
        btn_save = findViewById(R.id.btn_save);
        btn_new = findViewById(R.id.btn_new);
        linearlayout4 = findViewById(R.id.linearlayout4);
        linear2 = findViewById(R.id.linear2);
        tv_name = findViewById(R.id.tv_name);
        tv_dept = findViewById(R.id.tv_dept);
        tv_doj = findViewById(R.id.tv_doj);


        SpannableString content = new SpannableString("Preview Image");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tv_adhar.setText(content);

        SpannableString content1 = new SpannableString("Preview Image");
        content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
        tv_emp.setText(content1);


        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            img_aadhar.setEnabled(false);
            img_emp.setEnabled(false);
        }

        img_aadhar.setOnClickListener(capture);
        img_emp.setOnClickListener(capture1);


        (new getUnitName()).execute(LoginActivity.username);

        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });


        fullScreenInd = getIntent().getStringExtra("fullScreenIndicator");

        if ("y".equals(fullScreenInd)) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            // getSupportActionBar().hide();

            img_aadhar.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            img_aadhar.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            img_aadhar.setAdjustViewBounds(false);
            img_aadhar.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        fullScreenInd1 = getIntent().getStringExtra("fullScreenIndicator");

        if ("y".equals(fullScreenInd1)) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            // getSupportActionBar().hide();

            img_emp.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            img_emp.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            img_emp.setAdjustViewBounds(false);
            img_emp.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        spr_unitname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                (new getEmployeeNumber()).execute(spr_unitname.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        tv_adhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri myUri = Uri.parse("");
                Intent intent1 = new Intent(Intent.ACTION_SEND);
                intent1.setAction(Intent.ACTION_VIEW);
                intent1.setDataAndType(myUri , "image/png");
                startActivity(intent1);
            }
        });


        tv_emp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri myUri = Uri.parse("");
                Intent intent1 = new Intent(Intent.ACTION_SEND);
                intent1.setAction(Intent.ACTION_VIEW);
                intent1.setDataAndType(myUri, "image/png");
                startActivity(intent1);

            }
        });

        img_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                eT_adharno.getText().clear();

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (eT_adharno.getText().toString().length()<12){
                    eT_adharno.setError("AadhaarNo must contain 12 digit");
                } else {
                    String[] result = {eT_adharno.getText().toString(), AC_Empno.getText().toString(), "", ""};
                    (new updateAadharPhotoDetail()).execute(result);
                }
            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AC_Empno.getText().toString().isEmpty()) {
                    AC_Empno.setError("Enter EmployeeNo");
                } else {

                    (new getEmpDetail()).execute(AC_Empno.getText().toString(), spr_unitname.getSelectedItem().toString());

                }
            }
        });
    }



    private View.OnClickListener capture = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                sendTakePictureIntent();
            }
        }
    };

    private View.OnClickListener capture1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                sendTakePictureIntent1();
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

    private void sendTakePictureIntent1() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File pictureFile = null;
            try {
                pictureFile = getPictureFilepath();
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

    private File getPictureFilepath() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile = "" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile, ".jpg", storageDir);
        pictureFilepath1 = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICTURE_CAPTURE && resultCode == RESULT_OK) {

            if (pictureFilePath == null) {

                Toast.makeText(ctx, "Take Aadhar Photo", Toast.LENGTH_SHORT).show();

            } else {

                File imgFile = new File(pictureFilePath);
                if (imgFile.exists()) {
                    img_aadhar.setImageURI(Uri.fromFile(imgFile));
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), Uri.fromFile(imgFile));

                        Bitmap image = BitmapFactory.decodeFile(imgFile.getPath());
                        Bitmap resizeBitmap = resize(image, image.getWidth() / 3, image.getHeight() / 3, imgFile.getPath());
                        image = null;
                        try {
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            resizeBitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                            adh_data = stream.toByteArray();
                            resizeBitmap = null;
                        } catch (Exception e) {
                            e.printStackTrace();
                            adh_data = null;
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                /*if (imgFile.exists()) {
                    img_aadhar.setImageURI(Uri.fromFile(imgFile));
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), Uri.fromFile(imgFile));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }*/
            }

            if (pictureFilepath1 == null) {

                Toast.makeText(ctx, "Take Employee Photo", Toast.LENGTH_SHORT).show();

            } else {

                File imgFile1 = new File(pictureFilepath1);
                if (imgFile1.exists()) {
                    img_aadhar.setImageURI(Uri.fromFile(imgFile1));
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), Uri.fromFile(imgFile1));

                        Bitmap image = BitmapFactory.decodeFile(imgFile1.getPath());
                        Bitmap resizeBitmap = resize(image, image.getWidth() / 3, image.getHeight() / 3, imgFile1.getPath());
                        image = null;
                        try {
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            resizeBitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                            emp_data = stream.toByteArray();
                            resizeBitmap = null;
                        } catch (Exception e) {
                            e.printStackTrace();
                            emp_data = null;
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }



                /*if (imgFile1.exists()) {
                    img_emp.setImageURI(Uri.fromFile(imgFile1));
                    try {
                        bitmap1 = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), Uri.fromFile(imgFile1));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }*/
            }
        }
    }


    public class getUnitName extends AsyncTask<String, String, String> {

        SweetAlertDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new SweetAlertDialog(EmployeePhotoUpload.this, SweetAlertDialog.PROGRESS_TYPE);
            p.setTitleText("Loading, Please Wait...");
            p.setCancelable(false);
            p.show();
        }

        protected String doInBackground(String... params) {

            String ourrefno = "";
            try {
                SoapObject request = new SoapObject(NAME_SPACE, "S_getUserUnit");

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);
                request.addProperty("username", LoginActivity.username);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
                try {
                    androidHttpTransport.call(NAME_SPACE + "S_getUserUnit", envelope);

                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                }

                SoapPrimitive response;

                try {
                    response = (SoapPrimitive) envelope.getResponse();
                    ourrefno = response.toString();

                } catch (Exception e) {
                    Helper.InfoMsg("Warning", "Something Went Wrong", EmployeePhotoUpload.this);
                }
            } catch (Exception er) {
                er.printStackTrace();
            }
            return ourrefno;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            p.dismiss();

            if (result.equals("[]")) {

                Helper.InfoMsg("Warning", "No Unit Assigned for this user..", EmployeePhotoUpload.this);

            } else {

                arrayList = new ArrayList<String>();

                try {
                    jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = new JSONObject(jsonArray.get(i).toString());
                        String co = jsonObject1.getString("UnitName");
                        arrayList.add(co);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spin_adapter = new ArrayAdapter<String>(EmployeePhotoUpload.this, android.R.layout.simple_list_item_1, arrayList);
                spr_unitname.setAdapter(spin_adapter);

            }
        }
    }

    public class getEmployeeNumber extends AsyncTask<String, String, String> {

        SweetAlertDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new SweetAlertDialog(EmployeePhotoUpload.this, SweetAlertDialog.PROGRESS_TYPE);
            p.setTitleText("Loading, Please Wait...");
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String[] paras = {"unitname"};
            String[] values = {params[0]};
            String methodname = "S_getEmpNo";
            String URL = "";
            return Helper.WebServiceCall(paras, values, methodname, NAME_SPACE, URL);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            p.dismiss();

            if (result.equals("")) {
                Toast.makeText(EmployeePhotoUpload.this, "Please give valid details", Toast.LENGTH_SHORT).show();
            } else {

                try {
                    empnolist.clear();
                    JSONArray jsonarray = new JSONArray(result);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        empnolist.add(jsonobj.getString("Empcode"));

                    }
                    s_employeeno = empnolist.toArray(new String[empnolist.size()]);
                    empno = new ArrayAdapter<String>(getApplicationContext(), R.layout.autocompletelist, R.id.autocomtext, empnolist);
                    AC_Empno.setThreshold(1);
                    AC_Empno.setAdapter(empno);
                } catch (Exception er) {
                    Toast.makeText(EmployeePhotoUpload.this, "Waiting for Network", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public class getEmpDetail extends AsyncTask<String, String, String> {

        SweetAlertDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new SweetAlertDialog(EmployeePhotoUpload.this, SweetAlertDialog.PROGRESS_TYPE);
            p.setTitleText("Loading, Please Wait...");
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String[] paras = {"empcode", "unitname"};
            String[] values = {params[0], params[1]};
            String methodname = "S_getEmpDetails";
            String URL = "";
            return Helper.WebServiceCall(paras, values, methodname, NAME_SPACE, URL);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            p.dismiss();
            if (result.equalsIgnoreCase("[]")) {

                Helper.dialog_warning(EmployeePhotoUpload.this, "Warning", "Employee not assign for this unit", "ok");

            } else {

                try {
                    jsonArray = new JSONArray(result);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonobj = new JSONObject(jsonArray.get(i).toString());

                        String Empname = jsonobj.getString("Empname");
                        String Dept = jsonobj.getString("Dept");
                        String DateofJoin = jsonobj.getString("DateofJoin");
                        String Aadhar = jsonobj.getString("AadhaarNo");
                        PhotoPath = jsonobj.getString("PhotoPath");
                        AadhaarPhotoPath = jsonobj.getString("AadhaarPhotoPath");

                        eT_Name.setText(Empname);
                        eT_Dept.setText(Dept);
                        eT_doj.setText(DateofJoin);

                        str_PhotoPath = PhotoPath;
                        str_PhotoPath = str_PhotoPath.substring(1);

                        str_AadhaarPhotoPath = AadhaarPhotoPath;
                        str_AadhaarPhotoPath = str_AadhaarPhotoPath.substring(1);

                        if (Aadhar.equalsIgnoreCase("null")) {
                            eT_adharno.setText("");
                        } else {
                            eT_adharno.setText(Aadhar);
                            eT_adharno.setEnabled(false);
                            img_clear.setEnabled(false);
                        }

                        if (!PhotoPath.equalsIgnoreCase("null")) {
                            Picasso.get()
                                    .load("")
                                    .resize(250, 250)
                                    .into(img_emp);
                            img_emp.setEnabled(false);
                        }

                        if (!AadhaarPhotoPath.equalsIgnoreCase("null")) {
                            Picasso.get()
                                    .load("")
                                    .resize(250, 250)
                                    .into(img_aadhar);
                            img_aadhar.setEnabled(false);
                        } else {
                            img_aadhar.setEnabled(true);
                        }
                    }

                    tv_name.setVisibility(View.VISIBLE);
                    tv_dept.setVisibility(View.VISIBLE);
                    tv_doj.setVisibility(View.VISIBLE);
                    eT_Name.setVisibility(View.VISIBLE);
                    eT_Dept.setVisibility(View.VISIBLE);
                    eT_doj.setVisibility(View.VISIBLE);
                    linear2.setVisibility(View.VISIBLE);
                    linearlayout4.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class updateAadharPhotoDetail extends AsyncTask<String, String, String> {

        SweetAlertDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new SweetAlertDialog(EmployeePhotoUpload.this, SweetAlertDialog.PROGRESS_TYPE);
            p.setTitleText("Loading, Please Wait...");
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String methodname = "U_empPhotoUpload";
            String URL = "";

            String responsetring = "";
            try {
                Bitmap bm = bitmap;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, baos);
                adh_data = baos.toByteArray();

                Bitmap bm1 = bitmap1;
                ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
                bm1.compress(Bitmap.CompressFormat.JPEG, 90, baos1);
                emp_data = baos1.toByteArray();
            } catch (Exception er) {

            }
            try {
                SoapObject request = new SoapObject(NAME_SPACE, methodname);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                MarshalBase64 marshal = new MarshalBase64();
                marshal.register(envelope);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                request.addProperty("Aadharno", params[0]);
                request.addProperty("empcode", params[1]);
                request.addProperty("photopath", params[2]);
                request.addProperty("aadharpath", params[3]);
                request.addProperty("verifyempfile", emp_data);
                request.addProperty("verifyAadharfile", adh_data);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                try {
                    androidHttpTransport.call(NAME_SPACE + methodname, envelope);
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

            if (result.equalsIgnoreCase("Success")){
                Helper.dialog_success(EmployeePhotoUpload.this, "Image Upload Successfully", "Upload Success", "Ok");
                Intent intent = new Intent(EmployeePhotoUpload.this,EmployeePhotoUpload.class);
                startActivity(intent);
                finish();
            } else {
                Helper.dialog_warning(EmployeePhotoUpload.this, "Warning", result, "Ok");
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
}