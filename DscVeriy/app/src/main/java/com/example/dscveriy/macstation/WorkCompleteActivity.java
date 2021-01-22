package com.example.dscveriy;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class WorkCompleteActivity extends AppCompatActivity {

    private static final String NAMESPACE = "";
    Spinner spr_workstationissue;
    Button btn_workcompleted;
    EditText eT_remarks;
    ArrayList<String> arrayList_workstation;
    ArrayAdapter<String> adapter_workstation;
    public JSONArray jsonArray = null;
    public static String URL = "";
    String sno = "";
    public static String IssueTypeInTamil = "";
    public static String IssueTypeInEnglish = "";
    public int listposition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_complete);

        eT_remarks = findViewById(R.id.eT_remarks);
        btn_workcompleted = findViewById(R.id.btn_workcompleted);
        spr_workstationissue = findViewById(R.id.spr_workstationissue);

        arrayList_workstation = new ArrayList<>();

        (new getWorkStationIssue()).execute();

        Intent intent = getIntent();

        sno = intent.getStringExtra("sno");

        spr_workstationissue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                listposition = position;
                JSONObject jsonobj = null;
                try {
                    jsonobj = new JSONObject(jsonArray.get(listposition).toString());

                    IssueTypeInTamil = jsonobj.getString("IssueTypeInTamil");
                    IssueTypeInEnglish = jsonobj.getString("IssueTypeInEnglish");
                }
                catch (Exception er){
                    er.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_workcompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                (new u_WorkStationwithBarcode()).execute(eT_remarks.getText().toString(),spr_workstationissue.getSelectedItem().toString(),IssueTypeInTamil,sno);
            }
        });
    }


    public class u_WorkStationwithBarcode extends AsyncTask<String, String, String> {

        SweetAlertDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new SweetAlertDialog(WorkCompleteActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            p.setTitleText("Loading, Please Wait...");
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String[] paras = {"clearremarks", "breakDownTypeinEnglish", "breakDownTypeinTamil", "sno"};
            String[] values = {params[0], params[1], params[2], params[3]};
            String methodname = "u_WorkCompleted";
            String URL = "";
            return Helper.WebServiceCall(paras, values, methodname, NAMESPACE, URL);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            p.dismiss();

            if (result.equalsIgnoreCase("success")) {
                Intent intent1 = new Intent(WorkCompleteActivity.this, WorkStation.class);
                setResult(RESULT_OK, intent1);
                finish();
            } else {
                new SweetAlertDialog(WorkCompleteActivity.this, SweetAlertDialog.WARNING_TYPE).setContentText(result).show();
            }
        }
    }

    public class getWorkStationIssue extends AsyncTask<String, String, String> {

        SweetAlertDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new SweetAlertDialog(WorkCompleteActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            p.setTitleText("Loading, Please Wait...");
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String methodname = "getWorkStationIssue";
            URL = "";
            return Helper.WebServiceCall(null , null, methodname, NAMESPACE, URL);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            p.dismiss();
            try {
                jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = new JSONObject(jsonArray.get(i).toString());
                    String IssueTypeInEnglish = jsonObject1.getString("IssueTypeInEnglish");
                    arrayList_workstation.add(IssueTypeInEnglish);
                }
                adapter_workstation = new ArrayAdapter<String>(WorkCompleteActivity.this, android.R.layout.simple_list_item_1, arrayList_workstation);
                spr_workstationissue.setAdapter(adapter_workstation);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}