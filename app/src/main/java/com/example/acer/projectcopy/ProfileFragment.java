package com.example.acer.projectcopy;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

    private EditText statusEdit;
    private EditText nativePlaceEdit;
    private EditText usernameEdit;
    private EditText ageEdit;
    private EditText hobbiesEdit;
    private Button updateButton;
    private CircleImageView userProfileImage;
    private Activity mActivity;
    private StorageReference storeProfileImageStorageReference;

    private DatabaseReference getUserDatabaseReference;
    private FirebaseAuth mAuth;
    private Bitmap thumb_bitmap = null;
    private StorageReference thumbImageRef;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        statusEdit = (EditText) mActivity.findViewById(R.id.status_edit_l);
        nativePlaceEdit = (EditText) mActivity.findViewById(R.id.native_edit_l);
        usernameEdit = (EditText) mActivity.findViewById(R.id.name_edit_l);
        ageEdit = (EditText) mActivity.findViewById(R.id.age_edit_l);
        hobbiesEdit = (EditText) mActivity.findViewById(R.id.hobbies_edit_l);
        updateButton = (Button) mActivity.findViewById(R.id.update_button_l);
        userProfileImage = (CircleImageView) mActivity.findViewById(R.id.profile_pic_view_l);

        mAuth=FirebaseAuth.getInstance();
        String userid=mAuth.getCurrentUser().getUid();
        getUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
        storeProfileImageStorageReference = FirebaseStorage.getInstance().getReference().child("User_Images");
        thumbImageRef = FirebaseStorage.getInstance().getReference().child("Thumb_images");

        getUserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("user_name").getValue().toString();
                String status = dataSnapshot.child("user_status").getValue().toString();
                String image = dataSnapshot.child("user_image").getValue().toString();
                String native_place = dataSnapshot.child("user_native_place").getValue().toString();
                String thumb_image = dataSnapshot.child("user_thumb_image").getValue().toString();
                String age = dataSnapshot.child("user_age").getValue().toString();
                String hobbies = dataSnapshot.child("user_hobbies").getValue().toString();

                if(!image.equals("default_image")) {
                     Picasso.with(mActivity).load(image).placeholder(R.mipmap.ic_account_circle_black_48dp).into(userProfileImage);
                    //Picasso.with(mActivity).load(image).into(userProfileImage);
                }
                usernameEdit.setText(name);
                statusEdit.setText(status);
                nativePlaceEdit.setText(native_place);
                ageEdit.setText(age);
                hobbiesEdit.setText(hobbies);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mActivity,"Updating the database",Toast.LENGTH_SHORT).show();
                getUserDatabaseReference.child("user_status").setValue(statusEdit.getText().toString());
                getUserDatabaseReference.child("user_name").setValue(usernameEdit.getText().toString());
                getUserDatabaseReference.child("user_native_place").setValue(nativePlaceEdit.getText().toString());

            }
        });

        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            Uri imageUri = data.getData();
           /* CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(mActivity);*/
            // }
            //  if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            //   CropImage.ActivityResult result = CropImage.getActivityResult(data);

            // if (resultCode == RESULT_OK) {

            //   Uri resultUri = result.getUri();

            File thumb_filePathUri = new File(imageUri.getPath());
            String userid = mAuth.getCurrentUser().getUid();
            Toast.makeText(mActivity, "Path:" + imageUri.getPath(), Toast.LENGTH_SHORT).show();
            /*try {
                thumb_bitmap = new Compressor(mActivity)
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .setQuality(50)
                        .compressToBitmap(thumb_filePathUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            final byte[] thumb_byte = byteArrayOutputStream.toByteArray();*/
            StorageReference filePath = storeProfileImageStorageReference.child(userid + ".jpg");
            final StorageReference thumb_filePath = thumbImageRef.child(userid + ".jpg");

            filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(mActivity, "Saving ur imge into fstorage", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mActivity, "Error occured", Toast.LENGTH_SHORT).show();
                    }
                }
            })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUri = taskSnapshot.getDownloadUrl();
                            getUserDatabaseReference.child("user_image").setValue(downloadUri.toString());
                            //Picasso.with(mActivity).load(downloadUri).into(userProfileImage);
                        }
                    });
           /* UploadTask uploadTask = thumb_filePath.putBytes(thumb_byte);

            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                    String thumb_downloadUrl = thumb_task.getResult().getDownloadUrl().toString();
                    if (thumb_task.isSuccessful()) {
                        Toast.makeText(mActivity, "Saving thumb image into fstorage", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(mActivity, "Error occured", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    getUserDatabaseReference.child("user_thumb_image").setValue(downloadUri.toString());
                    Picasso.with(mActivity).load(downloadUri).into(userProfileImage);
                }
            });
*/

            //else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

            //Exception error = result.getError();
            //}
        }


    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
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
