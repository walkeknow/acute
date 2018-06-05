package com.kewal.acute;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class LoginInActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonLogin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewRegister;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPass);
        textViewRegister = (TextView) findViewById(R.id.textViewRegister);

        buttonLogin.setOnClickListener(this);
        textViewRegister.setOnClickListener(this);

        getSupportActionBar().setTitle("Login");

    }

    private void LoginUsr() {

        String emailid = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

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
            progressDialog.setMessage("Logging You In, please wait...");
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(emailid, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.hide();
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(LoginInActivity.this, LoginMainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    } else {
                        Log.w(TAG, "signInWithEmail:failed", task.getException());
                        Toast.makeText(LoginInActivity.this, "User Login Unsuccessful. " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewRegister:
                finish();
                Intent intent = new Intent(LoginInActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;

            case R.id.buttonLogin:
                LoginUsr();
                break;
        }
    }


}


