package io.github.code10ftn.monumentum.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;

import io.github.code10ftn.monumentum.R;
import io.github.code10ftn.monumentum.model.Monument;
import io.github.code10ftn.monumentum.network.RestApi;
import io.github.code10ftn.monumentum.utils.MapUtils;

import static io.github.code10ftn.monumentum.activity.NewMonumentActivity.REQUEST_LOCATION_PERMISSION;

@EActivity(R.layout.activity_maps)
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final LatLng NOVI_SAD = new LatLng(45.2671, 19.8335);

    private static final int ZOOM_LEVEL = 12;

    @Extra
    Long monumentId;

    @ViewById
    Toolbar toolbar;

    @FragmentById
    SupportMapFragment mapFragment;

    @RestService
    RestApi restApi;

    private GoogleMap googleMap;

    private ClusterManager<Monument> clusterManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void afterViews() {
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        clusterManager = new ClusterManager<>(this, googleMap);
        googleMap.setOnCameraIdleListener(clusterManager);

        if (monumentId != null) {
            getSingleMonument(monumentId);
        } else {
            getAllMonuments();
        }
    }

    @Background
    void getSingleMonument(Long monumentId) {
        try {
            final Monument monument = restApi.getMonument(monumentId);
            final List<Monument> monuments = new ArrayList<>();
            monuments.add(monument);
            clusterManager.addItems(monuments);
            moveCamera(new LatLng(monument.getLatitude(), monument.getLongitude()));
        } catch (RestClientException e) {
            e.printStackTrace();
        }
    }

    @UiThread
    void moveCamera(LatLng latLng) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL));
    }

    @Background
    void getAllMonuments() {
        try {
            final List<Monument> monuments = restApi.getAllMonuments();
            clusterManager.addItems(monuments);
            moveCamera(getCurrentLocation());
        } catch (RestClientException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }

    private LatLng getCurrentLocation() {
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return NOVI_SAD;
        }

        final Location location = MapUtils.getLastKnownLocation(lm);
        if (location == null) {
            return NOVI_SAD;
        }

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        return new LatLng(latitude, longitude);
    }
}
