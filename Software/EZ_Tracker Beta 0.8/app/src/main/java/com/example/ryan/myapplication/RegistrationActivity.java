package com.example.ryan.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbRef;
    public String name;
    public String height;
    public String weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //find IDs
        Button registerAccount = findViewById(R.id.registerRegister);
        Button registerBack = findViewById(R.id.registerBackButton);

        firebaseAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();

        registerAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextEmail = findViewById(R.id.registerEmail);
                final String email = editTextEmail.getText().toString().trim();

                EditText editTextPassword = findViewById(R.id.registerPassword);
                String password = editTextPassword.getText().toString().trim();

                EditText editTextName = findViewById(R.id.registerName);
                name = editTextName.getText().toString().trim();

                EditText editTextHeight = findViewById(R.id.registerHeight);
                height = editTextHeight.getText().toString().trim();

                EditText editTextWeight = findViewById(R.id.registerWeight);
                weight = editTextWeight.getText().toString().trim();

                if (TextUtils.isEmpty(name)){
                    Toast.makeText(RegistrationActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(RegistrationActivity.this, R.string.toastReg_email, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Patterns.EMAIL_ADDRESS.matcher(email).matches() == false){
                    Toast.makeText(RegistrationActivity.this, R.string.toastReg_email_two, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegistrationActivity.this, R.string.toastReg_pw, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 8){
                    Toast.makeText(RegistrationActivity.this, R.string.password_len_err, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(height)){
                    Toast.makeText(RegistrationActivity.this, R.string.register_height_err, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(weight)){
                    Toast.makeText(RegistrationActivity.this, R.string.register_weight_Err, Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();

                            EditText editTextDob = findViewById(R.id.registerDob);
                            String dob = editTextDob.getText().toString().trim();

                            UserInformation userInfo = new UserInformation(name, height, weight, dob);

                            dbRef.child(user.getUid()).setValue(userInfo);

                            dbRef.child(user.getUid()).child("steps").child("monday").setValue(0);
                            dbRef.child(user.getUid()).child("steps").child("tuesday").setValue(0);
                            dbRef.child(user.getUid()).child("steps").child("wednesday").setValue(0);
                            dbRef.child(user.getUid()).child("steps").child("thursday").setValue(0);
                            dbRef.child(user.getUid()).child("steps").child("friday").setValue(0);
                            dbRef.child(user.getUid()).child("steps").child("saturday").setValue(0);
                            dbRef.child(user.getUid()).child("steps").child("sunday").setValue(0);
                            dbRef.child(user.getUid()).child("steps").child("goalSteps").setValue(35000);
                            dbRef.child(user.getUid()).child("steps").child("calories").setValue(0);
                            dbRef.child(user.getUid()).child("steps").child("realSteps").setValue(0);

                            Toast.makeText(RegistrationActivity.this, R.string.toastReg_success, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                            finish();
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegistrationActivity.this, R.string.toastReg_fail, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        registerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        //save data
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
        editor.commit();
    }

    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocale(language);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        finish();
        startActivity(intent);
    }
}