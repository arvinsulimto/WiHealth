package com.niki.wihealth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.wang.avi.AVLoadingIndicatorView;

import java.net.InterfaceAddress;


public class Login extends AppCompatActivity {
    TextView login;
    AutoCompleteTextView email;
    EditText password;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FrameLayout blur;
    AVLoadingIndicatorView login_loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.sign_in_login);
        login_loader = findViewById(R.id.login_loader);
        blur = findViewById(R.id.background_blur_login);
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String uEmail = email.getText().toString();
                String uPassword = password.getText().toString();

                if (uEmail.equals("") || uPassword.equals("")){
                    Toast.makeText(getApplicationContext(), "All forms must be filled!", Toast.LENGTH_SHORT).show();
                }
                else if(!isValidEmail(uEmail)) {
                    Toast.makeText(getApplicationContext(), "Email invalid!", Toast.LENGTH_SHORT).show();
                    email.setText("");
                    password.setText("");
                }
                else{
                    blur.setBackgroundResource(R.drawable.button_background);
                    login_loader.setVisibility(View.VISIBLE);
                    login_loader.show();

                    firebaseAuth.signInWithEmailAndPassword(uEmail, uPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                login_loader.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                login_loader.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "Username or Password is Invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public void openSignupPage(View view) {
        startActivity(new Intent(this, Register.class));
    }
}

