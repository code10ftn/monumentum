package io.github.code10ftn.monumentum.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;

import io.github.code10ftn.monumentum.R;
import io.github.code10ftn.monumentum.adapter.CommentsAdapter;
import io.github.code10ftn.monumentum.database.wrapper.FavoriteMonumentDAOWrapper;
import io.github.code10ftn.monumentum.database.wrapper.VisitedMonumentDAOWrapper;
import io.github.code10ftn.monumentum.model.Monument;
import io.github.code10ftn.monumentum.model.UserComment;
import io.github.code10ftn.monumentum.model.dto.AddCommentRequest;
import io.github.code10ftn.monumentum.network.RestApi;
import io.github.code10ftn.monumentum.utils.ApiServiceConstants;
import io.github.code10ftn.monumentum.utils.Constants;
import io.github.code10ftn.monumentum.utils.MapUtils;
import io.github.code10ftn.monumentum.view.MonumentDetailsHeaderView;
import io.github.code10ftn.monumentum.view.MonumentDetailsHeaderView_;
import io.github.code10ftn.monumentum.view.NewCommentDialogView;
import io.github.code10ftn.monumentum.view.NewCommentDialogView_;


@OptionsMenu(R.menu.menu_map)
@EActivity(R.layout.activity_monument_details)
public class MonumentDetailsActivity extends BaseDrawerActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    @ViewById
    protected SwipeRefreshLayout refreshLayout;

    @Extra
    Long monumentId;

    @ViewById
    ListView listView;

    @ViewById
    FloatingActionButton addComment;

    @Bean
    CommentsAdapter adapter;

    @RestService
    RestApi restApi;

    @Bean
    FavoriteMonumentDAOWrapper favoriteMonumentDAOWrapper;

    @Bean
    VisitedMonumentDAOWrapper visitedMonumentDAOWrapper;

    private MonumentDetailsHeaderView headerView;

    private Monument monument;

    private String comment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void afterViews() {
        listView.setAdapter(adapter);
        headerView = MonumentDetailsHeaderView_.build(this);
        listView.addHeaderView(headerView);

        getMonument();

        refreshLayout.setOnRefreshListener(() -> {
            getMonument();
            refreshLayout.setRefreshing(false);
        });
    }

    @Background
    void getMonument() {
        if (monumentId == null) {
            return;
        }

        if (isNetworkAvailable()) {
            try {
                monument = restApi.getMonument(monumentId);
                showMonument();
            } catch (RestClientException e) {
                showToast(getString(R.string.network_error));
            }
        } else {
            showToast("Internet connection not available - you can only see your locally saved data.");
            monument = favoriteMonumentDAOWrapper.findMonumentById(monumentId);
            if (monument != null) {
                showMonument();
                return;
            }

            monument = visitedMonumentDAOWrapper.findMonumentById(monumentId);
            showMonument();
        }
    }

    @Click
    void addComment() {
        if (!isNetworkAvailable()) {
            showToast("Cannot post comments without internet connection!");
            return;
        }

        final NewCommentDialogView viewInflated = NewCommentDialogView_.build(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(viewInflated);
        builder.setPositiveButton("OK", (dialog, which) -> {
            comment = viewInflated.getInput().getText().toString();
            postComment();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        final AlertDialog dialog = builder.create();
        final Window window = dialog.getWindow();
        if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        dialog.show();
    }

    @Background
    void postComment() {
        try {
            final UserComment userComment = restApi.addComment(new AddCommentRequest(monumentId, comment));
            monument.addComment(userComment);

            setItems(new ArrayList<>(monument.getUserComments()));
        } catch (RestClientException e) {
            showToast("Error while posting comment!");
        }
    }

    @OptionsItem(R.id.action_map)
    void openMap() {
        MapsActivity_.intent(this).monumentId(monumentId).start();
    }

    @UiThread
    void showMonument() {
        if (monument == null) {
            return;
        }

        final Location location = getLocation();
        if (location != null) {
            monument.updateDistance(location);
        }

        monument.setPhotoUri(String.format("%s%s/%d/image", ApiServiceConstants.ROOT_URL, ApiServiceConstants.MONUMENTS_PATH, monument.getId()));
        setTitle(monument.getName());
        headerView.setMonument(monument);
        setItems(new ArrayList<>(monument.getUserComments()));
    }

    @UiThread
    void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @UiThread
    void setItems(List<UserComment> comments) {
        adapter.setComments(comments);
        adapter.notifyDataSetChanged();
    }

    @Receiver(actions = Constants.LOCATION_UPDATE, local = true)
    protected void onLocationUpdate(@Receiver.Extra Location location) {
        monument.updateDistance(location);
        headerView.setMonument(monument);
    }

    private Location getLocation() {
        // Get current location
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MonumentDetailsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return null;
        }

        return MapUtils.getLastKnownLocation(lm);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showMonument();
            }

            return;
        }
    }

    private boolean isNetworkAvailable() {
        final ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
