package com.example.dscveriy;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONArray;

public class sync_to_db extends AppCompatActivity {
    String unitname,lineno,DeviceId;
    private static final String NAMESPACE = "";
    public JSONArray jsonArray = null;
    SQLiteRfit sqlitedatabase;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_to_db);

            unitname=getIntent().getExtras().getString("unitname");
            lineno=getIntent().getExtras().getString("lineno");
        DeviceId=getIntent().getExtras().getString("DeviceId");
            sqlitedatabase = new SQLiteRfit((sync_to_db.this));
            cursor=sqlitedatabase.fetchNotSyncRF();
            new  putRFIDLineIssue().execute();
    }
    public class putRFIDLineIssue extends AsyncTask<String, String, String> {

        ProgressDialog bar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bar = new ProgressDialog(sync_to_db.this);
            bar.setCancelable(false);

            bar.setIndeterminate(true);
            bar.setCanceledOnTouchOutside(false);
            bar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            bar.setIndeterminate(false);
            bar.setMax(100);
            bar.setMessage("synchronizing data to server");
            bar.show();

        }

        @Override
        protected String doInBackground(String... params) {
            publishProgress(new String[]{"10"});
            try {
                int rowcnnt = 0;
                if (cursor != null) {
                    rowcnnt = cursor.getCount();
                    int i = 0;
                    // move cursor to first row
                    if (cursor.moveToFirst()) {
                        do {
                            // Get version from Cursor
                            String UnitName = cursor.getString(cursor.getColumnIndex("UnitName"));
                            String RFCode = cursor.getString(cursor.getColumnIndex("RFCode"));
                            String EANCode = cursor.getString(cursor.getColumnIndex("Eancode"));
                            String Bundleno = cursor.getString(cursor.getColumnIndex("Bundleno"));
                            String Comprefno = cursor.getString(cursor.getColumnIndex("Refno"));
                            String Color = cursor.getString(cursor.getColumnIndex("Color"));
                            String Size = cursor.getString(cursor.getColumnIndex("Size"));
                            String LineNum = cursor.getString(cursor.getColumnIndex("LineNumber"));
                            String ReadType = cursor.getString(cursor.getColumnIndex("ReadType"));
                            String DefectType = cursor.getString(cursor.getColumnIndex("DefectType"));
                            String DefectSubType = cursor.getString(cursor.getColumnIndex("DefectSubType"));
                            String DefectZone = cursor.getString(cursor.getColumnIndex("DefectZone"));
                         //   String ScanDatetime = cursor.getString(cursor.getColumnIndex("ScanDatetime"));
                            String AuditUser = cursor.getString(cursor.getColumnIndex("AuditUser"));
                            String AuditDate = cursor.getString(cursor.getColumnIndex("AuditDate"));
                            String Styleno = cursor.getString(cursor.getColumnIndex("Styleno"));
                            String paras[] = {"UnitName", "LineNum", "RFCode", "EANCode", "Styleno", "Comprefno", "Color", "Size", "ReadType", "AuditDate", "AuditUser", "DefectType", "DefectSubType", "DefectZone", "ScanAuditDate","DeviceId"};
                            String values[] = {UnitName, LineNum, RFCode, EANCode, Styleno, Comprefno, Color, Size, ReadType, AuditDate, AuditUser, DefectType, DefectSubType, DefectZone, AuditDate,DeviceId};
                            String methodname = "Insert_P_ProScanRFIDWise2";
                            String URL = "http://"+ LoginPage.myIP + "/RFTService/RFITLogin.asmx";
                            String s = WebService.WebServiceCall(paras, values, methodname, NAMESPACE, URL);
                            if(s.equalsIgnoreCase("true")){
                                sqlitedatabase.updateSyncRF(RFCode,Bundleno,Comprefno,ReadType,LineNum,UnitName);
                            }
                            // move to next row
                            i++;
                            int percentage = i / rowcnnt;
                            publishProgress(new String[]{String.valueOf(percentage)});
                        } while (cursor.moveToNext());
                    }
                }
                publishProgress(new String[]{("100")});
                return  "ok";
            }
            catch (Exception er){
                er.printStackTrace();
                return  "fail";
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            bar.setProgress(Integer.parseInt(values[0]));
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            bar.dismiss();
            Toast.makeText(getApplicationContext(),"Data updated to server",Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
