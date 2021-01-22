package com.example.dscveriy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;


import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EmpDesignationVerify extends AppCompatActivity {

    private static final String NAME_SPACE = "";
    String url = "";
    Toolbar toolbar;
    Spinner spr_unitname;
    public JSONArray jsonArray = null;
    String[] s_employeeno;
    private static ArrayAdapter<String> empno = null;
    ArrayList<String> empnolist = new ArrayList<String>();
    ArrayList<String> arrayList;
    ArrayAdapter<String> spin_adapter;
    AutoCompleteTextView AC_Empno;
    ImageButton iBSearch;
    EditText eT_empno, eT_name, eT_dept, eT_desc, eT_emptype, eT_salary, eT_unit, eT_remarks, eT_status, eT_floor, eT_groupname;
    CheckBox cb_ischange;
    Button btn_save, btn_captureimg, btn_workingimg, btn_new;
    AutoCompleteTextView AC_designation;
    ArrayList<String> desclist = new ArrayList<String>();
    String[] s_desc;
    private static ArrayAdapter<String> designation = null;
    public static ImageView img_photo = null;
    public static ImageView workingimg_photo = null;
    public String pictureFilePath;
    public String pictureFilePath1;
    static final int REQUEST_PICTURE_CAPTURE = 1;
    byte[] imagebyteArray, imagebyteArray1;
    String checked = "";
    LinearLayout linearlayout4, linearlayout5;
    ConstraintLayout constraint;
    ScrollView scrollView;
    public static SearchAdapter adapter;
    List<SearchList> mList;
    ImageView img_empphoto;
    boolean a = false;
    boolean b = false;
    String UnitName = "", Empcode = "";
    SqliteDatabase sqliteDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_designation_verify);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Designation Verify");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        getSupportActionBar();


        AC_Empno = findViewById(R.id.AC_Empno);
        img_empphoto = findViewById(R.id.img_empphoto);
        spr_unitname = findViewById(R.id.spr_unitname);
        iBSearch = findViewById(R.id.iBSearch);
        eT_empno = findViewById(R.id.eT_empno);
        eT_name = findViewById(R.id.eT_name);
        eT_dept = findViewById(R.id.eT_dept);
        eT_desc = findViewById(R.id.eT_desc);
        eT_emptype = findViewById(R.id.eT_emptype);
        eT_salary = findViewById(R.id.eT_salary);
        eT_unit = findViewById(R.id.eT_unit);
        cb_ischange = findViewById(R.id.cb_ischange);
        AC_designation = findViewById(R.id.AC_designation);
        btn_save = findViewById(R.id.btn_save);
        img_photo = findViewById(R.id.img_photo);
        btn_captureimg = findViewById(R.id.btn_captureimg);
        eT_remarks = findViewById(R.id.eT_remarks);
        eT_status = findViewById(R.id.eT_status);
        workingimg_photo = findViewById(R.id.workingimg_photo);
        eT_floor = findViewById(R.id.eT_floor);
        eT_groupname = findViewById(R.id.eT_groupname);
        btn_workingimg = findViewById(R.id.btn_workingimg);
        btn_new = findViewById(R.id.btn_new);
        linearlayout4 = findViewById(R.id.linearlayout4);
        constraint = findViewById(R.id.constraint);
        linearlayout5 = findViewById(R.id.linearlayout5);
        scrollView = findViewById(R.id.scrollview);
        img_empphoto = findViewById(R.id.img_empphoto);

        (new getUnitName()).execute();

        sqliteDatabase = new SqliteDatabase(EmpDesignationVerify.this);


        spr_unitname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!spr_unitname.getSelectedItem().toString().equalsIgnoreCase("select unit")) {
                    UnitName = spr_unitname.getAdapter().getItem(position).toString();
                    (new getEmpNoCount()).execute(UnitName);
                    sqliteDatabase.deleteEmpNo();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);

            }
        });

        cb_ischange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    AC_designation.setVisibility(View.VISIBLE);
                    checked = "true";
                }

                if (!b) {
                    AC_designation.setVisibility(View.GONE);
                    checked = "false";
                    AC_designation.setText("");
                }
            }
        });


        iBSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AC_Empno.getText().toString().isEmpty()) {
                    Toast.makeText(EmpDesignationVerify.this, "Enter EmployeeNo", Toast.LENGTH_SHORT).show();
                } else if (spr_unitname.getSelectedItem().toString().equalsIgnoreCase("Select Unit")) {
                    Toast.makeText(EmpDesignationVerify.this, "Select Unit", Toast.LENGTH_SHORT).show();
                } else {
                    linearlayout4.setVisibility(View.VISIBLE);
                    constraint.setVisibility(View.VISIBLE);
                    linearlayout5.setVisibility(View.VISIBLE);
                    img_photo.setImageResource(R.drawable.camera);
                    workingimg_photo.setImageResource(R.drawable.camera);
                    (new getEmpDetail()).execute(AC_Empno.getText().toString(), spr_unitname.getSelectedItem().toString());
                }
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean validdesc = false;
                for (int i = 0; i < desclist.size(); i++) {
                    if (desclist.get(i).equals(AC_designation.getText().toString())) {
                        validdesc = true;
                        break;
                    }
                }

                if (cb_ischange.isChecked()) {

                    if (validdesc) {
                        if (imagebyteArray == null) {
                            Toast.makeText(EmpDesignationVerify.this, "Please Take Emp Photo", Toast.LENGTH_SHORT).show();
                        } else if (imagebyteArray1 == null) {
                            Toast.makeText(EmpDesignationVerify.this, "Please Take Emp Working Photo", Toast.LENGTH_SHORT).show();
                        } else if (AC_Empno.getText().toString().isEmpty()) {
                            AC_Empno.setError("Enter EmployeeNo");
                        } else if (spr_unitname.getSelectedItem().toString().equalsIgnoreCase("Select Unit")) {
                            Toast.makeText(EmpDesignationVerify.this, "Select Unit", Toast.LENGTH_SHORT).show();
                        } else if (AC_designation.getText().toString().equalsIgnoreCase(eT_desc.getText().toString())) {
                            new SweetAlertDialog(EmpDesignationVerify.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Warning...")
                                    .setContentText("Original Designation and Working Designation Should Not Match!")
                                    .show();
                        } else if (checked.equalsIgnoreCase("true")) {
                            if (AC_designation.getText().toString().isEmpty()) {
                                AC_designation.setError("Enter Designation");
                            } else {
                                startWork();
                                myactivity();
                            }
                        } else {
                            startWork();
                            myactivity();
                        }
                    } else {
                        new SweetAlertDialog(EmpDesignationVerify.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Warning...")
                                .setContentText("Enter a valid Designation, please select Designation shown in the list!")
                                .show();
                    }
                } else {

                    if (imagebyteArray == null) {
                        Toast.makeText(EmpDesignationVerify.this, "Please Take Emp Photo", Toast.LENGTH_SHORT).show();
                    } else if (imagebyteArray1 == null) {
                        Toast.makeText(EmpDesignationVerify.this, "Please Take Emp Working Photo", Toast.LENGTH_SHORT).show();
                    } else if (AC_Empno.getText().toString().isEmpty()) {
                        AC_Empno.setError("Enter EmployeeNo");
                    } else if (spr_unitname.getSelectedItem().toString().equalsIgnoreCase("Select Unit")) {
                        Toast.makeText(EmpDesignationVerify.this, "Select Unit", Toast.LENGTH_SHORT).show();
                    } else {
                        startWork();
                        myactivity();
                    }
                }
            }
        });

        btn_captureimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    sendTakePictureIntent();
                    b = true;
                }
            }
        });

        btn_workingimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    sendTakePictureIntent1();
                    a = true;
                }
            }
        });

        AC_Empno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mList = retrievePeople();
                adapter = new SearchAdapter(EmpDesignationVerify.this, R.layout.activity_main, R.id.tv_empno, mList);
                AC_Empno.setAdapter(adapter);
            }
        });
    }

    private List<SearchList> retrievePeople() {

        Cursor c1;

        c1 = sqliteDatabase.getEmpNo(UnitName, AC_Empno.getText().toString());

        List<SearchList> list = new ArrayList<SearchList>();

        if (c1.getCount() > 0) {
            if (c1.moveToFirst()) {
                do {
                    String EmpNo = c1.getString(c1.getColumnIndex("EmpNum"));
                    list.add(new SearchList(EmpNo));
                } while (c1.moveToNext());
            }
        }


        /*String[] paras = {"unitname", "empno"};
        String[] values = {spr_unitname.getSelectedItem().toString(), AC_Empno.getText().toString()};
        String methodname = "S_getEmpNoAutocomplete";

        String res = Helper.WebServiceCall(paras, values, methodname, NAME_SPACE, URL);

        try {
            JSONArray jsonarray = new JSONArray(res);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobj = jsonarray.getJSONObject(i);

                String empcode = jsonobj.getString("Empcode");

                list.add(new SearchList(empcode));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        return list;
    }

    public class getUnitName extends AsyncTask<String, String, String> {

        SweetAlertDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new SweetAlertDialog(EmpDesignationVerify.this, SweetAlertDialog.PROGRESS_TYPE);
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
                    Helper.InfoMsg("Warning", "Something Went Wrong", EmpDesignationVerify.this);
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

                Helper.InfoMsg("Warning", "No Unit Assigned for this user..", EmpDesignationVerify.this);

            } else {

                arrayList = new ArrayList<String>();
                arrayList.add("Select Unit");

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
                spin_adapter = new ArrayAdapter<String>(EmpDesignationVerify.this, android.R.layout.simple_list_item_1, arrayList);
                spr_unitname.setAdapter(spin_adapter);

            }
        }
    }

    public class getDesignation extends AsyncTask<String, String, String> {

        SweetAlertDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new SweetAlertDialog(EmpDesignationVerify.this, SweetAlertDialog.PROGRESS_TYPE);
            p.setTitleText("Loading, Please Wait...");
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String[] paras = {""};
            String[] values = {params[0]};
            String methodname = "getDesignationDetails";
            String URL = "";
            return Helper.WebServiceCall(paras, values, methodname, NAME_SPACE, URL);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            p.dismiss();

            if (result.equals("")) {
                Toast.makeText(EmpDesignationVerify.this, "Please give valid details", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    desclist.clear();
                    JSONArray jsonarray = new JSONArray(result);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        desclist.add(jsonobj.getString("DesignationName"));
                    }
                    s_desc = desclist.toArray(new String[desclist.size()]);
                    designation = new ArrayAdapter<String>(getApplicationContext(), R.layout.autocompletelist, R.id.autocomtext, desclist);
                    AC_designation.setThreshold(1);
                    AC_designation.setAdapter(designation);

                } catch (Exception er) {
                    Toast.makeText(EmpDesignationVerify.this, "Waiting for Network", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public class getEmpDetail extends AsyncTask<String, String, String> {

        SweetAlertDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new SweetAlertDialog(EmpDesignationVerify.this, SweetAlertDialog.PROGRESS_TYPE);
            p.setTitleText("Loading, Please Wait...");
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String[] paras = {"empno", "unitname"};
            String[] values = {params[0], params[1]};
            String methodname = "getEmployeeDetails";
            String URL = "";
            return Helper.WebServiceCall(paras, values, methodname, NAME_SPACE, URL);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            p.dismiss();

            try {
                JSONArray jsonarray = new JSONArray(result);

                for (int i = 0; i < jsonarray.length(); i++) {

                    JSONObject jsonobj = new JSONObject(jsonarray.get(i).toString());
                    String Empcode = jsonobj.getString("Empcode");
                    String Empname = jsonobj.getString("Empname");
                    String Unitname = jsonobj.getString("Unitname");
                    String dept = jsonobj.getString("dept");
                    String Desig = jsonobj.getString("Desig");
                    String Salary = jsonobj.getString("Salary");
                    String EmpType = jsonobj.getString("EmpType");
                    String Status = jsonobj.getString("Status");
                    String GroupName = jsonobj.getString("GroupName");
                    String EmpFloor = jsonobj.getString("EmpFloor");

                    if (GroupName.equalsIgnoreCase("null")) {
                        GroupName = "-";
                    }
                    if (EmpFloor.equalsIgnoreCase("null")) {
                        EmpFloor = "-";
                    }
                    if (Salary.equalsIgnoreCase("null")) {
                        Salary = "0";
                    }

                    img_empphoto.setVisibility(View.VISIBLE);

                    if (spr_unitname.getSelectedItem().toString().equalsIgnoreCase("") || spr_unitname.getSelectedItem().toString().equalsIgnoreCase("")) {
                        String url = "";

                        Picasso.get().load(url).into(img_empphoto, new Callback() {
                            @Override
                            public void onSuccess() {
                                Picasso.get().load("")
                                        .into(img_empphoto);
                            }

                            @Override
                            public void onError(Exception e) {
                                Picasso.get().load("")
                                        .into(img_empphoto);
                            }
                        });

                    } else {

                        String url = "";

                        Picasso.get().load(url).into(img_empphoto, new Callback() {
                            @Override
                            public void onSuccess() {
                                Picasso.get().load("")
                                        .into(img_empphoto);
                            }

                            @Override
                            public void onError(Exception e) {
                                Picasso.get().load("")
                                        .into(img_empphoto);
                                Picasso.get().load("")
                                        .into(img_empphoto);
                            }
                        });
                    }


                    eT_empno.setText(Empcode);
                    eT_name.setText(Empname);
                    eT_unit.setText(Unitname);
                    eT_dept.setText(dept);
                    eT_desc.setText(Desig);
                    eT_salary.setText(Salary);
                    eT_emptype.setText(EmpType);
                    eT_status.setText(Status);
                    eT_floor.setText(EmpFloor);
                    eT_groupname.setText(GroupName);

                }

                (new getDesignation()).execute(spr_unitname.getSelectedItem().toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class getEmpNoCount extends AsyncTask<String, String, String> {

        SweetAlertDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new SweetAlertDialog(EmpDesignationVerify.this, SweetAlertDialog.PROGRESS_TYPE);
            p.setTitleText("Loading, Please Wait...");
            p.setCancelable(false);
            p.show();
        }

        protected String doInBackground(String... params) {

            String ourrefno = "";
            try {
                SoapObject request = new SoapObject(NAME_SPACE, "S_getEmpNoCount");

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);
                request.addProperty("Unitname", params[0]);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
                try {
                    androidHttpTransport.call(NAME_SPACE + "S_getEmpNoCount", envelope);

                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                }

                SoapPrimitive response;

                try {
                    response = (SoapPrimitive) envelope.getResponse();
                    ourrefno = response.toString();

                } catch (Exception e) {
                    Helper.InfoMsg("Warning", "Something Went Wrong", EmpDesignationVerify.this);
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

                Helper.InfoMsg("Warning", "No Unit Assigned for this user..", EmpDesignationVerify.this);

            } else {

                try {
                    jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = new JSONObject(jsonArray.get(i).toString());
                        String Count = jsonObject1.getString("Count");

                        Cursor c1;
                        c1 = sqliteDatabase.getEmpNoCount(UnitName);

                        if (c1.getCount() > 0) {
                            String EmpCount = c1.getString(c1.getColumnIndex("Count"));
                            if (Count.equals(EmpCount)) {
                                Toast.makeText(EmpDesignationVerify.this, EmpCount, Toast.LENGTH_SHORT).show();
                            } else {
                                sqliteDatabase.deleteEmpNo();
                                (new getEmpNumber()).execute(UnitName);
                            }
                        } else {
                            sqliteDatabase.deleteEmpNo();
                            (new getEmpNumber()).execute(UnitName);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class getEmpNumber extends AsyncTask<String, String, String> {

        SweetAlertDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new SweetAlertDialog(EmpDesignationVerify.this, SweetAlertDialog.PROGRESS_TYPE);
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

            try {
                JSONArray jsonarray = new JSONArray(result);

                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobj = new JSONObject(jsonarray.get(i).toString());
                    Empcode = jsonobj.getString("Empcode");
                    sqliteDatabase.saveEmpNo(Empcode, UnitName);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void startWork() {
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(BackGroundImageUpload.class)
                .setInputData(createInputData())
                .setInitialDelay(2, TimeUnit.SECONDS).build();
        WorkManager.getInstance(this).enqueue(oneTimeWorkRequest);
    }

    private Data createInputData() {
        Data data = new Data.Builder()
                .putString("imagePath", pictureFilePath)
                .putString("imagePath1", pictureFilePath1)
                .putString("empno", eT_empno.getText().toString())
                .putString("empname", eT_name.getText().toString())
                .putString("unitname", spr_unitname.getSelectedItem().toString())
                .putString("orgdesc", eT_desc.getText().toString())
                .putString("workingdesc", AC_designation.getText().toString())
                .putString("salary", eT_salary.getText().toString())
                .putString("remarks", eT_remarks.getText().toString())
                .putString("ischange", checked)
                .putString("stfname", LoginActivity.EmpNo)
                .putString("audituser", LoginActivity.username)
                .build();
        return data;
    }

    private void synctosqlite() {
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(BackgroudEmpCodeUpload.class)
                .setInputData(InputDataToEmpCode())
                .setInitialDelay(2, TimeUnit.SECONDS).build();
        WorkManager.getInstance(this).enqueue(oneTimeWorkRequest);
    }

    private Data InputDataToEmpCode() {
        Data data = new Data.Builder()
                .putString("Empcode", Empcode)
                .putString("UnitName", UnitName)
                .build();
        return data;
    }

    public void myactivity() {

        Helper.dialog_success(EmpDesignationVerify.this, "Image Upload Successfully", "Upload Success", "Ok");

        AC_Empno.setText("");
        eT_empno.setText("");
        eT_name.setText("");
        eT_dept.setText("");
        eT_desc.setText("");
        eT_emptype.setText("");
        eT_salary.setText("");
        eT_status.setText("");
        eT_unit.setText("");
        eT_floor.setText("");
        eT_groupname.setText("");
        cb_ischange.setChecked(false);
        eT_remarks.setText("");
        linearlayout4.setVisibility(View.INVISIBLE);
        linearlayout5.setVisibility(View.INVISIBLE);
        constraint.setVisibility(View.INVISIBLE);
        scrollView.fullScroll(ScrollView.FOCUS_UP);
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


    private void sendTakePictureIntent1() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {

            File pictureFile = null;
            try {
                pictureFile = getPictureFile1();
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

    private File getPictureFile1() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile = "" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile, ".jpg", storageDir);
        pictureFilePath1 = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PICTURE_CAPTURE && resultCode == RESULT_OK) {

            if (b) {
                if (pictureFilePath != null) {
                    File imgFile = new File(pictureFilePath);
                    if (imgFile.exists()) {
                        Bitmap image = BitmapFactory.decodeFile(imgFile.getPath());
                        Bitmap resizeBitmap = resize(image, image.getWidth() / 3, image.getHeight() / 3, imgFile.getPath());
                        image = null;
                        img_photo.setVisibility(View.VISIBLE);
                        img_photo.setImageBitmap(resizeBitmap);

                        try {
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            resizeBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            imagebyteArray = stream.toByteArray();
                            resizeBitmap = null;
                            b = false;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }


            if (a) {
                if (pictureFilePath1 != null) {
                    File imgFile1 = new File(pictureFilePath1);
                    if (imgFile1.exists()) {
                        Bitmap image = BitmapFactory.decodeFile(imgFile1.getPath());
                        Bitmap resizeBitmap = resize(image, image.getWidth() / 3, image.getHeight() / 3, imgFile1.getPath());
                        image = null;
                        workingimg_photo.setVisibility(View.VISIBLE);
                        workingimg_photo.setImageBitmap(resizeBitmap);

                        try {
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            resizeBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            imagebyteArray1 = stream.toByteArray();
                            resizeBitmap = null;
                            a = false;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
}