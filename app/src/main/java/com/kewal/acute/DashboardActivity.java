package com.kewal.acute;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSignOut;
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        mAuth = FirebaseAuth.getInstance();

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("   Dashboard");
            actionBar.setLogo(R.drawable.ic_action_name);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        findViewById(R.id.add_emp_card).setOnClickListener(this);
        findViewById(R.id.review_card).setOnClickListener(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        progressDialog = new ProgressDialog(DashboardActivity.this);

        progressDialog.show();
        progressDialog.setMessage("Fetching data please wait...");
        if (mAuth.getCurrentUser() == null) {
            progressDialog.dismiss();
            finish();
            startActivity(new Intent(DashboardActivity.this, LoginInActivity.class));
        }
        Query query = FirebaseDatabase.getInstance().getReference("Supervisors")
                .orderByChild("id").equalTo(mAuth.getUid());

        query.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.dashboard_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_ab:
                if (isLoggedIn()) {
                    mAuth.signOut();
                    LoginManager.getInstance().logOut();
                    Intent intent = new Intent(DashboardActivity.this, LoginInActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    signOut();
                }
                break;

                default:
                    Log.i("menu", "Nothing Clicked");

            /*case  R.id.profile_ab:
                startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
                break;*/
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mAuth.signOut();
                        Intent intent = new Intent(DashboardActivity.this, LoginInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_emp_card:
                startActivity(new Intent(DashboardActivity.this, AddEmployeeActivity.class));
                break;

            case R.id.review_card:
                startActivity(new Intent(DashboardActivity.this, SearchReviewActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Log.d("DashAct", "Back pressed");
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()) {
                Log.v("Prof", "User exists");
                progressDialog.dismiss();
                //Intent intent = new Intent(DashboardActivity.this, DashboardActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //startActivity(intent);
            } else {
                progressDialog.dismiss();
                Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
                startActivity(intent);
                Log.v("Prof", "New User");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.v("ProfileAct","Failed to fetch Profile");
            if (isLoggedIn()) {
                progressDialog.dismiss();
                mAuth.signOut();
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(DashboardActivity.this, LoginInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                signOut();
            }
        }
    };
}