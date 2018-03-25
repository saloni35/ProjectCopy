package com.example.acer.projectcopy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;


public class ProfilePageFragment extends Fragment {

    private EditText statusEdit;
    private EditText nativePlaceEdit;
    private EditText usernameEdit;
    private Button updateButton;
    private CircleImageView userProfileImage;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference storeProfileImageRef;
    private Activity mActivity;
    private StorageReference thumbImageRef;
    private ProgressDialog loadingBar;

    private Bitmap thumb_bitmap = null;
    public ProfilePageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        statusEdit = (EditText) mActivity.findViewById(R.id.status_edit);
        nativePlaceEdit = (EditText) mActivity.findViewById(R.id.native_edit);
        usernameEdit = (EditText) mActivity.findViewById(R.id.name_edit);
        updateButton = (Button) mActivity.findViewById(R.id.update_button);
        userProfileImage = (CircleImageView) mActivity.findViewById(R.id.profile_pic_view);
        loadingBar=new ProgressDialog(mActivity);

        mAuth = FirebaseAuth.getInstance();
        thumbImageRef=FirebaseStorage.getInstance().getReference().child("Thumb_images");
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
                String usr_status=dataSnapshot.child("user_status").getValue().toString();
                String usr_name=dataSnapshot.child("user_name").getValue().toString();
//                String usr_native=dataSnapshot.child("user_native_place").getValue().toString();
                String usr_image=dataSnapshot.child("user_image").getValue().toString();
                String usr_thumb_image=dataSnapshot.child("user_thumb_image").getValue().toString();

                statusEdit.setText(usr_status);
//                nativePlaceEdit.setText(usr_native);
                usernameEdit.setText(usr_name);

                if(!usr_image.equals("default_image")) {
                   // Picasso.with(mActivity).load(usr_image).placeholder(R.mipmap.ic_account_circle_black_48dp).into(userProfileImage);
                    Picasso.with(mActivity).load(usr_image).into(userProfileImage);
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
 //               databaseReference.child("user_native_place").setValue(nativePlaceEdit.getText().toString());

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData()!=null) {
            Toast.makeText(mActivity, data.getData().toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(mActivity, data.getData().getPath().toString(), Toast.LENGTH_SHORT).show();
            Uri imageUri = data.getData();
            /*CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(mActivity);*/
        //}
       // if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
       //     CropImage.ActivityResult result = CropImage.getActivityResult(data);

            //if (resultCode == RESULT_OK) {
                loadingBar.setTitle("Updating Profile Image...");
                loadingBar.setMessage("Please Wait while we are updating your profile image");
                loadingBar.show();

               // Uri resultUri = result.getUri();
                Uri resultUri = data.getData();

                File thumb_filePathUri=new File(resultUri.getPath());
                String user_id=mAuth.getCurrentUser().getUid();

                try {
                    thumb_bitmap = new Compressor(mActivity)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(50)
                            .compressToBitmap(thumb_filePathUri);
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
                final byte[] thumb_byte=byteArrayOutputStream.toByteArray();

                StorageReference filepath=storeProfileImageRef.child(user_id+".jpg");
                final StorageReference thumb_filePath=thumbImageRef.child(user_id+".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(mActivity, "Saving your profile image to firebase storage...", Toast.LENGTH_SHORT).show();
                            final String downloadUrl = task.getResult().getDownloadUrl().toString();

                            databaseReference.child("user_image").setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(mActivity,"Image uploaded successfully",Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            UploadTask uploadTask = thumb_filePath.putBytes(thumb_byte);

                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                                    String thumb_downloadUrl = thumb_task.getResult().getDownloadUrl().toString();
                                    if (thumb_task.isSuccessful()) {
                                        Map update_user_data = new HashMap();
                                        update_user_data.put("user_image", downloadUrl);
                                        update_user_data.put("user_thumb_image", thumb_downloadUrl);

                                        databaseReference.updateChildren(update_user_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(mActivity, "Image uploaded successfully", Toast.LENGTH_SHORT);

                                                    loadingBar.dismiss();
                                                }
                                            }
                                        });
                                    }
                                }

                            });
                        }

                        else
                        {
                            Toast.makeText(mActivity,"Error occured while uploading your image...",Toast.LENGTH_SHORT).show();
                            //loadingBar.dismiss();
                        }
                    }
                });
            } /*else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();*/
            }
      //  }
    //}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_page, container, false);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
