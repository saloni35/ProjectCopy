package com.example.acer.projectcopy;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.acer.projectcopy.adapter.ParallaxNewsfeedAdapter;
import com.example.acer.projectcopy.font.MaterialIconsTextView;
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
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
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

                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private DatabaseReference firebaseReference;
    private DatabaseReference getFirebaseReference;
    private RobotoTextView navHeaderUserName;
    private RobotoTextView navHeaderUserEmail;
    private ParallaxNewsfeedAdapter parallaxNewsfeedAdapter;
    private String user_email;
    private String image_path;
    private String name;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(HomeActivity.this));

        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait......");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(21, 91, 142)));

        mainContentList = (ListView) findViewById(R.id.list_view);
        mainContentLayout = (RelativeLayout) findViewById(R.id.home_main_layout);

        drawerLayout = (DrawerLayout)findViewById(R.id.navigation_drawer);
        navigationView=(NavigationView)findViewById(R.id.navigation_view);

        View headerView = navigationView.getHeaderView(0);

        navHeaderUserName = (RobotoTextView) headerView.findViewById(R.id.header_navigation_drawer_media_username);
        navHeaderUserEmail = (RobotoTextView) headerView.findViewById(R.id.header_navigation_drawer_media_email);
        final ImageView navHeaderUserImage = (ImageView) headerView.findViewById(R.id.header_navigation_drawer_media_image);

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

        final ArrayList<DummyModel> arrayList = new ArrayList<>();
        final ArrayList<DummyModel> tmp = new ArrayList<>();
        arrayList.add(new DummyModel(0, "http://pengaja.com/uiapptemplate/newphotos/listviews/googlecards/travel/0.jpg", "Jane Smith", R.string.fontello_heart_empty));
        arrayList.add(new DummyModel(1, "http://pengaja.com/uiapptemplate/newphotos/listviews/googlecards/travel/1.jpg", "Jane Smith", R.string.fontello_heart_empty));
        arrayList.add(new DummyModel(2, "http://pengaja.com/uiapptemplate/newphotos/listviews/googlecards/travel/2.jpg", "Jane Smith", R.string.fontello_heart_empty));


        final DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("Users");
        getFirebaseReference = FirebaseDatabase.getInstance().getReference().child("Posts");
        getFirebaseReference.keepSynced(true);

        getFirebaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childDs : dataSnapshot.getChildren()) {
                    final String id;
                    final String image;
                    id = childDs.child("authorId").getValue().toString();
                    image = childDs.child("imagePath").getValue().toString();
                    dr.addValueEventListener(new ValueEventListener() {
                        String name = "";

                        @Override
                        public void onDataChange(DataSnapshot drDataSnapshot) {

                            name = drDataSnapshot.child(id).child("user_name").getValue().toString();
                            arrayList.add(new DummyModel(arrayList.size(), image, name, R.string.fontello_heart_empty));
                            tmp.clear();
                            for (int i = arrayList.size() - 1; i >= 0; i--) {

                                tmp.add(arrayList.get(i));
                            }
                            parallaxNewsfeedAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        parallaxNewsfeedAdapter = new ParallaxNewsfeedAdapter(this, tmp, false);

        mainContentList.setAdapter(parallaxNewsfeedAdapter);



        final ProfileFragment profileFragment = new ProfileFragment();
        final SearchFriendsFragment searchFriendsFragment = new SearchFriendsFragment();
        final FacebookFriendsListFragment facebookFriendsListFragment = new FacebookFriendsListFragment();
        final CreatePlanFragment createPlanFragment = new CreatePlanFragment();
        final ShowPlanFragment showPlanFragment = new ShowPlanFragment();
        final CreatePostFragment createPostFragment = new CreatePostFragment();
        final MyPlansFragment myPlansFragment = new MyPlansFragment();

        final Menu menu = navigationView.getMenu();


        String menu_items[] = {"Profile", "Newsfeed", "Facebook Friends", "Search Friends", "Create Plans", "Show Plans", "Create Post", "My Plans"};
        String menu_icons[] = new String[8];
        menu_icons[0] = getString(R.string.material_icon_account);
        menu_icons[1] = getString(R.string.drawer_icon_newsfeed);
        menu_icons[2] = getString(R.string.drawer_icon_facebook_friends);
        menu_icons[3] = getString(R.string.drawer_icon_friends);
        menu_icons[4] = getString(R.string.drawer_icon_create_plans);
        menu_icons[5] = getString(R.string.drawer_icon_show_plans);
        menu_icons[6] = getString(R.string.drawer_icon_create_post);
        menu_icons[7] = getString(R.string.drawer_icon_show_plans);

        int i = 0;
        View child_view[] = new View[menu_items.length];
        while (i < menu_items.length) {
            final MenuItem item = menu.getItem(i);
            child_view[i] = item.getActionView();

            //String TAG = "Tag Message";
            Log.d("SDEBUG", i + "This is tag no:" + child_view[i].toString());
            MaterialIconsTextView mv = (MaterialIconsTextView) child_view[i].findViewById(R.id.list_item_navigation_drawer_media_icon);
            mv.setText(menu_icons[i]);
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
                while (image_path.isEmpty()) {

                }
                if (!image_path.equals("default_image")) {
                    ImageUtil.displayRoundImage(navHeaderUserImage, image_path, null);
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
                } else if (item.getItemId() == R.id.plan_create_navigation_item) {
                    mainContentList.setVisibility(View.INVISIBLE);
                    ft = getFragmentManager().beginTransaction().replace(R.id.home_main_layout, createPlanFragment);
                    ft.commit();
                } else if (item.getItemId() == R.id.plan_show_navigation_item) {
                    mainContentList.setVisibility(View.INVISIBLE);
                    ft = getFragmentManager().beginTransaction().replace(R.id.home_main_layout, showPlanFragment);
                    ft.commit();
                } else if (item.getItemId() == R.id.post_create_navigation_item) {
                    mainContentList.setVisibility(View.INVISIBLE);
                    ft = getFragmentManager().beginTransaction().replace(R.id.home_main_layout, createPostFragment);
                    ft.commit();
                } else if (item.getItemId() == R.id.my_plans_navigation_item) {
                    mainContentList.setVisibility(View.INVISIBLE);
                    ft = getFragmentManager().beginTransaction().replace(R.id.home_main_layout, myPlansFragment);
                    ft.commit();
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
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }

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
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
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
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
            return true;
        }
        return true;
    }


}
