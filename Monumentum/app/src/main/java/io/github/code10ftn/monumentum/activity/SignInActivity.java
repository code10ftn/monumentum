package io.github.code10ftn.monumentum.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.web.client.RestClientException;

import io.github.code10ftn.monumentum.R;
import io.github.code10ftn.monumentum.model.dto.SignInRequest;
import io.github.code10ftn.monumentum.model.dto.SigninResponse;
import io.github.code10ftn.monumentum.network.RestApi;
import io.github.code10ftn.monumentum.utils.Preferences_;

@EActivity(R.layout.activity_sign_in)
public class SignInActivity extends AppCompatActivity {

    @ViewById
    EditText email;

    @ViewById
    EditText password;

    @ViewById
    Toolbar toolbar;

    @RestService
    RestApi restApi;

    @Pref
    Preferences_ prefs;

    @AfterViews
    void afterViews() {
        setSupportActionBar(toolbar);

        if (prefs.keepLogin().get() && prefs.token().exists()) {
            signinSuccess();
        } else {
            prefs.token().remove();
        }
    }

    @Click
    void signIn() {
        if (!fieldsFilled()) {
            showToast(getString(R.string.fields_empty));
            return;
        }

        attemptSignin(new SignInRequest(email.getText().toString(), password.getText().toString()));
    }

    private boolean fieldsFilled() {
        final String emailText = email.getText().toString();
        final String passwordText = password.getText().toString();

        return !emailText.equals("") &&
                !passwordText.equals("");
    }

    @Background
    void attemptSignin(SignInRequest signInRequest) {
        try {
            final SigninResponse response = restApi.signin(signInRequest);
            prefs.token().put(response.getToken());
            signinSuccess();
        } catch (RestClientException e) {
            showToast(getString(R.string.signin_error));
        }
    }

    @UiThread
    void signinSuccess() {
        Toast.makeText(this, R.string.welcome_text, Toast.LENGTH_SHORT).show();
        NavigationActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK).start();
    }

    @Click
    void signUp() {
        SignUpActivity_.intent(this).start();
    }

    @EditorAction
    void password(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            signIn();
        }
    }

    @UiThread
    void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
