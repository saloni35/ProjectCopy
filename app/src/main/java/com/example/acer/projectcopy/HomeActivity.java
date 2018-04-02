package com.example.acer.projectcopy;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.acer.projectcopy.adapter.ParallaxNewsfeedAdapter;
import com.example.acer.projectcopy.font.RobotoTextView;
import com.example.acer.projectcopy.model.DummyModel;
import com.example.acer.projectcopy.util.ImageUtil;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {

    ActionBarDrawerToggle drawerToggle;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ListView mainContentList;
    RelativeLayout mainContentLayout;
    int itemid;
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private DatabaseReference firebaseReference;
    private RobotoTextView navHeaderUserName;
    private RobotoTextView navHeaderUserEmail;


    private String user_email;
    private String image_path;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(HomeActivity.this));

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(21, 91, 142)));

        mainContentList = (ListView) findViewById(R.id.list_view);
        mainContentLayout = (RelativeLayout) findViewById(R.id.home_main_layout);

        drawerLayout = (DrawerLayout)findViewById(R.id.navigation_drawer);
        navigationView=(NavigationView)findViewById(R.id.navigation_view);

        View headerView = navigationView.getHeaderView(0);

        navHeaderUserName = (RobotoTextView) headerView.findViewById(R.id.header_navigation_drawer_media_username);
        navHeaderUserEmail = (RobotoTextView) headerView.findViewById(R.id.header_navigation_drawer_media_email);
        final ImageView navHeaderUserImage = (ImageView) headerView.findViewById(R.id.header_navigation_drawer_media_image);
        Toast.makeText(HomeActivity.this, headerView.toString() + " HEADER username:" + navHeaderUserName.getText(), Toast.LENGTH_LONG).show();

        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser();
        String userid = current_user.getUid();
        user_email = current_user.getEmail();

        firebaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
        firebaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                image_path = dataSnapshot.child("user_image").getValue().toString();
                name = dataSnapshot.child("user_name").getValue().toString();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        drawerLayout.setDrawerShadow(R.mipmap.drawer_shadow,
                GravityCompat.START);

        int color = ContextCompat.getColor(getApplicationContext(), R.color.material_grey_100);
        color = Color.argb(0xCD, Color.red(color), Color.green(color),
                Color.blue(color));
        navigationView.setBackgroundColor(color);

        navigationView.setBackgroundResource(R.drawable.background_media);
        /*Intent intent = getIntent();
        String jsondata = intent.getStringExtra("jsondata");

        if(jsondata==null)
        {
            Toast.makeText(HomeActivity.this,"jsondata null",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(HomeActivity.this,"jsondata not null",Toast.LENGTH_SHORT).show();
        }*/

        ArrayList<DummyModel> arrayList = new ArrayList<>();
        arrayList.add(new DummyModel(0, "http://pengaja.com/uiapptemplate/newphotos/listviews/googlecards/travel/0.jpg", "Jane Smith", R.string.fontello_heart_empty));
        arrayList.add(new DummyModel(1, "http://pengaja.com/uiapptemplate/newphotos/listviews/googlecards/travel/1.jpg", "Jane Smith", R.string.fontello_heart_empty));
        arrayList.add(new DummyModel(2, "http://pengaja.com/uiapptemplate/newphotos/listviews/googlecards/travel/2.jpg", "Jane Smith", R.string.fontello_heart_empty));
        mainContentList.setAdapter(new ParallaxNewsfeedAdapter(this, arrayList, false));

        final ProfileFragment profileFragment = new ProfileFragment();
        final FindFriendsFragment findFriendsFragment = new FindFriendsFragment();
        final SearchFriendsFragment searchFriendsFragment = new SearchFriendsFragment();
        final FacebookFriendsListFragment facebookFriendsListFragment = new FacebookFriendsListFragment();

        final Menu menu = navigationView.getMenu();


        String menu_items[] = {"Profile", "Newsfeed", "Facebook Friends", "Friends", "Invite Friends", "Add Account", "Privacy Settings",
                "Your Plan", "Groups"};
        int i = 0;
        View child_view[] = new View[menu_items.length];
        while (i < menu_items.length) {
            final MenuItem item = menu.getItem(i);
            child_view[i] = item.getActionView();
            RobotoTextView tv = (RobotoTextView) child_view[i].findViewById(R.id.list_item_navigation_drawer_media_title);
            tv.setText(menu_items[i]);
            child_view[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    menu.performIdentifierAction(item.getItemId(), 0);
                }
            });
            i++;
        }


        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mainContentLayout.setVisibility(View.INVISIBLE);
                navHeaderUserName.setText(name);
                if (!image_path.equals("default_image")) {
                    ImageUtil.displayRoundImage(navHeaderUserImage, image_path, null);
                    //Picasso.with(HomeActivity.this).load(image_path).placeholder(R.mipmap.ic_account_circle_black_48dp).into(navHeaderUserImage);
                } else {
                    ImageUtil.displayRoundImage(navHeaderUserImage, "@string/default_image_url", null);
                }
                navHeaderUserEmail.setText(user_email);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mainContentLayout.setVisibility(View.VISIBLE);
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                item.setChecked(true);
                FragmentTransaction ft;

                if (item.getItemId() == R.id.profile_navigation_item) {
                    mainContentList.setVisibility(View.INVISIBLE);
                    ft = getFragmentManager().beginTransaction().replace(R.id.home_main_layout, profileFragment);
                    ft.commit();
                } else if (item.getItemId() == R.id.facebook_friends_item) {
                    mainContentList.setVisibility(View.INVISIBLE);
                    ft = getFragmentManager().beginTransaction().replace(R.id.home_main_layout, facebookFriendsListFragment);
                    ft.commit();
                } else if (item.getItemId() == R.id.friends_navigation_item) {
                    mainContentList.setVisibility(View.INVISIBLE);
                    ft = getFragmentManager().beginTransaction().replace(R.id.home_main_layout, searchFriendsFragment);
                    ft.commit();

                } else if (item.getItemId() == R.id.newsfeed_navigation_item) {
                    mainContentLayout.removeAllViews();
                    mainContentLayout.addView(mainContentList);
                    mainContentList.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });



        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Home");
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu,menu);

        return true;
    }


    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        if (item.getItemId() == R.id.menu_link_fb)
        {

        }
        if (item.getItemId() == R.id.menu_logout_fb) {
            try {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
            } catch (Exception e) {
                Toast.makeText(HomeActivity.this, "logout failed", Toast.LENGTH_SHORT).show();
            }
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                Toast.makeText(HomeActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(HomeActivity.this, StartActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;

        }

        if (item.getItemId() == R.id.menu_logout) {
            Toast.makeText(HomeActivity.this, "Logout clicked", Toast.LENGTH_SHORT).show();
            try {
                FirebaseAuth.getInstance().signOut();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                Toast.makeText(HomeActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(HomeActivity.this, StartActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        if (item.getItemId() == R.id.menu_delete_user) {
            Toast.makeText(HomeActivity.this, "Delete User clicked", Toast.LENGTH_SHORT).show();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(HomeActivity.this, "User account deleted successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(HomeActivity.this, StartActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(HomeActivity.this, "Deletion failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            return true;
        } else {
            return false;
        }

    }

}
