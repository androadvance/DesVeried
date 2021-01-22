package com.example.dscveriy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EmployeeTemplateVerification extends AppCompatActivity {

    private static final String NAME_SPACE = "http://tempuri.org/";
    String url = "";
    Toolbar toolbar;
    NonScrollListView lv_listview;
    public static List<EMPVerifyList> listitem;
    public static EMPVerifyAdapter adapter;
    public List<ListHeader> listitem1;
    public HeaderAdapter adapter1;
    Spinner spr_unitname;
    AutoCompleteTextView au_Empno;
    EditText eT_EmpName, eT_team, eT_Dept, eT_Designation;
    ImageButton img_search;
    ArrayList<String> arrayList;
    ArrayAdapter<String> spin_adapter;
    public JSONArray jsonArray = null;
    String[] s_employeeno;
    private static ArrayAdapter<String> empno = null;
    ArrayList<String> empnolist = new ArrayList<String>();
    TextView tv_name, tv_team, tv_dept, tv_desc;
    public static String MachineID, LastVerifiedDate, MachineIP, VerifiedUser, VerifyStatus;
    Button btn_save;
    String str_remarks, str_imagepath;
    public ListView listViewitems;
    Button refresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_template_verification);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Employee Template Verify");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        getSupportActionBar();

        refresh = findViewById(R.id.refresh);
        lv_listview = findViewById(R.id.lv_listviews);
        img_search = findViewById(R.id.iBSearch);
        spr_unitname = findViewById(R.id.spinner);
        au_Empno = findViewById(R.id.empno);
        eT_EmpName = findViewById(R.id.name);
        eT_team = findViewById(R.id.team);
        eT_Dept = findViewById(R.id.dept);
        eT_Designation = findViewById(R.id.desc);
        listViewitems = findViewById(R.id.linearLayout12);

        tv_name = findViewById(R.id.tv_name);
        tv_team = findViewById(R.id.tv_team);
        tv_dept = findViewById(R.id.tv_dept);
        tv_desc = findViewById(R.id.tv_desc);
        btn_save = findViewById(R.id.save);

        listitem1 = new ArrayList<ListHeader>();
        listitem1.add(new ListHeader("Machine Name", "Status", "Update", "LastCheck Date"));
        adapter1 = new HeaderAdapter(listitem1, getApplicationContext());
        listViewitems.setAdapter(adapter1);

        Intent intent = getIntent();
        if (intent != null) {
            str_remarks = intent.getStringExtra("remarks");
            str_imagepath = intent.getStringExtra("imagepath");
        }

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                (new getMachineWiseList()).execute(spr_unitname.getSelectedItem().toString(), au_Empno.getText().toString());
            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (au_Empno.getText().toString().isEmpty()) {
                    au_Empno.setError("Enter Employee Number");

                } else {

                    (new getEmpDetail()).execute(au_Empno.getText().toString(),spr_unitname.getSelectedItem().toString());

                }

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check atleast one machine checked..
                int checked = 0;
                for (int i = 0; i < listitem.size(); i++) {
                    EMPVerifyList item = listitem.get(i);
                    if (item.Status != null & !item.Status.isEmpty()) {
                        checked++;
                    }
                }
                if (checked > 0) {
                    for (int i = 0; i < listitem.size(); i++) {
                        EMPVerifyList item = listitem.get(i);
                        if (item.Status != null & !item.Status.isEmpty()) {

                        }
                    }
                } else {
                    Toast.makeText(EmployeeTemplateVerification.this, "Please select verification status for atleast one machine", Toast.LENGTH_SHORT).show();
                    return;
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


        (new getUnitName()).execute();
    }


    public class getUnitName extends AsyncTask<String, String, String> {

        SweetAlertDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new SweetAlertDialog(EmployeeTemplateVerification.this, SweetAlertDialog.PROGRESS_TYPE);
            p.setTitleText("Loading, Please Wait...");
            p.setCancelable(false);
            p.show();
        }

        protected String doInBackground(String... params) {

            String ourrefno = "";
            try {
                SoapObject request = new SoapObject(NAME_SPACE, "");

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);
                request.addProperty("username", LoginActivity.username);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
                try {
                    androidHttpTransport.call(NAME_SPACE + "", envelope);

                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                }

                SoapPrimitive response;

                try {
                    response = (SoapPrimitive) envelope.getResponse();
                    ourrefno = response.toString();

                } catch (Exception e) {
                    Helper.InfoMsg("Warning", "Something Went Wrong", EmployeeTemplateVerification.this);
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

                Helper.InfoMsg("Warning", "No Unit Assigned for this user..", EmployeeTemplateVerification.this);

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
                spin_adapter = new ArrayAdapter<String>(EmployeeTemplateVerification.this, android.R.layout.simple_list_item_1, arrayList);
                spr_unitname.setAdapter(spin_adapter);

                (new getEmployeeNumber()).execute(spr_unitname.getSelectedItem().toString());
            }
        }
    }

    public class getEmployeeNumber extends AsyncTask<String, String, String> {

        SweetAlertDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new SweetAlertDialog(EmployeeTemplateVerification.this, SweetAlertDialog.PROGRESS_TYPE);
            p.setTitleText("Loading, Please Wait...");
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String[] paras = {""};
            String[] values = {params[0]};
            String methodname = "";
            String URL = "";
            return Helper.WebServiceCall(paras, values, methodname, NAME_SPACE, URL);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            p.dismiss();

            if (result.equals("")) {
                Toast.makeText(EmployeeTemplateVerification.this, "Please give valid details", Toast.LENGTH_SHORT).show();
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
                    au_Empno.setThreshold(1);
                    au_Empno.setAdapter(empno);
                } catch (Exception er) {
                    Toast.makeText(EmployeeTemplateVerification.this, "Waiting for Network", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public class getMachineWiseList extends AsyncTask<String, String, String> {

        SweetAlertDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new SweetAlertDialog(EmployeeTemplateVerification.this, SweetAlertDialog.PROGRESS_TYPE);
            p.setTitleText("Loading, Please Wait...");
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String[] paras = {"", ""};
            String[] values = {params[0], params[1]};
            String methodname = "";
            String URL = "";
            return Helper.WebServiceCall(paras, values, methodname, NAME_SPACE, URL);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            p.dismiss();

            if (result.equals("[]")) {

                Toast.makeText(EmployeeTemplateVerification.this, "No Record found", Toast.LENGTH_SHORT).show();

            } else {

                listitem = new ArrayList<>();
                try {
                    jsonArray = new JSONArray(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonobj = new JSONObject(jsonArray.get(i).toString());

                        MachineID = jsonobj.getString("MachineID");
                        LastVerifiedDate = jsonobj.getString("LastVerifiedDate");
                        MachineIP = jsonobj.getString("MachineIP");
                        VerifiedUser = jsonobj.getString("VerifiedUser");
                        MachineIP = jsonobj.getString("MachineIP");
                        VerifyStatus = jsonobj.getString("VerifyStatus");

                        listitem.add(new EMPVerifyList(MachineID, VerifyStatus, "", LastVerifiedDate, au_Empno.getText().toString(), "", spr_unitname.getSelectedItem().toString(), "", MachineIP, VerifiedUser));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter = new EMPVerifyAdapter(listitem, getApplicationContext(), EmployeeTemplateVerification.this);
                lv_listview.setAdapter(adapter);

            }
        }
    }

    public class getEmpDetail extends AsyncTask<String, String, String> {

        SweetAlertDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new SweetAlertDialog(EmployeeTemplateVerification.this, SweetAlertDialog.PROGRESS_TYPE);
            p.setTitleText("Loading, Please Wait...");
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String[] paras = {"",""};
            String[] values = {params[0],params[1]};
            String methodname = "";
            String URL = "";
            return Helper.WebServiceCall(paras, values, methodname, NAME_SPACE, URL);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            if (result.equalsIgnoreCase("[]")){

                Helper.dialog_warning(EmployeeTemplateVerification.this,"Warning","Employee not assign for this unit","ok");

            } else {

                try {
                    jsonArray = new JSONArray(result);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonobj = new JSONObject(jsonArray.get(i).toString());

                        String Empname = jsonobj.getString("Empname");
                        String Dept = jsonobj.getString("Dept");
                        String Desig = jsonobj.getString("Desig");
                        String Unitname = jsonobj.getString("Unitname");

                        eT_EmpName.setText(Empname);
                        eT_Dept.setText(Dept);
                        eT_Designation.setText(Desig);
                        eT_team.setText(Unitname);

                        listViewitems.setVisibility(View.VISIBLE);
                        tv_name.setVisibility(View.VISIBLE);
                        tv_team.setVisibility(View.VISIBLE);
                        tv_dept.setVisibility(View.VISIBLE);
                        tv_desc.setVisibility(View.VISIBLE);
                        eT_EmpName.setVisibility(View.VISIBLE);
                        eT_team.setVisibility(View.VISIBLE);
                        eT_Dept.setVisibility(View.VISIBLE);
                        eT_Designation.setVisibility(View.VISIBLE);
                        btn_save.setVisibility(View.INVISIBLE);
                        refresh.setVisibility(View.VISIBLE);

                        (new getMachineWiseList()).execute(spr_unitname.getSelectedItem().toString(), au_Empno.getText().toString());

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                p.dismiss();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

        }
    }
}