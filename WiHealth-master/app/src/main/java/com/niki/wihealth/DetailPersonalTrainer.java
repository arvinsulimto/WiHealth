package com.niki.wihealth;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class DetailPersonalTrainer extends AppCompatActivity {
    ImageView btnCall;
    private static final int REQUEST_CALL = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_personal_trainer);

        btnCall = findViewById(R.id.imgCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "123";
                if(ContextCompat.checkSelfPermission(DetailPersonalTrainer.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(DetailPersonalTrainer.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
                }else{
                    String dial = "tel:"+number;
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CALL){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                String number = "123";
                if(ContextCompat.checkSelfPermission(DetailPersonalTrainer.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(DetailPersonalTrainer.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
                }else{
                    String dial = "tel:"+number;
                    startActivity(new Intent(Intent.ACTION_CALL,Uri.parse(dial)));
                }
            }


        }
    }
}


