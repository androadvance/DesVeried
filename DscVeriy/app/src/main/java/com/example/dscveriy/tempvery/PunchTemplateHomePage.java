package com.example.dscveriy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PunchTemplateHomePage extends AppCompatActivity {

    Toolbar toolbar;
    CardView EmpPhotoUpload,EmpPhotoVerify,EmpTemplateVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punch_template_home_page);

        EmpPhotoUpload = findViewById(R.id.empphotoupload);
        EmpPhotoVerify = findViewById(R.id.empphotoverify);
        EmpTemplateVerify = findViewById(R.id.emptemplateverify);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Punch Verify Template");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        getSupportActionBar();

        EmpPhotoUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PunchTemplateHomePage.this, EmployeePhotoUpload.class);
                startActivity(intent);
            }
        });

        EmpPhotoVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PunchTemplateHomePage.this, EmployeePhotoVerify.class);
                startActivity(intent);
            }
        });

        EmpTemplateVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PunchTemplateHomePage.this,EmployeeTemplateVerification.class);
                startActivity(intent);

            }
        });
    }
}