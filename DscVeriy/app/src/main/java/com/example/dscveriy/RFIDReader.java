package com.example.dscveriy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.raylinks.Function;
import com.raylinks.ModuleControl;

import org.json.JSONArray;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class RFIDReader extends AppCompatActivity {

    ModuleControl moduleControl = new ModuleControl();
    Function fun = new Function();
    private static boolean loopFlag;
    private byte flagCrc;
    Button btn_fg, btn_reject, btn_rework, btn_start, btn_record;
    EditText et_rfcode;
    EditText eT_unitname;
    EditText eT_lineno;
    EditText eT_Eanno;
    EditText eT_StyleNo;
    EditText eT_color;
    EditText eT_username;
    EditText eT_size;
    EditText eT_Bundle;
    EditText eT_refno;
    TextView t_rft;
    TextView t_rftpercent;
    TextView t_reworkoptoday;
    TextView t_reworkoptodaypercent;
    TextView t_reworkopold;
    TextView t_reworkopoldpercent;
    TextView t_rework;
    TextView t_reworkpercent;
    TextView t_rejectionoptoday;
    TextView t_rejectionoptodaypercent;
    TextView t_rejectionopold;
    TextView t_rejectionopoldpercent;
    TextView txt_rejection;
    TextView t_rejectionpercent;
    TextView t_total;
    TextView t_totalpercent;
    SQLiteRfit sqLiteRfit;
    EditText date;
    Context context;
    String defecttype = "";
    String defectsubtype = "";
    String reworktype = "";
    String scantype = "OK";
    String rejection = "";

    private long backPressedTime;


    String unit1, line1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rfidreader);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        date = findViewById(R.id.dateid);
        eT_unitname = findViewById(R.id.unitnameid);
        eT_lineno = findViewById(R.id.linenoid);
        eT_Eanno = findViewById(R.id.eanid);
        eT_StyleNo = findViewById(R.id.stylenoid);
        eT_color = findViewById(R.id.colorid);
        eT_username = findViewById(R.id.userid);
        eT_size = findViewById(R.id.sizeid);
        eT_Bundle = findViewById(R.id.bundlenoid);
        eT_refno = findViewById(R.id.refnoid);
        t_rft = findViewById(R.id.rftid);
        t_rftpercent = findViewById(R.id.rftpercentid);
        t_reworkoptoday = findViewById(R.id.reworktodayid);
        t_reworkoptodaypercent = findViewById(R.id.reworktodaypercentid);
        t_reworkopold = findViewById(R.id.reworkoldid);
        t_reworkopoldpercent = findViewById(R.id.reworkoldpercentid);
        t_rework = findViewById(R.id.reworkid);
        t_reworkpercent = findViewById(R.id.reworkpercentid);
        t_rejectionoptoday = findViewById(R.id.rejectiontodayid);
        t_rejectionoptodaypercent = findViewById(R.id.rejectiontodaypercentid);
        t_rejectionopold = findViewById(R.id.rejectionoldid);
        t_rejectionopoldpercent = findViewById(R.id.rejectionoldpercentid);
        txt_rejection = findViewById(R.id.rejectionid1);
        t_rejectionpercent = findViewById(R.id.rejectionpercentid1);
        t_total = findViewById(R.id.totalid);
        t_totalpercent = findViewById(R.id.totalpercentid);

        btn_fg = findViewById(R.id.btn_fg);
        btn_reject = findViewById(R.id.btn_reject);
        btn_rework = findViewById(R.id.btn_rework);
        btn_start = findViewById(R.id.btn_start);
        et_rfcode = findViewById(R.id.et_RFCode);
        btn_record = findViewById(R.id.btn_record);

        btn_fg.setOnClickListener(btn_fgOnClickListener);
        btn_reject.setOnClickListener(btn_rejectOnClickListener);
        btn_rework.setOnClickListener(btn_reworkOnClickListener);
        btn_start.setOnClickListener(btn_startOnClickListener);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String currentDateandTime = sdf.format(new Date());
        date.setText(currentDateandTime);

        btn_fg.setBackgroundColor(getResources().getColor(R.color.colorRed));

        btn_fg.setBackgroundColor(Color.RED);
        btn_rework.setBackgroundColor(getResources().getColor(R.color.colorSelect));
        btn_reject.setBackgroundColor(getResources().getColor(R.color.colorSelect));

        sqLiteRfit = new SQLiteRfit(RFIDReader.this);
        Cursor c;
        c = sqLiteRfit.fetchData();
        c.moveToFirst();

        if (c != null && c.getCount() > 0) {

            String name = c.getString(c.getColumnIndex("IMEINO"));
            Cursor c1;
            c1 = sqLiteRfit.fetchunit(name);
            c1.moveToFirst();
            String unit = c1.getString(c1.getColumnIndex("Unit"));
            String line = c1.getString(c1.getColumnIndex("LineNo"));
            String username = c1.getString(c1.getColumnIndex("Username"));

            eT_unitname.setText(unit);
            eT_lineno.setText(line);
            eT_username.setText(username);
            readRfidTag();

        } else {

            Helper.dialog_error(RFIDReader.this, "Warning", "Please sync to load data get from server", "OK");

        }

        scantype = "OK";

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiManager.isWifiEnabled()) {
            if (line1 != null & unit1 != null) {

                line1 = getIntent().getExtras().getString("line");
                unit1 = getIntent().getExtras().getString("unit");

                eT_lineno.setText(line1);
                eT_unitname.setText(unit1);

            }
        }

        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RFIDReader.this, LiveRecord.class);
                intent.putExtra("linenos", eT_lineno.getText().toString());
                intent.putExtra("units", eT_unitname.getText().toString());
                intent.putExtra("date", date.getText().toString());
                startActivity(intent);
            }
        });
    }



    public void readRfidTag() {
        showcount();
        if (btn_start.getText().toString().equalsIgnoreCase("start")) {
            if (moduleControl.UhfStartInventory((byte) 0, (byte) 0, flagCrc)) {
                btn_start.setText("Stop");
                loopFlag = true;
                new TagThread().start();

            }
        } else {
            if (moduleControl.UhfStopOperation(flagCrc)) {
                btn_start.setText("Start");
                loopFlag = false;
            } else {

                Toast.makeText(RFIDReader.this, "Stop inventory fail", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private View.OnClickListener btn_fgOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


        }
    };

    private View.OnClickListener btn_rejectOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (btn_start.getText().toString().equalsIgnoreCase("stop")) {
                loopFlag = false;
                while (!moduleControl.UhfStopOperation(flagCrc)) {
                }
                btn_start.setText("Start");
            }
            Intent intent1 = new Intent(RFIDReader.this, RejectionActivity.class);
            intent1.putExtra("ScanType", "REJECTION");
            startActivity(intent1);
            finish();
        }
    };

    private View.OnClickListener btn_reworkOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (btn_start.getText().toString().equalsIgnoreCase("stop")) {
                loopFlag = false;
                while (!moduleControl.UhfStopOperation(flagCrc)) {
                }
            }
            Intent intent2 = new Intent(RFIDReader.this, ReworkActivity.class);
            intent2.putExtra("ScanType", "REWORK");
            startActivity(intent2);
            finish();
        }
    };

    private View.OnClickListener btn_startOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (btn_start.getText().toString().equalsIgnoreCase("start")) {
                if (moduleControl.UhfStartInventory((byte) 0, (byte) 0, flagCrc)) {
                    btn_start.setText("Stop");
                    loopFlag = true;
                    new TagThread().start();

                }
            } else {
                loopFlag = false;
                while (!moduleControl.UhfStopOperation(flagCrc)) {
                }
                btn_start.setText("Start");
            }
        }
    };

    public String lastrfcode = "";
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            try {
                Bundle bundle = msg.getData();
                String tagUii = bundle.getString("tagUii").toUpperCase();

                tagUii = tagUii.substring(4);

                et_rfcode.setText(tagUii);
                //loopFlag = true;

                boolean loopproceed = false;
                if (lastrfcode == "") {
                    lastrfcode = et_rfcode.getText().toString();
                    loopproceed = true;
                } else if (lastrfcode.equalsIgnoreCase(et_rfcode.getText().toString())) {
                    loopproceed = false;
                } else {
                    loopproceed = true;
                    lastrfcode = et_rfcode.getText().toString();
                }
                if (loopproceed) {

                    Cursor c5;
                    c5 = sqLiteRfit.fetchEANno(et_rfcode.getText().toString());
                    c5.moveToFirst();
                    if (c5.getCount() == 0) {
                        Helper.dialog_error(RFIDReader.this, "Warning", "RFcode not assign", "ok");
                        Helper.musicwarning(RFIDReader.this);
                    } else {
                        String eanno = c5.getString(c5.getColumnIndex("eancode"));
                        eT_Eanno.setText(eanno);
                    }


                    Cursor c;
                    c = sqLiteRfit.fetchEanDetail(et_rfcode.getText().toString(), eT_lineno.getText().toString(), eT_unitname.getText().toString(), eT_Eanno.getText().toString());

                    if (c.getCount() > 0) {
                        String bundleno = c.getString(c.getColumnIndex("Bundleno"));
                        Cursor cursor = sqLiteRfit.checkRFCode(et_rfcode.getText().toString(), "OK", bundleno);
                        //  Cursor cursor2 = sqLiteRfit.checkRFCode(et_rfcode.getText().toString(), "REJECTION", bundleno);
                        if (cursor.getCount() <= 0) {
                            String style = c.getString(c.getColumnIndex("Mstyleno"));
                            String color = c.getString(c.getColumnIndex("Color"));
                            String size = c.getString(c.getColumnIndex("Size"));
                            String refno = c.getString(c.getColumnIndex("Comprefno"));

                            eT_StyleNo.setText(style);
                            eT_color.setText(color);
                            eT_size.setText(size);
                            eT_Bundle.setText(bundleno);
                            eT_refno.setText(refno);
                            sqLiteRfit.insertRFITScan(et_rfcode.getText().toString().toUpperCase(), eT_Eanno.getText().toString(), eT_Bundle.getText().toString(), style, eT_refno.getText().toString(), eT_color.getText().toString(), eT_size.getText().toString(),
                                    eT_lineno.getText().toString(), scantype, defecttype, defectsubtype, reworktype, date.getText().toString(), eT_username.getText().toString(), date.getText().toString(), 0, eT_unitname.getText().toString());
                            Helper.play(RFIDReader.this);
                            showcount();

                        } else {
                            cursor.moveToFirst();
                            String readtype = cursor.getString(cursor.getColumnIndex("ReadType"));
                            String bundlen = cursor.getString(cursor.getColumnIndex("Bundleno"));
                            Helper.dialog_error(RFIDReader.this, "Warning", "RFcode already scanned in ReadType : " + readtype + " and bundle no : " + bundlen, "OK");
                            Helper.musicwarning(RFIDReader.this);
                        }

                    } else {
                        Toast.makeText(context, "RFCode not assign", Toast.LENGTH_SHORT).show();
                        Helper.musicwarning(RFIDReader.this);
                    }
                }

            } catch (Exception ie) {
                ie.printStackTrace();
            }
        }

    };

    public void showcount() {

        Cursor c1;
        c1 = sqLiteRfit.fetchAllDetailCount();
        if (c1 != null) {
            if (c1.moveToFirst()) {
                String RFT = c1.getString(c1.getColumnIndex("OK"));
                String ReworkToday = c1.getString(c1.getColumnIndex("RewOk"));
                String ReworkOld = c1.getString(c1.getColumnIndex("RewoldOk"));
                String Rework = c1.getString(c1.getColumnIndex("Rew"));
                String RejectionToday = c1.getString(c1.getColumnIndex("Rejok"));
                String RejectionOld = c1.getString(c1.getColumnIndex("Rejoldok"));
                String Rejection = c1.getString(c1.getColumnIndex("Rej"));
                String Total = c1.getString(c1.getColumnIndex("TotQty"));

                String RFTpercent = c1.getString(c1.getColumnIndex("OKPercentage"));
                String Reworkpercent = c1.getString(c1.getColumnIndex("ReworkPercentage"));
                String ReworkTodaypercent = c1.getString(c1.getColumnIndex("ReworkOPPercentage"));
                String ReworkOldpercent = c1.getString(c1.getColumnIndex("ReworkOldOPPercentage"));
                String Rejectionpercent = c1.getString(c1.getColumnIndex("RejnPercentage"));
                String RejectionTodaypercent = c1.getString(c1.getColumnIndex("RejnOPPercentage"));
                String RejectionOldpercent = c1.getString(c1.getColumnIndex("RejnOldOPPercentage"));
                String Totalpercent = c1.getString(c1.getColumnIndex("TotalPercentage"));


                t_rft.setText(RFT);
                t_reworkoptoday.setText(ReworkToday);
                t_reworkopold.setText(ReworkOld);
                t_rework.setText(Rework);
                t_rejectionoptoday.setText(RejectionToday);
                t_rejectionopold.setText(RejectionOld);
                txt_rejection.setText(Rejection);
                t_total.setText(Total);

                t_rftpercent.setText(RFTpercent);
                t_reworkpercent.setText(Reworkpercent);
                t_reworkoptodaypercent.setText(ReworkTodaypercent);
                t_reworkopoldpercent.setText(ReworkOldpercent);
                t_rejectionpercent.setText(Rejectionpercent);
                t_rejectionoptodaypercent.setText(RejectionTodaypercent);
                t_rejectionopoldpercent.setText(RejectionOldpercent);
                t_totalpercent.setText(Totalpercent);
            }
        }
    }

    class TagThread extends Thread {

        public void run() {
            byte[] bLenUii = new byte[1];
            byte[] bUii = new byte[255];

            while (loopFlag) {
                if (moduleControl.UhfReadInventory(bLenUii, bUii)) {
                    String sUii = fun.bytesToHexString(bUii, bLenUii[0]);

                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("tagUii", sUii);
                    bundle.putByte("tagLen", bLenUii[0]);

                    msg.setData(bundle);
                    handler.sendMessage(msg);

                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (btn_start.getText().toString().equalsIgnoreCase("stop")) {
            loopFlag = false;
            while (!moduleControl.UhfStopOperation(flagCrc)) {
            }
            btn_start.setText("Start");
        }
       /* Intent intent = new Intent(RFIDReader.this,MainActivity.class);
        startActivity(intent);*/
        finish();
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();

        if (backPressedTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            return;

        } else {

            Toast.makeText(context, "Press back again  to exit", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }*/


}




