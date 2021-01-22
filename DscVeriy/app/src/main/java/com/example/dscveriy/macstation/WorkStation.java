package com.example.dscveriy;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class WorkStation extends AppCompatActivity {

    private static final String NAMESPACE = "";
    public static String URL = "";
    Button btn_search;
    Spinner spr_unitname, spr_lineno;
    ListView lv_listview;
    TextView tv_pendingcount;
    ArrayList<String> arrayList_unitname;
    ArrayAdapter<String> adapter_unitname;
    ArrayList<String> arrayList_lineno;
    ArrayAdapter<String> adapter_lineno;
    public JSONArray jsonArray = null;
    public List<WorkStationList> workStationLists;
    public WorkStationAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_station);

        spr_unitname = findViewById(R.id.spr_unitname);
        spr_lineno = findViewById(R.id.spr_lineno);
        btn_search = findViewById(R.id.btn_search);
        lv_listview = findViewById(R.id.lv_listview);
        tv_pendingcount = findViewById(R.id.tv_pendingcount);

        arrayList_unitname = new ArrayList<>();

        arrayList_lineno = new ArrayList<>();

        workStationLists = new ArrayList<>();

        (new getUnitname()).execute();

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                (new getUnitLineNoWiseWorkStation()).execute(LoginActivity.EmpNo, spr_unitname.getSelectedItem().toString(), spr_lineno.getSelectedItem().toString());
            }
        });
    }

    public class getUnitname extends AsyncTask<String, String, String> {

        SweetAlertDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new SweetAlertDialog(WorkStation.this, SweetAlertDialog.PROGRESS_TYPE);
            p.setTitleText("Loading, Please Wait...");
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String[] paras = {""};
            String[] values = {LoginActivity.EmpNo};
            String methodname = "";
            URL = "";
            return Helper.WebServiceCall(paras, values, methodname, NAMESPACE, URL);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            p.dismiss();
            try {
                jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = new JSONObject(jsonArray.get(i).toString());
                    String UnitName = jsonObject1.getString("UnitName");
                    arrayList_unitname.add(UnitName);
                }
                adapter_unitname = new ArrayAdapter<String>(WorkStation.this, android.R.layout.simple_list_item_1, arrayList_unitname);
                spr_unitname.setAdapter(adapter_unitname);

                (new getLineNO()).execute(spr_unitname.getSelectedItem().toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class getLineNO extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... param) {

            String[] paras = {"unitname"};
            String[] values = {param[0]};
            String methodname = "getLineNo";
            URL = "";
            return Helper.WebServiceCall(paras, values, methodname, NAMESPACE, URL);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = new JSONObject(jsonArray.get(i).toString());
                    String LineNumber = jsonObject1.getString("LineNumber");
                    arrayList_lineno.add(LineNumber);
                }
                adapter_lineno = new ArrayAdapter<String>(WorkStation.this, android.R.layout.simple_list_item_1, arrayList_lineno);
                spr_lineno.setAdapter(adapter_lineno);

                (new getWorkStationMachineList()).execute();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class getWorkStationMachineList extends AsyncTask<String, String, String> {

        SweetAlertDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new SweetAlertDialog(WorkStation.this, SweetAlertDialog.PROGRESS_TYPE);
            p.setTitleText("Loading, Please Wait...");
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String[] paras = {""};
            String[] values = {LoginActivity.EmpNo};
            String methodname = "";
            String URL = "";
            return Helper.WebServiceCall(paras, values, methodname, NAMESPACE, URL);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            p.dismiss();
            workStationLists.clear();
            try {
                jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonobj = new JSONObject(jsonArray.get(i).toString());
                    String Unitname = jsonobj.getString("Unitname");
                    String LineNumber = jsonobj.getString("LineNumber");
                    String BreakDownWorkstation = jsonobj.getString("BreakDownWorkstation");
                    String BreakDownTypeEnglish = jsonobj.getString("BreakDownTypeEnglish");
                    String BreakDownDate = jsonobj.getString("BreakDownDate");
                    String IssueRaisedBy = jsonobj.getString("IssueRaisedBy");
                    String sno = jsonobj.getString("sno");
                    String IsResponse = jsonobj.getString("IsResponse");
                    String IsQcPassed = jsonobj.getString("IsQcPassed");
                    String IsQcFailed = jsonobj.getString("IsQcFailed");
                    String Status = jsonobj.getString("Status");

                    String unitlineno = "- " + Unitname + " / " + LineNumber;
                    String breakdownworkstation = "- " + BreakDownWorkstation;
                    String breakdowntypeenglish = "- " + BreakDownTypeEnglish;
                    String breakdowndate = "- " + BreakDownDate;
                    String issueraisedby = "- " + IssueRaisedBy;


                    if (IsResponse.equalsIgnoreCase("false")){
                        workStationLists.add(new WorkStationList(unitlineno, breakdownworkstation, breakdowntypeenglish, breakdowndate, issueraisedby, "Work Starting", sno,"","",IsResponse));
                    } else if(IsResponse.equalsIgnoreCase("true") && Status.equalsIgnoreCase("open")){
                        workStationLists.add(new WorkStationList(unitlineno, breakdownworkstation, breakdowntypeenglish, breakdowndate, issueraisedby, "", sno,"Work Completed","",IsResponse));
                    } else if (IsResponse.equalsIgnoreCase("true") && Status.equalsIgnoreCase("close") && IsQcPassed.equalsIgnoreCase("false") && IsQcFailed.equalsIgnoreCase("false")){
                        workStationLists.add(new WorkStationList(unitlineno, breakdownworkstation, breakdowntypeenglish, breakdowndate, issueraisedby, "", sno,"","QC Verify",IsResponse));
                    }
                }

                adapter = new WorkStationAdapter(workStationLists, getApplicationContext(), WorkStation.this);
                lv_listview.setAdapter(adapter);

                (new getWorkStationpendingCount()).execute();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class getUnitLineNoWiseWorkStation extends AsyncTask<String, String, String> {

        SweetAlertDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new SweetAlertDialog(WorkStation.this, SweetAlertDialog.PROGRESS_TYPE);
            p.setTitleText("Loading, Please Wait...");
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String[] paras = {"empno", "unitname", "lineno"};
            String[] values = {params[0], params[1], params[2]};
            String methodname = "";
            String URL = "";
            return Helper.WebServiceCall(paras, values, methodname, NAMESPACE, URL);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            p.dismiss();

            if (result.equalsIgnoreCase("[]")) {

                new SweetAlertDialog(WorkStation.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Warning...")
                        .setContentText("No Work Station Found")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Intent intent = new Intent(WorkStation.this, WorkStation.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                sweetAlertDialog.dismissWithAnimation();
                                startActivity(intent);
                                finish();
                            }
                        })
                        .show();
            } else {
                workStationLists.clear();
                try {
                    jsonArray = new JSONArray(result);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonobj = new JSONObject(jsonArray.get(i).toString());
                        String Unitname = jsonobj.getString("Unitname");
                        String LineNumber = jsonobj.getString("LineNumber");
                        String BreakDownWorkstation = jsonobj.getString("BreakDownWorkstation");
                        String BreakDownTypeEnglish = jsonobj.getString("BreakDownTypeEnglish");
                        String BreakDownDate = jsonobj.getString("BreakDownDate");
                        String IssueRaisedBy = jsonobj.getString("IssueRaisedBy");
                        String sno = jsonobj.getString("sno");
                        String IsResponse = jsonobj.getString("IsResponse");
                        String IsQcPassed = jsonobj.getString("IsQcPassed");
                        String IsQcFailed = jsonobj.getString("IsQcFailed");
                        String Status = jsonobj.getString("Status");

                        String unitlineno = "- " + Unitname + " / " + LineNumber;
                        String breakdownworkstation = "- " + BreakDownWorkstation;
                        String breakdowntypeenglish = "- " + BreakDownTypeEnglish;
                        String breakdowndate = "- " + BreakDownDate;
                        String issueraisedby = "- " + IssueRaisedBy;

                        if (IsResponse.equalsIgnoreCase("false")){
                            workStationLists.add(new WorkStationList(unitlineno, breakdownworkstation, breakdowntypeenglish, breakdowndate, issueraisedby, "Work Starting", sno,"","",IsResponse));
                        } else if(IsResponse.equalsIgnoreCase("true") && Status.equalsIgnoreCase("open")){
                            workStationLists.add(new WorkStationList(unitlineno, breakdownworkstation, breakdowntypeenglish, breakdowndate, issueraisedby, "", sno,"Work Completed","",IsResponse));
                        } else if (IsResponse.equalsIgnoreCase("true") && Status.equalsIgnoreCase("close") && IsQcPassed.equalsIgnoreCase("false") && IsQcFailed.equalsIgnoreCase("false")){
                            workStationLists.add(new WorkStationList(unitlineno, breakdownworkstation, breakdowntypeenglish, breakdowndate, issueraisedby, "", sno,"","QC Verify",IsResponse));
                        }
                    }

                    adapter = new WorkStationAdapter(workStationLists, getApplicationContext(), WorkStation.this);
                    lv_listview.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class getWorkStationpendingCount extends AsyncTask<String, String, String> {

        SweetAlertDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new SweetAlertDialog(WorkStation.this, SweetAlertDialog.PROGRESS_TYPE);
            p.setTitleText("Loading, Please Wait...");
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String[] paras = {""};
            String[] values = {LoginActivity.EmpNo};
            String methodname = "";
            String URL = "";
            return Helper.WebServiceCall(paras, values, methodname, NAMESPACE, URL);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            p.dismiss();
            try {
                jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobj = new JSONObject(jsonArray.get(i).toString());
                    String total = jsonobj.getString("total");
                    tv_pendingcount.setText(" - " + total);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        (new getWorkStationMachineList()).execute();
    }
}