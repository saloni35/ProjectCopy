package com.example.acer.projectcopy;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.acer.projectcopy.font.RobotoTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileTabFragment extends Fragment {

    private Activity mActivity;
    private DatabaseReference databaseReference;
    private String intent_data;
    private RobotoTextView name;
    private RobotoTextView native_place;
    private RobotoTextView hobbies;
    private RobotoTextView age;
    private RobotoTextView status;

    public ProfileTabFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        intent_data = mActivity.getIntent().getStringExtra("userid");
        name = (RobotoTextView) mActivity.findViewById(R.id.profile_tab_name);
        native_place = (RobotoTextView) mActivity.findViewById(R.id.profile_tab_native_place);
        hobbies = (RobotoTextView) mActivity.findViewById(R.id.profile_tab_hobbies);
        age = (RobotoTextView) mActivity.findViewById(R.id.profile_tab_age);
        status = (RobotoTextView) mActivity.findViewById(R.id.profile_tab_status);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.getKey().equals(intent_data)) {
                        Users user = child.getValue(Users.class);

                        if (user.getUser_name() == null) {
                            Toast.makeText(mActivity, "user null", Toast.LENGTH_SHORT).show();
                        } else {
                            name.setText(user.getUser_name());
                            hobbies.setText(user.getUser_hobbies());
                            native_place.setText(user.getUser_native_place());
                            age.setText(user.getUser_age().toString());
                            status.setText(user.getUser_status());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
