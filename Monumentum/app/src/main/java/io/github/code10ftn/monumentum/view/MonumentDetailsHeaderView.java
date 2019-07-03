package io.github.code10ftn.monumentum.view;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.web.client.RestClientException;

import io.github.code10ftn.monumentum.R;
import io.github.code10ftn.monumentum.model.Monument;
import io.github.code10ftn.monumentum.model.dto.UpdateFavoriteMonumentRequest;
import io.github.code10ftn.monumentum.model.dto.UpdateMonumentRatingRequest;
import io.github.code10ftn.monumentum.model.dto.UpdateVisitedMonumentRequest;
import io.github.code10ftn.monumentum.network.RestApi;
import io.github.code10ftn.monumentum.utils.Constants;
import io.github.code10ftn.monumentum.utils.Preferences_;

@EViewGroup(R.layout.view_monument_details_header)
public class MonumentDetailsHeaderView extends LinearLayout {

    @ViewById
    TextView location;

    @ViewById
    TextView postedBy;

    @ViewById
    ImageView monumentImage;

    @ViewById
    ImageView visitImage;

    @ViewById
    ImageView favoriteImage;

    @ViewById
    TextView distance;

    @ViewById
    TextView description;

    @ViewById
    RatingBar ratingBar;

    @RestService
    RestApi restApi;

    @Pref
    Preferences_ prefs;

    private Monument monument;

    public MonumentDetailsHeaderView(Context context) {
        super(context);
    }

    public void setMonument(Monument monument) {
        this.monument = monument;

        location.setText(monument.getLocation());
        postedBy.setText(String.format("added by %s", monument.getUploaded().getName()));

        Glide.with(getContext())
                .load(monument.getPhotoUri())
                .into(monumentImage);

        description.setText(monument.getDescription());
        setupIcons();

        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (!isNetworkAvailable()) {
                showToast("Cannot rate monument without internet connection!");
            }

            rateMonument((int) rating);
        });

        if (monument.getDistance() != null) {
            final String unit = prefs.units().getOr(Constants.KILOMETERS);
            final String unitString = unit.equals(Constants.KILOMETERS) ? "km" : "mi";
            final double distanceString = monument.getDistance() / (unit.equals(Constants.KILOMETERS) ? 1000 : 1600);
            distance.setText(String.format("%s%s", Constants.DECIMAL_FORMAT.format(distanceString), unitString));
        }
    }

    @Click
    void favoriteImage() {
        if (!isNetworkAvailable()) {
            showToast("Cannot change favorite status without internet connection!");
            return;
        }

        markFavorite(!monument.isFavorite());
    }

    @Click
    void visitImage() {
        if (!isNetworkAvailable()) {
            showToast("Cannot change visited status without internet connection!");
            return;
        }

        markVisited(!monument.isVisited());
    }

    @Background
    void rateMonument(int rating) {
        try {
            restApi.rateMonument(new UpdateMonumentRatingRequest(monument.getId(), rating));
            monument.setUserRating(rating);
            setupIcons();
        } catch (RestClientException e) {
            showToast("Error while rating monument!");
        }
    }

    @Background
    void markFavorite(boolean favorite) {
        try {
            restApi.markFavorite(new UpdateFavoriteMonumentRequest(monument.getId(), favorite));
            monument.setFavorite(favorite);
            setupIcons();
        } catch (RestClientException e) {
            showToast("Error while marking monument as favorite!");
        }
    }

    @Background
    void markVisited(boolean visited) {
        try {
            restApi.markVisited(new UpdateVisitedMonumentRequest(monument.getId(), visited));
            monument.setVisited(visited);
            setupIcons();
        } catch (RestClientException e) {
            showToast("Error while marking monument as visited!");
        }
    }

    @UiThread
    void setupIcons() {
        ratingBar.setRating((float) monument.getUserRating());
        favoriteImage.setImageDrawable(monument.isFavorite() ? getResources().getDrawable(R.drawable.ic_heart) : getResources().getDrawable(R.drawable.ic_heart_outlined));
        visitImage.setImageDrawable(monument.isVisited() ? getResources().getDrawable(R.drawable.ic_visited) : getResources().getDrawable(R.drawable.ic_visited_outlined));
    }

    @UiThread
    void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private boolean isNetworkAvailable() {
        final ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
