package com.niki.wihealth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Rating extends AppCompatActivity {
    RatingBar rating;
    Button btn;
    DataPassing dataPassing = DataPassing.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    static float finalPoint = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        btn = findViewById(R.id.btn_submit_rate);
        rating = findViewById(R.id.rate_bar);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DocumentReference ref = db.collection("users").document(firebaseAuth.getUid());
                ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        final long point = documentSnapshot.getLong("point");
                        String membership = documentSnapshot.getString("membership");
                        dataPassing.setMembership(membership);

                        ref.update("point", point + 10);
                        Log.d("Data Passing", dataPassing.getLocation());
                    }
                });
                final DocumentReference ref1 = db.collection("sport_center").document(dataPassing.getLocation());
                ref1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        finalPoint = rating.getRating();
                        Long r = documentSnapshot.getLong("rating");
                        String membership = dataPassing.getMembership();
                        if (membership.equals("silver")){
                            finalPoint = finalPoint * (long) 1.5;
                        } else if (membership.equals("gold")){
                            finalPoint = finalPoint * (long) 2;
                        }
                        finalPoint = Math.round(finalPoint * 10) / 10.0f;
                        ref1.update("rating", r + finalPoint);
                    }
                });
                Toast.makeText(Rating.this, "Your Rating is " + rating.getRating(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                finish();
            }
        });
    }
}
