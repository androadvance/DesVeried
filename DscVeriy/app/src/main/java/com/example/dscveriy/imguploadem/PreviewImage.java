package com.example.dscveriy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;


public class PreviewImage extends AppCompatActivity {

    private String fullScreenInd;
    ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_image);

    }
}