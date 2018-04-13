package com.example.acer.projectcopy;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.acer.projectcopy.util.ImageUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class CreatePostFragment extends Fragment {

    private EditText postTitle;
    private EditText postDesc;
    private ImageView postImage;
    private Button createPost;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;
    private DatabaseReference databaseRefToId;

    private String userId;
    private ProgressDialog loadingBar;
    private Uri imageUri;

    private Uri downloadUri;

    private StorageReference storePostImageStorageReference;

    private Activity mActivity;

    public CreatePostFragment() {
        // Required empty public constructor
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromInputMethod(view.getWindowToken(), 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            ImageUtil.displayImage(postImage, imageUri.toString(), null);
        }
    }

    private void updatePostDetails() {
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        databaseRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        databaseRefToId = databaseRef.push();

        storePostImageStorageReference = FirebaseStorage.getInstance().getReference().child("Post_User_Images");
        StorageReference filePath = storePostImageStorageReference.child(databaseRefToId.getKey() + ".jpg");

        final Post post = new Post();
        // Uploading the image to firebase storage
        filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(mActivity, "Saving ur imge into fstorage", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mActivity, "Error occured while uploading image", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        downloadUri = taskSnapshot.getDownloadUrl();
                        post.setAuthorId(userId);
                        post.setCreatedDate(new Date().getTime());
                        post.setId(databaseRefToId.getKey().toString());
                        post.setDescription(postDesc.getText().toString());
                        post.setImageTitle(postTitle.getText().toString());
                        post.setImagePath(downloadUri.toString());
                        databaseRefToId.updateChildren(post.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                loadingBar.dismiss();
                                if (task.isSuccessful()) {
                                    Toast.makeText(mActivity, "Successfully Created Plan", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mActivity, "Please Try Again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        postTitle = (EditText) mActivity.findViewById(R.id.create_post_titleEditText);
        postDesc = (EditText) mActivity.findViewById(R.id.create_post_descriptionEditText);
        createPost = (Button) mActivity.findViewById(R.id.create_post_button);
        postImage = (ImageView) mActivity.findViewById(R.id.create_post_imageView);

        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 1);
            }
        });

        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View focusView = null;
                boolean cancel = false;
                String title = postTitle.getText().toString().trim();
                String description = postDesc.getText().toString().trim();

                if (TextUtils.isEmpty(description)) {
                    postDesc.setError("Fill this field");
                    focusView = postDesc;
                    cancel = true;
                }

                if (TextUtils.isEmpty(title)) {
                    postTitle.setError("Fill this field");
                    focusView = postTitle;
                    cancel = true;
                }

                if (imageUri == null) {
                    Dialog warningD = new Dialog(mActivity);
                    focusView = postImage;

                    cancel = true;
                }

                if (!cancel) {
                    loadingBar = new ProgressDialog(mActivity);
                    loadingBar.setTitle("Please Wait !!!");
                    loadingBar.setMessage("Creating Post");
                    loadingBar.show();
                    updatePostDetails();

                } else if (focusView != null) {
                    focusView.requestFocus();
                    hideKeyboard(mActivity);
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_post, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
