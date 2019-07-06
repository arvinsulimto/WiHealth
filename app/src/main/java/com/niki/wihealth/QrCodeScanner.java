package com.niki.wihealth;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrCodeScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView scannerView;
    static int MY_REQUEST_CODE = 100;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DataPassing dataPassing = DataPassing.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_REQUEST_CODE);
            }
        }

        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(com.google.zxing.Result result) {
        try{
            JSONObject object = new JSONObject(result.getText());
            final String location = object.getString("location");
            dataPassing.setLocation(location);
            Log.d("LOCATION", location);
            Intent intent = new Intent(QrCodeScanner.this, MainActivity.class);
            final DocumentReference ref1 = db.collection("users").document(firebaseAuth.getUid());
            ref1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot1) {
                    boolean status = documentSnapshot1.getBoolean("status");
                    if (status == false){
                        final DocumentReference ref = db.collection("sport_center").document(location);
                        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Long count = documentSnapshot.getLong("count");
                                ref.update("count", count + 1);
                                ref1.update("status", true);
                                Toast.makeText(QrCodeScanner.this, "Success join to this sport center", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else{
                        final DocumentReference ref = db.collection("sport_center").document(location);
                        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Long count = documentSnapshot.getLong("count");
                                ref.update("count", count - 1);
                                ref1.update("status", false);
                                Toast.makeText(QrCodeScanner.this, "Success go out sport center", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Rating.class));
                                finish();
                            }
                        });
                    }
                }
            });



            startActivity(intent);
            finish();
        } catch (Exception e){
            Toast.makeText(QrCodeScanner.this, "Aw, Snap!, Your Qr Code is broken", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(QrCodeScanner.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
