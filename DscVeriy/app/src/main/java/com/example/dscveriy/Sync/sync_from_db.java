package com.example.dscveriy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class sync_from_db extends AppCompatActivity {
String unitname,lineno,imei;
private static final String NAMESPACE = "";
public JSONArray jsonArray = null;
SQLiteRfit sqlitedatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_from_db);
        unitname=getIntent().getExtras().getString("unitname");
        lineno=getIntent().getExtras().getString("lineno");

        (new getRFIDLineIssue()).execute(unitname, lineno);
    }

    public class getRFIDLineIssue extends AsyncTask<String, String, String> {

        ProgressDialog bar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bar = new ProgressDialog(sync_from_db.this);
            bar.setCancelable(false);
            bar.setIndeterminate(true);
            bar.setCanceledOnTouchOutside(false);
            bar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            bar.setIndeterminate(false);
            bar.setMax(100);
            bar.setMessage("synchronizing data from server");
            bar.show();
        }

        @Override
        protected String doInBackground(String... params) {
            publishProgress(new String[]{"10"});
            String paras[] = {"unitname", "prodline"};
            String values[] = {params[0], params[1]};
            String methodname = "";
            String URL = "";
            publishProgress(new String[]{"100"});
            return WebService.WebServiceCall(paras, values, methodname, NAMESPACE, URL);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            bar.setProgress(Integer.parseInt(values[0]));
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                bar.setProgress(50);
                jsonArray = new JSONArray(result);
                int cnt=0;
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject1 = new JSONObject(jsonArray.get(i).toString());
                    String p_unitname = jsonObject1.getString("Unitname");
                    String p_issue = jsonObject1.getString("Issueno");
                    String p_cutinward = jsonObject1.getString("CutInwno");
                    String p_issuedate = jsonObject1.getString("IssueDate");
                    String p_prodline = jsonObject1.getString("ProdLine");
                    String p_comprefno = jsonObject1.getString("Comprefno");
                    String p_mstyleno = jsonObject1.getString("Mstyleno");
                    String p_color = jsonObject1.getString("Color");
                    String p_size = jsonObject1.getString("Size");
                    String p_bundleno = jsonObject1.getString("BundleNo");
                    String p_issueqty = jsonObject1.getString("IssueQty");
                    String p_balance = jsonObject1.getString("BalanceFGQty");
                    String p_eancode = jsonObject1.getString("EANCode");
                    String p_RFcode = jsonObject1.getString("RFCode");

                    sqlitedatabase = new SQLiteRfit((sync_from_db.this));
                    sqlitedatabase.insertLineIssue(p_unitname, p_issue, p_cutinward, p_issuedate, p_prodline, p_comprefno, p_mstyleno, p_color, p_size, p_bundleno, p_issueqty, p_balance, p_eancode, p_RFcode);
                    cnt++;
                }
                bar.setProgress(80);

                if(cnt==jsonArray.length())
                    Toast.makeText(sync_from_db.this, "Data sync completed successfully", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(sync_from_db.this, "Data sync failed", Toast.LENGTH_SHORT).show();

                bar.dismiss();
                finish();
            } catch (JSONException e) {
                e.printStackTrace();

            }
            bar.cancel();
        }
    }



}
