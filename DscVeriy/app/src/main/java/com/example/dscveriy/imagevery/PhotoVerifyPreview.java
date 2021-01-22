package com.example.dscveriy;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class PhotoVerifyPreview extends AppCompatActivity {

    private static final String NAME_SPACE = "";
    ImageView img_empPhoto;
    Button btn_empphotoverify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_verify_preview);

        img_empPhoto = findViewById(R.id.empphotoverify);
        btn_empphotoverify = findViewById(R.id.verify);

        final String Photourl = getIntent().getExtras().getString("Photourl");
        final String Photoverify = getIntent().getExtras().getString("Photoverify");
        final String EmpNo = getIntent().getExtras().getString("Empno");

        if (Photoverify != null){
            if (Photoverify.equalsIgnoreCase("true")){
                btn_empphotoverify.setVisibility(View.INVISIBLE);
            }
        }

        Picasso.get()
                .load("")
                .resize(250, 150)
                .centerCrop()
                .into(img_empPhoto);


        btn_empphotoverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                (new updatePhotoVerify()).execute(EmpNo);

            }
        });
    }


    public class updatePhotoVerify extends AsyncTask<String,String,String>{

        SweetAlertDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new SweetAlertDialog(PhotoVerifyPreview.this, SweetAlertDialog.PROGRESS_TYPE);
            p.setTitleText("Loading, Please Wait...");
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String[] paras = {"empcode"};
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

                Helper.dialog_success(PhotoVerifyPreview.this,"Success","Emp Photo verified Successfully","Ok");
            } else {

                Helper.dialog_warning(PhotoVerifyPreview.this,"Warning",result,"Ok");
            }
        }
    }
}