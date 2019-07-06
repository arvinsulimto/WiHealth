package com.niki.wihealth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Profile extends AppCompatActivity {
    TextView tvName, tvEmail, tvMembership, tvPhone, tvPoint;
    Button btnLogout;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String name, email, phone, membership;
    long point;
    FrameLayout blur;
    AVLoadingIndicatorView profile_loader;

    LinearLayout linearReward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profile_loader = findViewById(R.id.profile_loader);
        blur = findViewById(R.id.background_blur_profile);
    }

    @Override
    protected void onStart() {
        super.onStart();
        blur.setBackgroundResource(R.drawable.button_background);
        profile_loader.setVisibility(View.VISIBLE);
        profile_loader.show();

        if (firebaseAuth.getCurrentUser() != null){
            tvName = findViewById(R.id.tv_profile_name);
            tvEmail = findViewById(R.id.tv_profile_email);
            tvMembership = findViewById(R.id.tv_profile_membership);
            tvPhone = findViewById(R.id.tv_profile_phone);
            btnLogout = findViewById(R.id.btn_logout);
            tvPoint = findViewById(R.id.tv_profile_point);
            linearReward= findViewById(R.id.linear_Reward);

            DocumentReference ref = db.collection("users").document(firebaseAuth.getUid());

            ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    name = documentSnapshot.getString("name");
                    email = documentSnapshot.getString("email");
                    phone = documentSnapshot.getString("phone");
                    membership = documentSnapshot.getString("membership");
                    point = documentSnapshot.getLong("point");

                    tvPoint.setText(Long.toString(point));
                    tvName.setText(name);
                    tvEmail.setText(email);
                    tvMembership.setText(membership);
                    tvPhone.setText(phone);
                    profile_loader.setVisibility(View.INVISIBLE);
                    blur.setVisibility(View.INVISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Error getting data, please try again", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                }
            });

        }
        else {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        linearReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(intent, 0);

        return true;
    }

}
