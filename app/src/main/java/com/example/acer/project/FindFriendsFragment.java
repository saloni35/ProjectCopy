package com.example.acer.project;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class FindFriendsFragment extends Fragment {

    private Activity mActivity;
    private Button findFriend;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Users, UsersViewHolder> recyclerAdapter;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerOptions<Users> options;
    private Button applyFilter;
    private FindFriendsFragment currentFragment;

    public FindFriendsFragment() {
        // Required empty public constructor
        currentFragment=this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


     /*   recyclerView = (RecyclerView) mActivity.findViewById(R.id.fragment_find_friend_list);

        linearLayoutManager = new LinearLayoutManager(mActivity.getA());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (linearLayoutManager != null && recyclerView!=null)
            recyclerView.setLayoutManager(linearLayoutManager);


     //   recyclerView.setHasFixedSize(true);*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_find_friends, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_find_friend_List);
        findFriend = (Button) rootView.findViewById(R.id.fragment_search_user_by_name_button);
        applyFilter = (Button) rootView.findViewById(R.id.apply_filter_button);

        return rootView;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.keepSynced(true);
       /* if (findFriend == null) {
            Toast.makeText(mActivity, "Hello", Toast.LENGTH_SHORT).show();
        } */

        applyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApplyFilterFragment applyFilterFragment=new ApplyFilterFragment();
                FragmentTransaction ft;
                if(!applyFilterFragment.isAdded()) {
                    ft = getChildFragmentManager().beginTransaction().add(R.id.home_main_layout, applyFilterFragment);
                    ft.commit();
                }
                else {
                    ft = getChildFragmentManager().beginTransaction().show(applyFilterFragment);
                    ft.commit();
                }
                ft=getChildFragmentManager().beginTransaction().hide(currentFragment);
                ft.commit();
            }
        });

        findFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findFriendByUsername();
                v.invalidate();
            }
        });
    }

    public void findFriendByUsername() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (linearLayoutManager != null && recyclerView != null)
            recyclerView.setLayoutManager(linearLayoutManager);

        Toast.makeText(mActivity, "Searching Friends ... Please wait!!!!", Toast.LENGTH_SHORT).show();

        recyclerView.setHasFixedSize(true);
        Query query = databaseReference.orderByKey();

        options = new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(query, Users.class)
                .build();

        ObservableSnapshotArray<Users> array = options.getSnapshots();
       /* if (array == null) {
            Toast.makeText(mActivity, "null returned", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mActivity, array.size() + "not null", Toast.LENGTH_SHORT).show();

        } */

        recyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
                options
        ) {
            @Override
            public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.find_friend_layout, parent, false);
              //  Toast.makeText(mActivity, "com.example.acer.project.UsersViewHolder constructor", Toast.LENGTH_SHORT).show();
                return new UsersViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull Users model) {

                TextView username = holder.username;
                username.setText(model.getUser_name());
                TextView userstatus = holder.userstatus;
                userstatus.setText(model.getUser_status());
                //Toast.makeText(mActivity, "onbindviewholder", Toast.LENGTH_SHORT).show();
            }

        };

        //Toast.makeText(mActivity, "Hello", Toast.LENGTH_SHORT).show();
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.startListening();
        //    Toast.makeText(mActivity, username.getText().toString(), Toast.LENGTH_SHORT).show();


    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public TextView userstatus;
        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            username = (TextView) mView.findViewById(R.id.find_friend_username);
            userstatus = (TextView) mView.findViewById(R.id.find_friend_user_status);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
}

