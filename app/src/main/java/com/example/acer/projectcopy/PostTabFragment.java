package com.example.acer.projectcopy;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.acer.projectcopy.adapter.ParallaxNewsfeedAdapter;
import com.example.acer.projectcopy.model.DummyModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;


public class PostTabFragment extends Fragment {

    ListView mainContentList;

    private DatabaseReference firebaseReference;

    private ParallaxNewsfeedAdapter parallaxNewsfeedAdapter;
    private Activity mActivity;

    private String userid;

    public PostTabFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mActivity));

        userid = mActivity.getIntent().getStringExtra("userid");

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainContentList = (ListView) mActivity.findViewById(R.id.list_view);

        final ArrayList<DummyModel> arrayList = new ArrayList<>();


        firebaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseReference.keepSynced(true);

        firebaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String id = "";
                String image = "";
                String name = "";

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    if (childDataSnapshot.getKey().equals("Users")) {
                        name = childDataSnapshot.child(userid).child("user_name").getValue().toString();
                    }

                }
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    if (childDataSnapshot.getKey().equals("Posts")) {
                        for (DataSnapshot ds : childDataSnapshot.getChildren()) {
                            id = ds.child("authorId").getValue().toString();
                            if (id.equals(userid)) {
                                image = ds.child("imagePath").getValue().toString();
                            }
                            if (name != "" && id != "" && image != "") {
                                arrayList.add(new DummyModel(arrayList.size(), image, name, R.string.fontello_heart_empty));
                                parallaxNewsfeedAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        parallaxNewsfeedAdapter = new ParallaxNewsfeedAdapter(mActivity, arrayList, false);

        mainContentList.setAdapter(parallaxNewsfeedAdapter);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_tab, container, false);
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
