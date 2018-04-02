package com.example.acer.projectcopy;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.acer.projectcopy.adapter.DefaultAdapter;
import com.example.acer.projectcopy.model.DummyModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;


public class FacebookFriendsListFragment extends Fragment {

    private Activity mActivity;
    private DatabaseReference databaseReference;
    private DynamicListView mDynamicListView;

    public FacebookFriendsListFragment() {
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
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mActivity));
        return inflater.inflate(R.layout.fragment_facebook_friends_list, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*Intent intent = mActivity.getIntent();
        String jsondata = intent.getStringExtra("jsondata");
        JSONArray friendslist;
        ArrayList<String> friends = new ArrayList <String>();
        try {
            if(jsondata==null)
            {
                Toast.makeText(mActivity,"jsondata null",Toast.LENGTH_SHORT).show();
            }
            else {
                friendslist = new JSONArray(jsondata);
                if (friendslist == null) {
                    Toast.makeText(mActivity, "friendlist null", Toast.LENGTH_SHORT).show();
                } else {
                    for (int l = 0; l < friendslist.length(); l++) {
                        friends.add(friendslist.getJSONObject(l).getString("name"));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

     /*   ArrayAdapter adapter = new ArrayAdapter <String>(mActivity, R.layout.facebook_friends_list_layout, friends);
        ListView listView = (ListView) mActivity.findViewById(R.id.facebook_friends_list);
        listView.setAdapter(adapter);*/
        mDynamicListView = (DynamicListView) mActivity.findViewById(R.id.dynamic_listview);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.keepSynced(true);

        final ArrayList<DummyModel> arrayList = new ArrayList<>();
        final BaseAdapter adapter = new DefaultAdapter(mActivity, arrayList, false);

        AnimationAdapter animAdapter;
        animAdapter = new AlphaInAnimationAdapter(adapter);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                    Users user = childSnapshot.getValue(Users.class);

                    DummyModel dummyModel = new DummyModel(i, user.getUser_image(), user.getUser_name(), R.string.fontello_heart_empty);

                    arrayList.add(dummyModel);
                    i++;
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        animAdapter.setAbsListView(mDynamicListView);
        mDynamicListView.setAdapter(animAdapter);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }
}
