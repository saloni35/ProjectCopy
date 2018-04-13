package com.example.acer.projectcopy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "";
    private EditText emailid;
    private EditText password;
    private Button login;
    private FirebaseAuth mAuth;
    private LoginButton fb_loginButton;
    private CallbackManager mCallbackManager;
    private DatabaseReference databaseReference;
    private Intent intent;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facebookSDKInitialize();
        setContentView(R.layout.activity_login);

        emailid = (EditText) findViewById(R.id.emailid_login_edittext);
        password = (EditText) findViewById(R.id.password_login_edittext);
        login = (Button) findViewById(R.id.login_button);
        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        intent = new Intent(LoginActivity.this, HomeActivity.class);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = emailid.getText().toString();
                final String pswd = password.getText().toString();
                progressDialog.setTitle("Please Wait....");
                progressDialog.setMessage("Logging In");
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loginUser(email, pswd);


                    }
                }).start();

            }
        });

        //mCallbackManager = CallbackManager.Factory.create();
        fb_loginButton = findViewById(R.id.fb_login_button);
        fb_loginButton.setReadPermissions("email", "public_profile", "read_custom_friendlists", "user_friends");

        fb_loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                getFacebookFriendsList(loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });

    }

    private void getFacebookFriendsList(LoginResult loginResult) {

        GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(
                loginResult.getAccessToken(),
                "/v2.12/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        //Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                        try {
                            JSONArray rawName = response.getJSONObject().getJSONArray("data");
                            intent.putExtra("jsondata", rawName.toString());
                            Log.d(TAG, "intent data: " + rawName.toString());
                            //      startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).executeAsync();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    protected void facebookSDKInitialize() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.<br />
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.<br />
        AppEventsLogger.deactivateApp(this);
    }


    private void handleFacebookAccessToken(final AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            final String current_user_id = mAuth.getCurrentUser().getUid();
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.hasChild(current_user_id)) {
                                        databaseReference = databaseReference.child(current_user_id);
//                                        databaseReference.child("user_emailid").setValue(emailid);
                                        databaseReference.child("user_status").setValue("Hey there, i am using travello");
                                        databaseReference.child("user_image").setValue("default_profile");
                                        databaseReference.child("user_thumb_image").setValue("default_image");
                                        databaseReference.child("user_name").setValue(" ");
                                        databaseReference.child("user_native_place").setValue("India");
                                        databaseReference.child("user_age").setValue("");
                                        databaseReference.child("user_hobbies").setValue("");
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                            Toast.makeText(LoginActivity.this, databaseReference.toString(), Toast.LENGTH_LONG).show();
                            //Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed." + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void loginUser(String email, String pswd) {

        if (email.isEmpty()) {
            emailid.setError("Cannot keep this field empty");
            emailid.requestFocus();
        } else if (pswd.isEmpty()) {
            password.setError("Cannot keep this field empty");
            password.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(email, pswd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthEmailException e) {
                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {

                                }
                                String TAG = "";
                                Log.d(TAG, "Authentication Failed :" + task.getException().getMessage());
                            }
                        }
                    });

        }
    }
}
