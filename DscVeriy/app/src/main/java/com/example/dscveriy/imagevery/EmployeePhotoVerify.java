package com.example.dscveriy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EmployeePhotoVerify extends AppCompatActivity {

    private static final String NAME_SPACE = "http://tempuri.org/";
    String url = "";
    Toolbar toolbar;
    Spinner spr_unitname;
    AutoCompleteTextView aC_Empno;
    ImageButton iBSearch;
    Spinner spr_photoverify, spr_aadharverify;
    ListView lv_listview2;
    NonScrollListView lv_listview1;
    Button btn_filter,btn_refresh;

    ArrayList<String> arrayList;
    ArrayAdapter<String> spin_adapter;

    public List<PhotoVerifyList> listitems1;
    public PhotoVerifyAdapter adapter;

    public List<PhotoVerifyListHeader> listitems2;
    public PhotoVerifyAdapterHeader adapterHeader;

    public ArrayList<String> photoverify_arrayList;
    public ArrayAdapter<String> photoverify_adapters;

    public ArrayList<String> aadharverify_arrayList;
    public ArrayAdapter<String> aadharverify_adapters;

    public JSONArray jsonArray = null;
    String[] s_employeeno;
    private static ArrayAdapter<String> empno = null;
    ArrayList<String> empnolist = new ArrayList<String>();
    String empphoto = "";
    String adharphoto = "";
    LinearLayout linearLayout13;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_photo_verify);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Employee Photo Verify");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        getSupportActionBar();

        spr_unitname = findViewById(R.id.spinner);
        aC_Empno = findViewById(R.id.empno);
        iBSearch = findViewById(R.id.iBSearch);
        spr_photoverify = findViewById(R.id.photoverify);
        spr_aadharverify = findViewById(R.id.aadarverify);
        lv_listview1 = findViewById(R.id.lv_listview);
        btn_filter = findViewById(R.id.btn_filter);
        lv_listview2 = findViewById(R.id.lv_listviewheader);
        linearLayout13 = findViewById(R.id.linearLayout13);
        btn_refresh = findViewById(R.id.btn_refresh);

        photoverify_arrayList = new ArrayList<String>();
        photoverify_arrayList.add("Select Status");
        photoverify_arrayList.add("All");
        photoverify_arrayList.add("Verify");
        photoverify_arrayList.add("Not verify");
        photoverify_adapters = new ArrayAdapter<String>(EmployeePhotoVerify.this, android.R.layout.simple_list_item_1, photoverify_arrayList);
        spr_photoverify.setAdapter(photoverify_adapters);

        aadharverify_arrayList = new ArrayList<String>();
        aadharverify_arrayList.add("Select Status");
        aadharverify_arrayList.add("All");
        aadharverify_arrayList.add("Verify");
        aadharverify_arrayList.add("Not verify");
        aadharverify_adapters = new ArrayAdapter<String>(EmployeePhotoVerify.this, android.R.layout.simple_list_item_1, aadharverify_arrayList);
        spr_aadharverify.setAdapter(aadharverify_adapters);

        listitems2 = new ArrayList<PhotoVerifyListHeader>();
        listitems2.add(new PhotoVerifyListHeader("EmpNo", "Name", "Photo", "Aadhar"));
        adapterHeader = new PhotoVerifyAdapterHeader(listitems2, getApplicationContext());
        lv_listview2.setAdapter(adapterHeader);

        (new getUnitName()).execute(LoginActivity.username);

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                startActivity(getIntent());
            }
        });


        iBSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (aC_Empno.getText().toString().isEmpty()){

                    aC_Empno.setError("Enter EmployeeNo");

                } else {

                    (new getEmpVerifiedStatusList()).execute(aC_Empno.getText().toString(), spr_unitname.getSelectedItem().toString(), "", "");
                }
            }
        });


        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                empphoto = spr_photoverify.getSelectedItem().toString();
                adharphoto = spr_aadharverify.getSelectedItem().toString();

                if (empphoto.equalsIgnoreCase("Verify")) {
                    empphoto = "true";
                } else if (empphoto.equalsIgnoreCase("Not Verify")) {
                    empphoto = "false";
                }

                if (adharphoto.equalsIgnoreCase("Verify")) {
                    adharphoto = "true";
                } else if (adharphoto.equalsIgnoreCase("Not Verify")) {
                    adharphoto = "false";
                }

                if (empphoto.equalsIgnoreCase("All")) {
                    empphoto = "All";
                } else if (adharphoto.equalsIgnoreCase("All")) {
                    adharphoto = "All";
                }

                if (empphoto.equalsIgnoreCase("Select Status") && adharphoto.equalsIgnoreCase("Select Status")){
                    Toast.makeText(EmployeePhotoVerify.this, "Please select status", Toast.LENGTH_SHORT).show();
                } else {
                    (new getEmpVerifiedStatusList()).execute("", spr_unitname.getSelectedItem().toString(), empphoto, adharphoto);
                }
            }
        });

        spr_unitname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                (new getEmployeeNumber()).execute(spr_unitname.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void shuffle(){
        Collections.shuffle(arrayList, new Random(System.currentTimeMillis()));
    }

    public class getUnitName extends AsyncTask<String, String, String> {

        SweetAlertDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new SweetAlertDialog(EmployeePhotoVerify.this, SweetAlertDialog.PROGRESS_TYPE);
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
                    Helper.InfoMsg("Warning", "Something Went Wrong", EmployeePhotoVerify.this);
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

                Helper.InfoMsg("Warning", "No Unit Assigned for this user..", EmployeePhotoVerify.this);

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
                spin_adapter = new ArrayAdapter<String>(EmployeePhotoVerify.this, android.R.layout.simple_list_item_1, arrayList);
                spr_unitname.setAdapter(spin_adapter);

            }
        }
    }

    public class getEmployeeNumber extends AsyncTask<String, String, String> {

        SweetAlertDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new SweetAlertDialog(EmployeePhotoVerify.this, SweetAlertDialog.PROGRESS_TYPE);
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
                Toast.makeText(EmployeePhotoVerify.this, "Please give valid details", Toast.LENGTH_SHORT).show();
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
                    aC_Empno.setThreshold(1);
                    aC_Empno.setAdapter(empno);
                } catch (Exception er) {
                    Toast.makeText(EmployeePhotoVerify.this, "Waiting for Network", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public class getEmpVerifiedStatusList extends AsyncTask<String, String, String> {

        SweetAlertDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new SweetAlertDialog(EmployeePhotoVerify.this, SweetAlertDialog.PROGRESS_TYPE);
            p.setTitleText("Loading, Please Wait...");
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String[] paras = {"empno", "unitname", "photo", "aadhar"};
            String[] values = {params[0], params[1], params[2], params[3]};
            String methodname = "";
            String URL = "";
            return Helper.WebServiceCall(paras, values, methodname, NAME_SPACE, URL);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            p.dismiss();

            if (result.equalsIgnoreCase("No Record Found")) {

                // Helper.dialog_warning(EmployeePhotoVerify.this, "Warning", "Employee Photo Not Found...Please Upload Image", "ok");
                Helper.dialog_warning(EmployeePhotoVerify.this, "Warning", "Employee Photo and Aadhaar Already Verified/Not upload", "ok");

            } else {

                try {

                    //  listitems1.clear();
                    listitems1 = new ArrayList<>();

                    jsonArray = new JSONArray(result);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonobj = new JSONObject(jsonArray.get(i).toString());

                        String EmpCode = jsonobj.getString("Empcode");
                        String Empname = jsonobj.getString("Empname");
                        String Aadhaar = jsonobj.getString("IsAadhaarVerify");
                        String Photo = jsonobj.getString("IsPhotoVerify");
                        String Photourl = jsonobj.getString("PhotoPath");
                        String aadharurl = jsonobj.getString("AadhaarPhotoPath");
                        String AadhaarNo = jsonobj.getString("AadhaarNo");

                        String str_aadharurl = aadharurl;
                        str_aadharurl = str_aadharurl.substring(1);

                        String str_photourl = Photourl;
                        str_photourl = str_photourl.substring(1);

                        listitems1.add(new PhotoVerifyList(EmpCode, Empname, Aadhaar, Photo, str_photourl, str_aadharurl, AadhaarNo));
                    }

                    adapter = new PhotoVerifyAdapter(listitems1, getApplicationContext(), EmployeePhotoVerify.this);
                    lv_listview1.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                linearLayout13.setVisibility(View.VISIBLE);
                lv_listview1.setVisibility(View.VISIBLE);
                aC_Empno.setText("");
            }
        }
    }
}