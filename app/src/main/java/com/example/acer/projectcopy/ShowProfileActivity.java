package com.example.acer.projectcopy;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.acer.projectcopy.font.RobotoTextView;
import com.example.acer.projectcopy.util.ImageUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ShowProfileActivity extends AppCompatActivity {

    //private Activity mActivity;
    private MyPagerAdapter adapter;
    private TabLayout tabs;
    private ViewPager pager;
    private RobotoTextView username;
    private RobotoTextView userplace;
    private ImageView userimage;

    private DatabaseReference databaseReference;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        String userid = getIntent().getStringExtra("userid");

        username = (RobotoTextView) findViewById(R.id.show_profile_name);
        userplace = (RobotoTextView) findViewById(R.id.show_profile_place);
        userimage = (ImageView) findViewById(R.id.show_profile_user_image);
        tabs = (TabLayout) findViewById(R.id.show_profile_tabs);
        pager = (ViewPager) findViewById(R.id.show_profile_view_pager);

        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        pager.setCurrentItem(0);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username.setText(dataSnapshot.child("user_name").getValue().toString());
                userplace.setText("from " + dataSnapshot.child("user_native_place").getValue().toString());
                if (!dataSnapshot.child("user_image").getValue().toString().equals("default_image")) {
                    ImageUtil.displayImage(userimage, dataSnapshot.child("user_image").getValue().toString(), null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeAsUp: {
                super.onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final ArrayList<String> tabNames = new ArrayList<String>() {{
            add("Profile");
            add("Posts");
            add("Plans");
        }};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabNames.get(position);
        }

        @Override
        public int getCount() {
            return tabNames.size();
        }

        public android.support.v4.app.Fragment getItem(int position) {
            if (position == 0) {
                return new ProfileTabFragment();
            } else if (position == 1) {
                return new PostTabFragment();
            } else {
                return new PlanTabFragment();
            }

        }
    }

}
