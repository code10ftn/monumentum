package io.github.code10ftn.monumentum.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemSelect;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;

import io.github.code10ftn.monumentum.R;
import io.github.code10ftn.monumentum.model.dto.SettingsDto;
import io.github.code10ftn.monumentum.network.RestApi;
import io.github.code10ftn.monumentum.utils.Constants;
import io.github.code10ftn.monumentum.utils.NetworkUtils;
import io.github.code10ftn.monumentum.utils.Preferences_;

@EActivity(R.layout.activity_settings)
public class SettingsActivity extends AppCompatActivity {

    @ViewById
    Toolbar toolbar;

    @ViewById
    Switch keepLogin;

    @ViewById
    Spinner units;

    @Pref
    Preferences_ prefs;

    @RestService
    RestApi restApi;

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

        final List<String> unitList = new ArrayList<>();
        unitList.add(Constants.KILOMETERS);
        unitList.add(Constants.MILES);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, unitList);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        units.setAdapter(adapter);

        getSettings();
    }

    @CheckedChange
    void keepLogin() {
        prefs.keepLogin().put(keepLogin.isChecked());
        updateSettings();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }

    @ItemSelect
    void units(boolean selected, int position) {
        prefs.units().put(units.getSelectedItem().toString());
        updateSettings();
    }

    @Background
    void updateSettings() {
        try {
            restApi.updateSettings(new SettingsDto(prefs.keepLogin().get(), prefs.units().get()));
            showSuccess();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @UiThread
    void showSuccess() {
        Toast.makeText(this, "Settings uploaded to server", Toast.LENGTH_SHORT).show();
    }

    @Background
    void getSettings() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showToast("Internet connection not available - local settings will be used.");

            return;
        }

        try {
            final SettingsDto settings = restApi.getSettings();
            prefs.keepLogin().put(settings.isKeepLoggedIn());
            prefs.units().put(settings.getDistanceUnit());
        } catch (RestClientException e) {
            e.printStackTrace();
        }

        setSettings();
    }

    @UiThread
    void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @UiThread
    void setSettings() {
        keepLogin.setChecked(prefs.keepLogin().get());
        units.setSelection(((ArrayAdapter<String>) units.getAdapter()).getPosition(prefs.units().get()));
    }
}
