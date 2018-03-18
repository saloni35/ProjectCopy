package com.example.acer.project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class OtherUserProfileActivity extends AppCompatActivity {

    private TextView username;
    private TextView userstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);

        username=(TextView)findViewById(R.id.user_name);
        userstatus=(TextView)findViewById(R.id.user_status);
    }
}
