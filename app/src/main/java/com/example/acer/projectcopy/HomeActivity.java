package com.example.acer.projectcopy;

import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

public class HomeActivity extends AppCompatActivity {

    ActionBarDrawerToggle drawerToggle;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    RelativeLayout mainContentLayout;
    int itemid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mainContentLayout=(RelativeLayout) findViewById(R.id.home_main_layout);

        drawerLayout = (DrawerLayout)findViewById(R.id.navigation_drawer);
        navigationView=(NavigationView)findViewById(R.id.navigation_view);

        final ProfileFragment pf=new ProfileFragment();
        final FindFriendsFragment ff=new FindFriendsFragment();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                item.setChecked(true);
                if(item.getItemId()==R.id.profile_navigation_item) {

                    FragmentTransaction ft;
                    if(ff.isAdded()) {
                        ft = getFragmentManager().beginTransaction().remove(ff);
                        ft.commit();
                    }
                    ft=getFragmentManager().beginTransaction().add(R.id.home_main_layout,pf);
                    ft.commit();
                }
                else if(item.getItemId()==R.id.friends_navigation_item) {

                    FragmentTransaction ft;
                    if(pf.isAdded()){
                        ft = getFragmentManager().beginTransaction().remove(pf);
                        ft.commit();
                    }
                    ft=getFragmentManager().beginTransaction().add(R.id.home_main_layout,ff);
                    ft.commit();
                }

                return true;

            }
        });

        drawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        {
            @Override
            public void onDrawerOpened(View drawerView) {
                mainContentLayout.setVisibility(View.INVISIBLE);
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mainContentLayout.setVisibility(View.VISIBLE);
                super.onDrawerClosed(drawerView);
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Home");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        else
        {
            return false;
        }
    }
}
