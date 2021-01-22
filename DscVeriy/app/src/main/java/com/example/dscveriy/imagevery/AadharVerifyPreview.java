package com.example.dscveriy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AadharVerifyPreview extends AppCompatActivity {

    private static final String NAME_SPACE = "";
    ImageView img_aadharphoto;
    TextView tv_aadharNo;
    Button btn_verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aadhar_verify_preview);

        img_aadharphoto = findViewById(R.id.aadharphotoverify);
        tv_aadharNo = findViewById(R.id.aadharno);
        btn_verify = findViewById(R.id.verify);

        final String Aadharurl = getIntent().getExtras().getString("Aadharurl");
        final String AadharNo = getIntent().getExtras().getString("AadharNo");
        final String AadharVerify = getIntent().getExtras().getString("AadharVerify");
        final String Empno = getIntent().getExtras().getString("Empno");

        if (AadharVerify != null){
            if (AadharVerify.equalsIgnoreCase("true")){
                btn_verify.setVisibility(View.INVISIBLE);
            }
        }


        Picasso.get()
                .load("")
                .resize(250, 150)
                .centerCrop()
                .into(img_aadharphoto);

        String res = "AadharNo :" + AadharNo;

        tv_aadharNo.setText(res);

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                (new updateAadharVerify()).execute(Empno);

            }
        });
    }

    public class updateAadharVerify extends AsyncTask<String,String,String> {

        SweetAlertDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new SweetAlertDialog(AadharVerifyPreview.this, SweetAlertDialog.PROGRESS_TYPE);
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

            if (result.equalsIgnoreCase("success")){

                Helper.dialog_success(VerifyPreview.this,"Success","Emp Photo verified Successfully","Ok");
            } else {

                Helper.dialog_warning(VerifyPreview.this,"Warning",result,"ok");
            }
        }
    }
}