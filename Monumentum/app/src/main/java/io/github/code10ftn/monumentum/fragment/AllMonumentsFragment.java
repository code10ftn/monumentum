package io.github.code10ftn.monumentum.fragment;

import android.annotation.SuppressLint;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.web.client.RestClientException;

import io.github.code10ftn.monumentum.R;
import io.github.code10ftn.monumentum.database.DatabaseHelper;
import io.github.code10ftn.monumentum.database.wrapper.MonumentDAOWrapper;
import io.github.code10ftn.monumentum.fragment.base.BaseMonumentsFragment;
import io.github.code10ftn.monumentum.model.Monument;
import io.github.code10ftn.monumentum.network.RestApi;
import io.github.code10ftn.monumentum.utils.ApiServiceConstants;

@EFragment(R.layout.fragment_monuments)
public class AllMonumentsFragment extends BaseMonumentsFragment {

    @RestService
    RestApi restApi;

    @Bean
    MonumentDAOWrapper monumentDAOWrapper;

    @Bean
    DatabaseHelper databaseHelper;

    @AfterInject
    void populateData() {
        getMonuments();
    }

    @SuppressLint("DefaultLocale")
    @Background
    protected void getMonuments() {
        if (!isNetworkAvailable()) {
            showToast("Internet connection not available - you can see your favorite and visited monuments only.");
            return;
        }

        try {
            monuments = restApi.getAllMonuments();
            for (Monument monument : monuments) {
                monument.setPhotoUri(String.format("%s%s/%d/image", ApiServiceConstants.ROOT_URL, ApiServiceConstants.MONUMENTS_PATH, monument.getId()));
            }

            setItems(monuments);
        } catch (RestClientException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (restApi != null) {
            getMonuments();
        }
    }
}
