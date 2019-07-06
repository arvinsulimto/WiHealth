package com.niki.wihealth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    AVLoadingIndicatorView register_loader;
    TextView registerBtn;
    FrameLayout blur;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    AutoCompleteTextView firstName, lastName, email, phone;
    EditText password, rePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        blur = findViewById(R.id.background_blur_register);
        register_loader = findViewById(R.id.register_loader);

        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        rePassword = findViewById(R.id.re_password);
        phone = findViewById(R.id.phone);

        registerBtn = findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String uFullName = firstName.getText().toString() + " " + lastName.getText().toString();
                final String uEmail = email.getText().toString();
                String uPassword = password.getText().toString();
                String uRePassword = rePassword.getText().toString();
                final String uPhone = phone.getText().toString();

                if (uFullName.equals("") || uEmail.equals("") || uPassword.equals("") || uRePassword.equals("") || uPhone.equals("")){
                    Toast.makeText(getApplicationContext(), "All forms must be filled!", Toast.LENGTH_SHORT).show();
                }
                else if(!isValidEmail(uEmail)){
                    Toast.makeText(getApplicationContext(), "Email invalid!", Toast.LENGTH_SHORT).show();
                    email.setText("");
                }
                else if(!uPassword.equals(uRePassword)){
                    Toast.makeText(getApplicationContext(), "Password don't match!", Toast.LENGTH_SHORT).show();
                    rePassword.setText("");
                    password.setText("");
                }
                else{
                    blur.setBackgroundResource(R.drawable.button_background);
                    register_loader.setVisibility(View.VISIBLE);
                    register_loader.show();
                        firebaseAuth.createUserWithEmailAndPassword(uEmail, uPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                register_loader.setVisibility(View.INVISIBLE);
                                Map<String, Object> docData = new HashMap<>();
                                docData.put("name", uFullName);
                                docData.put("email", uEmail);
                                docData.put("point", 0);
                                docData.put("phone", uPhone);
                                docData.put("membership", "bronze");
                                docData.put("status", false);

                                String uid = firebaseAuth.getUid();

                                DocumentReference ref = db.collection("users").document(uid);
                                ref.set(docData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent intent = new Intent(getApplicationContext(), Login.class);
                                        startActivity(intent);
                                        Toast.makeText(Register.this, "Thank You for Registering WiHealth", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                            else{
                                Toast.makeText(Register.this, "Something went wrong, please try again in a few minutes", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });


    }

    public void openLoginPage(View view) {
        startActivity(new Intent(this,Login.class));
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
