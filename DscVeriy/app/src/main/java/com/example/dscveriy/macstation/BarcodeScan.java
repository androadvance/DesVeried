package com.example.dscveriy;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import org.json.JSONArray;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class BarcodeScan extends AppCompatActivity {

    private static final String NAMESPACE = "";
    EditText eT_Barcode, eT_remarks;
    ImageButton iBScan;
    Button btn_save;
    String sno = "";
    public JSONArray jsonArray = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scan);

        eT_Barcode = findViewById(R.id.eT_Barcode);
        eT_remarks = findViewById(R.id.eT_remarks);
        iBScan = findViewById(R.id.iBScan);
        btn_save = findViewById(R.id.btn_save);

        Intent intent = getIntent();

        sno = intent.getStringExtra("sno");

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if (eT_Barcode.getText().toString().isEmpty()){
                    Toast.makeText(BarcodeScan.this, "Barcode should not empty", Toast.LENGTH_SHORT).show();
                } else {*/
                    (new u_WorkStationwithBarcode()).execute("", eT_remarks.getText().toString(), LoginActivity.EmpNo, sno);
               // }
            }
        });

        iBScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_FORMATS", "CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF,CODABAR,EAN_13,EAN_8,UPC_A,QR_CODE");
                startActivityForResult(intent, 101);
            }
        });
    }

    public class u_WorkStationwithBarcode extends AsyncTask<String, String, String> {

        SweetAlertDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new SweetAlertDialog(BarcodeScan.this, SweetAlertDialog.PROGRESS_TYPE);
            p.setTitleText("Loading, Please Wait...");
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String[] paras = {"barcode", "responseremarks", "responsibleMechanic", "sno"};
            String[] values = {params[0], params[1], params[2], params[3]};
            String methodname = "";
            String URL = "";
            return Helper.WebServiceCall(paras, values, methodname, NAMESPACE, URL);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            p.dismiss();

            if (result.equalsIgnoreCase("success")) {
                Intent intent1 = new Intent(BarcodeScan.this, WorkStation.class);
                Toast.makeText(BarcodeScan.this, "barcode verified", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK, intent1);
                finish();
            } else {
                new SweetAlertDialog(BarcodeScan.this, SweetAlertDialog.WARNING_TYPE).setContentText(result).show();
            }
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (intent != null) {
            if (requestCode == 101) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                eT_Barcode.setText(contents);
            }
        }
    }
}