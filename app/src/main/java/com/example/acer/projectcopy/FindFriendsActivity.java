package com.example.acer.projectcopy;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FindFriendsActivity extends AppCompatActivity {

    private EditText username;
    private Button findFriend;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Users,UsersViewHolder> recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        username=(EditText)findViewById(R.id.find_user_by_name);
        findFriend=(Button)findViewById(R.id.find_user_by_name_button);

        recyclerView=(RecyclerView)findViewById(R.id.find_friend_list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this.getParent());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        databaseReference=FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.keepSynced(true);
        
        findFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findFriendByUsername();
            }
        });
    }

    public void findFriendByUsername() {

        Toast.makeText(FindFriendsActivity.this, username.getText().toString(), Toast.LENGTH_SHORT).show();
        Query query = databaseReference.orderByKey();

        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(query, Users.class)
                        .build();

        ObservableSnapshotArray<Users> array= options.getSnapshots();
        if(array==null)
        {
            Toast.makeText(FindFriendsActivity.this,"null returned",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(FindFriendsActivity.this,array.isEmpty()+"not null",Toast.LENGTH_SHORT).show();

        }

        recyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
                options
        ) {
            @Override
            public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.find_friend_layout, parent, false);
                Toast.makeText(FindFriendsActivity.this,"com.example.acer.project.UsersViewHolder constructor",Toast.LENGTH_SHORT).show();
                return new UsersViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull Users model) {

                TextView username = holder.username;
                username.setText(model.getUser_name());
                TextView userstatus = holder.userstatus;
                userstatus.setText(model.getUser_status());
                Toast.makeText(FindFriendsActivity.this, "onbindviewholder", Toast.LENGTH_SHORT).show();
            }

        };
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.startListening();
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder{

        View mView;
        private TextView username;
        private TextView userstatus;
        public UsersViewHolder(View itemView) {
            super(itemView);

            mView=itemView;
            username=(TextView)mView.findViewById(R.id.find_friend_username);
            userstatus=(TextView)mView.findViewById(R.id.find_friend_user_status);
        }

       /* public void setUser_name(String user_name)
        {
            username.setText(user_name);
        }
        public void setUser_status(String user_status){
            userstatus.setText(user_status);
        }
*/
    }

}
