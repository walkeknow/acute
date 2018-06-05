package com.kewal.acute;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class MainActivity extends AppCompatActivity {
    private Button Register;
    private EditText Email;
    private EditText Password;
    private TextView Signin;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        Register = (Button) findViewById(R.id.buttonRegister);
        Email = (EditText) findViewById(R.id.editTextEmail);
        Password = (EditText) findViewById(R.id.editTextPass);
        Signin = (TextView) findViewById(R.id.textViewOldUsr);

        getSupportActionBar().setTitle("Register on Acute");

        Register.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        registerUsr();
                    }
                }
        );

        Signin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        Intent intent = new Intent(MainActivity.this, LoginInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }
                }
        );
    }

    private void registerUsr(){
        String emailid = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();

        if(emailid.isEmpty()) {
            //email empty
            Toast.makeText(this, "E-mail field cannot be empty", Toast.LENGTH_SHORT).show();
        }else if(password.isEmpty()) {
            //password empty
            Toast.makeText(this, "Password field cannot be empty", Toast.LENGTH_SHORT).show();
        }else if(password.length() < 5) {
            //password is less than 5 characters
            Toast.makeText(this, "Password should be of minimum six characters ", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setMessage("Registering you, please wait...");
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(emailid, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.hide();
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, LoginMainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    } else {
                        Log.w(TAG, "signUpWithEmail:failed", task.getException());

                        if (task.getException() instanceof  FirebaseAuthUserCollisionException) {
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "User Registration Unsuccessful. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

}