package com.example.acer.projectcopy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "";
    private FirebaseAuth mAuth;
    private EditText emailid;
    private EditText password;
    private Button signupButton;
    private ProgressDialog dialog;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        emailid = (EditText) findViewById(R.id.emailid_signup_edittext);
        password = (EditText) findViewById(R.id.password_signup_edittext);
        signupButton = (Button) findViewById(R.id.signup_button);
        dialog=new ProgressDialog(this);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=emailid.getText().toString();
                String pswd=password.getText().toString();
                signupUser(email,pswd);
            }
        });

    }
    public  boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiInfo != null && wifiInfo.isConnected()) || (mobileInfo != null && mobileInfo.isConnected())) {
            return true;
        } else {
            showDialog();
            return false;
        }
    }
    private  void showDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Connect to wifi or quit")
                .setCancelable(false)
                .setPositiveButton("Connect to WIFI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNeutralButton("Go to Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_SETTINGS));
                    }
                })
                .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SignUpActivity.this.finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void signupUser(final String email, String pswd) {
        final String email_id = email;
        final String pass = pswd;
        if (email == null) {
            emailid.setError("Cannot keep this field empty");
        } else if (pswd == null) {
            password.setError("Cannot keep this field empty");
        }
        else {
            if (isConnected(SignUpActivity.this)) {
                dialog.setTitle("Creating User Account");
                dialog.setMessage("Please wait");
                dialog.show();
                mAuth.createUserWithEmailAndPassword(email, pswd)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String current_user_id = mAuth.getCurrentUser().getUid();
                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user_id);
                                    databaseReference.child("user_emailid").setValue(email_id);
                                    databaseReference.child("user_status").setValue("Hey there, i am using travello");
                                    databaseReference.child("user_image").setValue("default_profile");
                                    databaseReference.child("user_thumb_image").setValue("default_image");
                                    String name[] = email_id.split("@");
                                    databaseReference.child("user_name").setValue(name[0]);
                                    databaseReference.child("user_native_place").setValue("Default India");
                                    databaseReference.child("user_age").setValue(0);
                                    databaseReference.child("user_hobbies").setValue("");
                                    storageReference = firebaseStorage.getReference().child("User_Images");
                                    storageReference = firebaseStorage.getReference().child("User_Thumb_Images");
                                    Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthWeakPasswordException e) {
                                        Toast.makeText(SignUpActivity.this, "Weak Password:Minimum 6 characters required", Toast.LENGTH_SHORT).show();
                                    } catch (FirebaseAuthEmailException e) {
                                        Toast.makeText(SignUpActivity.this, "Invalid Email id", Toast.LENGTH_SHORT).show();
                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        Toast.makeText(SignUpActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                                    } catch (FirebaseAuthUserCollisionException e) {
                                        Toast.makeText(SignUpActivity.this, "User already exists with this email id", Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {

                                    }
                                    Toast.makeText(SignUpActivity.this, "Cannot Create Account: " + task.getException().getMessage() + " Cause: " + task.getException().getCause(), Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            }
                        });
            }
        }
    }
}



