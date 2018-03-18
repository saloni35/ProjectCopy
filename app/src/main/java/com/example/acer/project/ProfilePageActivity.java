package com.example.acer.project;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilePageActivity extends AppCompatActivity {

    private EditText statusEdit;
    private EditText nativePlaceEdit;
    private EditText usernameEdit;
    private Button updateButton;
    private CircleImageView userProfileImage;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference storeProfileImageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        statusEdit = (EditText) findViewById(R.id.status_edit);
        nativePlaceEdit = (EditText) findViewById(R.id.native_edit);
        usernameEdit = (EditText) findViewById(R.id.name_edit);
        updateButton = (Button) findViewById(R.id.update_button);
        userProfileImage = (CircleImageView) findViewById(R.id.profile_pic_view);

        mAuth = FirebaseAuth.getInstance();
        storeProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile_Images");
        String current_user_id = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user_id);

        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 1);
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String image=dataSnapshot.child("user_image").getValue().toString();
                if(!image.equals("default_image")) {
                    Picasso.with(ProfilePageActivity.this).load(image).placeholder(R.mipmap.ic_account_circle_black_48dp).into(userProfileImage);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("user_status").setValue(statusEdit.getText().toString());
                databaseReference.child("user_name").setValue(usernameEdit.getText().toString());
                databaseReference.child("user_native_place").setValue(nativePlaceEdit.getText().toString());
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                String user_id=mAuth.getCurrentUser().getUid();
                StorageReference filepath=storeProfileImageRef.child(user_id+".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ProfilePageActivity.this,"Saving your profile image to firebase storage...",Toast.LENGTH_SHORT).show();
                            String downloadUrl=task.getResult().getDownloadUrl().toString();
                            databaseReference.child("user_image").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(ProfilePageActivity.this,"Image uploaded successfully",Toast.LENGTH_SHORT);

                                    }
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(ProfilePageActivity.this,"Error occured while uploading your image...",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}