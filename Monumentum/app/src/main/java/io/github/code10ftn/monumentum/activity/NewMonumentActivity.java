package io.github.code10ftn.monumentum.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.github.code10ftn.monumentum.R;
import io.github.code10ftn.monumentum.network.RestApi;
import io.github.code10ftn.monumentum.utils.FileUtils;
import io.github.code10ftn.monumentum.utils.MapUtils;

@OptionsMenu(R.menu.menu_done)
@EActivity(R.layout.activity_new_monument)
public class NewMonumentActivity extends AppCompatActivity {

    private static final String TAG = NewMonumentActivity.class.getSimpleName();

    static final int REQUEST_IMAGE_CAPTURE = 1;

    static final int REQUEST_LOCATION_PERMISSION = 2;

    static final int REQUEST_CAMERA_PERMISSION = 3;

    static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 4;

    @ViewById
    Toolbar toolbar;

    @ViewById
    EditText name;

    @ViewById
    EditText description;

    @ViewById
    Button takePhoto;

    @ViewById
    SimpleDraweeView imageCaptured;

    @Bean
    FileUtils fileUtils;

    String imageUri;

    @RestService
    RestApi restApi;

    @AfterViews
    void afterViews() {
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        name.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(60)
        });

        description.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(300)
        });
    }

    @Click
    void takePhoto() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(NewMonumentActivity.this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);

            return;

        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(NewMonumentActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
            return;
        }

        final Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePicture.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (takePicture.resolveActivity(getPackageManager()) != null) {
            try {
                final File photoFile = fileUtils.createImage();
                imageUri = photoFile.getAbsolutePath();

                final Uri uriForFile = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".utils.GenericFileProvider", photoFile);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
                startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    @OnActivityResult(REQUEST_IMAGE_CAPTURE)
    void onResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Toast.makeText(this, imageUri, Toast.LENGTH_SHORT).show();

            final Uri uri = new Uri.Builder().scheme(UriUtil.LOCAL_FILE_SCHEME).path(imageUri).build();
            imageCaptured.setImageURI(uri);
        }
    }

    @OptionsItem(R.id.action_done)
    void add() {
        if (imageUri == null) {
            Toast.makeText(this, R.string.monument_image_mandatory, Toast.LENGTH_SHORT).show();
        } else if (name.getText().toString().isEmpty() || description.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.fields_mandatory, Toast.LENGTH_SHORT).show();
        } else {
            addMonument();
        }
    }

    @Background
    void addMonument() {
        final FileSystemResource image = new FileSystemResource(imageUri);

        // Get current location
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(NewMonumentActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return;
        }

        final Location location = MapUtils.getLastKnownLocation(lm);
        if (location == null) {
            showToast("Unable to obtain location - try again later.");
            return;
        }

        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        final String cityLocation = getLocation(latitude, longitude);

        try {
            restApi.addMonument(name.getText().toString(), description.getText().toString(), cityLocation,
                    String.valueOf(longitude), String.valueOf(latitude), image);
            showSuccess();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getLocation(double latitude, double longitude) {
        final Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        final List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                return String.format("%s, %s", addresses.get(0).getLocality(), addresses.get(0).getCountryName());
            } else {
                return "Unknown location";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Unknown location";
    }

    @UiThread
    void showSuccess() {
        Toast.makeText(this, R.string.success, Toast.LENGTH_SHORT).show();
        finish();
    }

    @UiThread
    void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission NOT granted!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case REQUEST_CAMERA_PERMISSION:
            case REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                } else {
                    Toast.makeText(this, "Permission NOT granted!", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }
}
