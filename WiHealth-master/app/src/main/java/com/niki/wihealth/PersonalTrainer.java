package com.niki.wihealth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class PersonalTrainer extends AppCompatActivity {

    CardView cd_pt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_trainer);

        Toolbar toolbar  = findViewById(R.id.toolbar1);

        setSupportActionBar(toolbar);
        cd_pt= findViewById(R.id.cd_pt);

        cd_pt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalTrainer.this,DetailPersonalTrainer.class);
                startActivity(intent);
            }
        });
    }
}
