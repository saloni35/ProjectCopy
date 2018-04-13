package com.example.acer.projectcopy;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.acer.projectcopy.adapter.DrawerAdapter;
import com.example.acer.projectcopy.font.RobotoTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;


public class RedefinedHomeActivity extends AppCompatActivity {

    LinearLayout mainContentLayout;
    private ListView mDrawerList;
    private List<DrawerItem> mDrawerItems;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle = "Drawer";
    private CharSequence mTitle = "Home";
    private Handler mHandler;
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
        setContentView(R.layout.activity_redefined_home);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.open, R.string.close) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                supportInvalidateOptionsMenu();
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mTitle = mDrawerTitle = getTitle();
        mDrawerList = (ListView) findViewById(R.id.list_view);

        //mDrawerLayout.setDrawerShadow(R.mipmap.drawer_shadow,
        // GravityCompat.START);
        View headerView = getLayoutInflater().inflate(
                R.layout.header_navigation_drawer_media, mDrawerList, false);
        mDrawerList.addHeaderView(headerView);
        prepareNavigationDrawerItems();
        mDrawerList.setAdapter(new DrawerAdapter(this, mDrawerItems, true));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerList
                .setBackgroundResource(R.drawable.background_media);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mHandler = new Handler();


    }

    @Override
    protected void onResume() {
        super.onResume();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                //  mDrawerList
                //           .setBackgroundResource(R.drawable.background_media);
            }
        };
        runnable.run();

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
       /* if (item.getItemId() == R.id.menu_link_fb)
        {

        }
        if (item.getItemId() == R.id.menu_logout_fb) {
            try {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
            } catch (Exception e) {
                Toast.makeText(RedefinedHomeActivity.this, "logout failed", Toast.LENGTH_SHORT).show();
            }
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                Toast.makeText(RedefinedHomeActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(RedefinedHomeActivity.this, StartActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;

        }

        if (item.getItemId() == R.id.menu_logout) {
            Toast.makeText(RedefinedHomeActivity.this, "Logout clicked", Toast.LENGTH_SHORT).show();
            try {
                FirebaseAuth.getInstance().signOut();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                Toast.makeText(RedefinedHomeActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(RedefinedHomeActivity.this, StartActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        if (item.getItemId() == R.id.menu_delete_user) {
            Toast.makeText(RedefinedHomeActivity.this, "Delete User clicked", Toast.LENGTH_SHORT).show();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RedefinedHomeActivity.this, "User account deleted successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RedefinedHomeActivity.this, StartActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(RedefinedHomeActivity.this, "Deletion failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            return true;
        } */
        else {
            return false;
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    private void prepareNavigationDrawerItems() {
        mDrawerItems = new ArrayList<>();
        mDrawerItems.add(new DrawerItem(R.string.drawer_icon_profile,
                R.string.drawer_title_profile,
                DrawerItem.DRAWER_ITEM_TAG_PROFILE));
        mDrawerItems.add(new DrawerItem(R.string.drawer_icon_newsfeed,
                R.string.drawer_title_newsfeed,
                DrawerItem.DRAWER_ITEM_TAG_NEWSFEED));
        mDrawerItems.add(new DrawerItem(R.string.drawer_icon_facebook_friends,
                R.string.drawer_title_facebook_friends,
                DrawerItem.DRAWER_ITEM_TAG_FACEBOOK_FRIENDS));
        mDrawerItems.add(new DrawerItem(R.string.drawer_icon_friends,
                R.string.drawer_title_friends,
                DrawerItem.DRAWER_ITEM_TAG_FRIENDS));
        mDrawerItems.add(new DrawerItem(R.string.drawer_icon_create_plans,
                R.string.drawer_title_create_plans,
                DrawerItem.DRAWER_ITEM_TAG_CREATE_PLANS));
        mDrawerItems.add(new DrawerItem(R.string.drawer_icon_show_plans,
                R.string.drawer_title_show_plans,
                DrawerItem.DRAWER_ITEM_TAG_SHOW_PLANS));
        mDrawerItems.add(new DrawerItem(R.string.drawer_icon_create_post,
                R.string.drawer_title_create_post,
                DrawerItem.DRAWER_ITEM_TAG_CREATE_POST));
        mDrawerItems.add(new DrawerItem(R.string.drawer_icon_invite_friends,
                R.string.drawer_title_invite_friends,
                DrawerItem.DRAWER_ITEM_TAG_INVITE_FRIENDS));
        mDrawerItems.add(new DrawerItem(
                R.string.drawer_icon_add_account,
                R.string.drawer_title_add_account,
                DrawerItem.DRAWER_ITEM_TAG_ADD_ACCOUNT));

    }

    private void selectItem(int position, int drawerTag) {
        if (position == 2 || position == 7 || position == 8 || position == 9) {

        } else {
            Fragment fragment = getFragmentByDrawerTag(drawerTag);
            commitFragment(fragment);
        }

        mDrawerList.setItemChecked(position, true);
        setTitle(mDrawerItems.get(position).getTitle());
        mDrawerLayout.closeDrawer(mDrawerList);

    }

    private Fragment getFragmentByDrawerTag(int drawerTag) {
        Fragment fragment;
        if (drawerTag == DrawerItem.DRAWER_ITEM_TAG_PROFILE) {
            fragment = new ProfilePageFragment();
        } else if (drawerTag == DrawerItem.DRAWER_ITEM_TAG_FACEBOOK_FRIENDS) {
            fragment = new FacebookFriendsListFragment();
        } else if (drawerTag == DrawerItem.DRAWER_ITEM_TAG_FRIENDS) {
            fragment = new SearchFriendsFragment();
        } else if (drawerTag == DrawerItem.DRAWER_ITEM_TAG_CREATE_PLANS) {
            fragment = new CreatePlanFragment();
        } else if (drawerTag == DrawerItem.DRAWER_ITEM_TAG_SHOW_PLANS) {
            fragment = new ShowPlanFragment();
        } else {
            fragment = new Fragment();
        }

        return fragment;
    }

    public void commitFragment(Fragment fragment) {
        // Using Handler class to avoid lagging while
        // committing fragment in same time as closing
        // navigation drawer
        mHandler.post(new CommitFragmentRunnable(fragment));
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position, mDrawerItems.get(position).getTag());
        }
    }

    private class CommitFragmentRunnable implements Runnable {

        private Fragment fragment;

        public CommitFragmentRunnable(Fragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public void run() {

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();
        }
    }
}