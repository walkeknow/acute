package com.kewal.acute;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import static com.kewal.acute.LoginInActivity.VOLLEY_TAG;


public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 0;
    GoogleSignInClient mGoogleSignInClient;
    private Button Register;
    private EditText Email;
    private EditText Password;
    private TextView Signin;
    private ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    private static final String TAG = "MainActivity";
    private CallbackManager mCallbackManager;
    private static final String mMacAddress = "02:00:00:00:00:00";
    private static final String fileAddressMac = "/sys/class/net/wlan0/address";
    private static String android_id = null;
    String json_email_id = "null";
    String json_password = "null";
    String json_login_or_register = "register";
    String json_login_through;
    String json_device_id = "null";
    String json_device_mac = "nul";
    JSONObject jsonObject;

    RequestQueue queue;
    //private boolean connected;

    //private boolean connected;

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

        json_device_id= Settings.Secure.getString(MainActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);

        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        json_device_mac = retrieveAdresseMAC(wifi);

        getSupportActionBar().setTitle("Register on Acute");

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://jsonplaceholder.typicode.com/posts/1";



        // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                Log.d("MainAct", response);

                                try {
                                    JSONObject jsonObject =  new JSONObject(response.toString());

                                    String title = jsonObject.toString();

                                    Log.d("Title", title);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MainAct", error.getMessage());
                    }


                });

        // Add the request to the RequestQueue.
                queue.add(stringRequest);

        Register.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!connected()) {
                            Toast.makeText(MainActivity.this, "No network connection", Toast.LENGTH_SHORT).show();
                        } else {
                            registerUsr();
                        }
                    }
                }
        );

        Signin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        Intent intent = new Intent(MainActivity.this, LoginInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
        );

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!connected()) {
                    Toast.makeText(MainActivity.this, "No network connection", Toast.LENGTH_SHORT).show();
                } else {
                    signIn();
                }
            }
        });

        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!connected()) {
                    Toast.makeText(MainActivity.this, "No network connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                getFacebookID(loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });
    }

    //Check Network Connection
    private boolean connected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null) {
            Intent intentn = new Intent(MainActivity.this, DashboardActivity.class);
            intentn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentn);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
            }
        }
    }

    private void javaToJson() {
        LoginDetails loginDetails = new LoginDetails(json_email_id, json_password, json_login_or_register, json_login_through, json_device_id, json_device_mac);
        Gson gson = new Gson();
        String jsonString = gson.toJson(loginDetails);
        Log.d("json", jsonString);

        try {
            jsonObject = new JSONObject(jsonString);
            //callAPI();
        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private void callAPI() {

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this);

        String url = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2014-12-01&minmagnitude=7";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        LoginDetails loginDetails = extractFeatureFromJson(response.toString());

                        updateUi(loginDetails);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(MainActivity.this,"Sorry, that didn't work!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, error.getMessage());
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
        jsonObjectRequest.setTag(VOLLEY_TAG);
    }

    private LoginDetails extractFeatureFromJson(String stringJSON) {

        if (TextUtils.isEmpty(stringJSON)) {
            return null;
        }


        try {
            Gson gson = new Gson();
            LoginDetails loginDetails = gson.fromJson(stringJSON, LoginDetails.class);


            // Create a new {@link Event} object
            return loginDetails;
        } catch (JsonParseException e) {
            Log.e(TAG, "Problem parsing the earthquake JSON results", e);
        }
        return null;
    }

    private void updateUi(final LoginDetails loginDetails) {
        //do nothing if post request
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        progressDialog.setMessage("Signing you in, please wait...");
        progressDialog.show();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            progressDialog.hide();
                            Query query = FirebaseDatabase.getInstance().getReference("Supervisors")
                                    .orderByChild("id").equalTo(mAuth.getUid());

                            query.addListenerForSingleValueEvent(valueEventListener);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        progressDialog.setMessage("Signing you in, please wait...");
        progressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            getGoogleID();
                            Query query = FirebaseDatabase.getInstance().getReference("Supervisors")
                                    .orderByChild("id").equalTo(mAuth.getUid());

                            query.addListenerForSingleValueEvent(valueEventListener);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT );
                        }
                    }
                });
    }

    private void getGoogleID() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
        if (acct != null) {
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            json_email_id = personEmail;
            json_login_through = "G";
            javaToJson();

            Log.d("google_id", personEmail + personId);
        }
    }

    private  void getFacebookID(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("LoginActivity Response ", response.toString());

                        try {
                            String  Name = object.getString("name");

                            String FEmail = object.getString("email");
                            Log.v("fb", Name + FEmail);

                            json_email_id = FEmail;
                            json_login_through = "F";
                            javaToJson();
                            //Toast.makeText(getApplicationContext(), "Name " + Name, Toast.LENGTH_LONG).show();


                        } catch (JSONException e) {
                            Log.v("fb", e.getMessage());
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, email, gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
            json_email_id = emailid;
            json_password = password;
            json_login_through = "L";
            mAuth.createUserWithEmailAndPassword(emailid, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.hide();
                    if (task.isSuccessful()) {

                        javaToJson();
                        Toast.makeText(MainActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
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

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()) {
                Log.v("Prof", "User exists");
                progressDialog.dismiss();
                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                progressDialog.dismiss();
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                Log.v("Prof", "New User");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.v("ProfileAct","Failed to fetch Profile");
            if (isLoggedIn()) {
                mAuth.signOut();
                progressDialog.dismiss();
                LoginManager.getInstance().logOut();
                //Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //startActivity(intent);
            } else {
                signOut();
            }
        }
    };

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mAuth.signOut();
                        //Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //startActivity(intent);
                    }
                });
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public static String retrieveAdresseMAC(WifiManager wifiMan) {
        WifiInfo wifiInf = wifiMan.getConnectionInfo();

        if(wifiInf.getMacAddress().equals(mMacAddress)){
            String ret = null;
            try {
                ret= getAdressMacByInterface();
                if (ret != null){
                    return ret;
                } else {
                    ret = getAddressMacByFile(wifiMan);
                    return ret;
                }
            } catch (IOException e) {
                Log.e("MobileAccess", "Error reading property MAC address");
            } catch (Exception e) {
                Log.e("MobileAcces", "Error reading property MAC address");
            }
        } else{
            return wifiInf.getMacAddress();
        }
        return mMacAddress;
    }

    private static String getAdressMacByInterface(){
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (nif.getName().equalsIgnoreCase("wlan0")) {
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:",b));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            }

        } catch (Exception e) {
            Log.e("MobileAccess", "Error reading property MAC address");
        }
        return null;
    }

    private static String getAddressMacByFile(WifiManager wifiMan) throws Exception {
        String ret;
        int wifiState = wifiMan.getWifiState();

        wifiMan.setWifiEnabled(true);
        File fl = new File(fileAddressMac);
        FileInputStream fin = new FileInputStream(fl);
        StringBuilder builder = new StringBuilder();
        int ch;
        while((ch = fin.read()) != -1){
            builder.append((char)ch);
        }

        ret = builder.toString();
        fin.close();

        boolean enabled = WifiManager.WIFI_STATE_ENABLED == wifiState;
        wifiMan.setWifiEnabled(enabled);
        return ret;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(VOLLEY_TAG);
        }
    }
}