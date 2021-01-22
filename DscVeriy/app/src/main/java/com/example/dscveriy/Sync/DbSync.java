package com.example.dscveriy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class DbSync extends AppCompatActivity {

    Button btn_getDB,btn_putDB;
    String unit,Linenumber,DeviceId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_sync);
        btn_getDB=findViewById(R.id.btn_getDB);
        btn_putDB=findViewById(R.id.btn_putDB);
        unit=getIntent().getExtras().getString("unitname");
        Linenumber=getIntent().getExtras().getString("lineno");
        DeviceId=getIntent().getExtras().getString("DeviceId");

        btn_getDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DbSync.this,sync_from_db.class);
                i.putExtra("unitname",unit);
                i.putExtra("lineno",Linenumber);
               // i.putExtra("imeino",imei);
                startActivity(i);
            }
        });

        btn_putDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DbSync.this,sync_to_db.class);
                i.putExtra("unitname",unit);
                i.putExtra("lineno",Linenumber);
                i.putExtra("DeviceId",DeviceId);
                startActivity(i);
            }
        });
    }


}
