package io.github.code10ftn.monumentum.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import io.github.code10ftn.monumentum.R;
import io.github.code10ftn.monumentum.adapter.NavigationAdapter;
import io.github.code10ftn.monumentum.pager.NonSwipeableViewPager;
import io.github.code10ftn.monumentum.utils.Constants;

@EActivity(R.layout.activity_navigation)
public class NavigationActivity extends BaseDrawerActivity implements SearchView.OnQueryTextListener, LocationListener {

    @ViewById
    NonSwipeableViewPager viewPager;

    @ViewById
    BottomNavigationView bottomNavigation;

    private LocationManager locationManager;

    private FusedLocationProviderClient locationClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void setupNavigation() {
        final NavigationAdapter adapter = new NavigationAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        setTitle(R.string.menu_about);
        setNavigationListener();

        setupLocation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                final Intent intent = new Intent(Constants.INTENT_ACTION_SEARCH);
                intent.putExtra("searchString", "");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                return true;
            }
        });

        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);

        return true;
    }

    private void setNavigationListener() {
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_all:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.action_favourite:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.action_visited:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        });
    }

    private void setupLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                && locationManager != null) {
            getLastLocationDelayed();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        } else {
            Toast.makeText(this, "Some features won't work without GPS access!", Toast.LENGTH_LONG).show();
        }
    }

    private void getLastLocationDelayed() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            Log.d("NAVIGATION", "onLocationLast");
                            broadcastLocationChanged(location);
                        }
                    });
        }
    }

    @Click
    void addMonument() {
        NewMonumentActivity_.intent(this).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final Intent intent = new Intent(Constants.INTENT_ACTION_SEARCH);
        intent.putExtra("searchString", query);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        broadcastLocationChanged(location);
    }

    private void broadcastLocationChanged(Location location) {
        final Intent intent = new Intent(Constants.LOCATION_UPDATE);
        intent.putExtra("location", location);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
