package io.github.code10ftn.monumentum.activity;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import io.github.code10ftn.monumentum.R;
import io.github.code10ftn.monumentum.utils.Preferences_;

@EActivity
public class BaseDrawerActivity extends AppCompatActivity {

    @ViewById
    NavigationView navigationView;

    @ViewById
    DrawerLayout drawer;

    ActionBarDrawerToggle drawerToggle;

    Toolbar toolbar;

    @Pref
    Preferences_ prefs;

    @AfterViews
    void addHamburger() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(item -> {
            drawer.closeDrawers();

            if (item.getItemId() == R.id.nav_browse) {
                if (!(this instanceof NavigationActivity)) {
                    NavigationActivity_.intent(getBaseContext()).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
                }
            } else if (item.getItemId() == R.id.nav_add) {
                NewMonumentActivity_.intent(getBaseContext()).start();
            } else if (item.getItemId() == R.id.nav_map) {
                MapsActivity_.intent(getBaseContext()).start();
            } else if (item.getItemId() == R.id.nav_settings) {
                SettingsActivity_.intent(getBaseContext()).start();
            }

            return true;
        });

        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                supportInvalidateOptionsMenu();
            }
        };

        drawerToggle.syncState();
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawer.addDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    @Override
    public void setContentView(int layoutResId) {
        final LinearLayout fullView = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base, null);
        final FrameLayout activityContainer = fullView.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResId, activityContainer, true);
        super.setContentView(fullView);
    }

    @Click(R.id.signoutButton)
    void signout() {
        prefs.token().remove();
        finish();
        SignInActivity_.intent(this).start();
    }
}
