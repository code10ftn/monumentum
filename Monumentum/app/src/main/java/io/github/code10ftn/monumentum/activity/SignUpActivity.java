package io.github.code10ftn.monumentum.activity;

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
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.web.client.RestClientException;

import io.github.code10ftn.monumentum.R;
import io.github.code10ftn.monumentum.model.dto.SignUpRequest;
import io.github.code10ftn.monumentum.network.RestApi;

@EActivity(R.layout.activity_sign_up)
public class SignUpActivity extends AppCompatActivity {

    @ViewById
    EditText name;

    @ViewById
    EditText email;

    @ViewById
    EditText password;

    @ViewById
    Toolbar toolbar;

    @RestService
    RestApi restApi;

    @AfterViews
    void afterViews() {
        setSupportActionBar(toolbar);
    }

    @Click
    void signUp() {
        if (!fieldsFilled()) {
            showToast(getString(R.string.fields_empty));
            return;
        }

        attemptSignUp(new SignUpRequest(name.getText().toString(), email.getText().toString(), password.getText().toString()));
    }

    private boolean fieldsFilled() {
        final String nameText = name.getText().toString();
        final String emailText = email.getText().toString();
        final String passwordText = password.getText().toString();

        return !emailText.equals("") &&
                !passwordText.equals("") &&
                !nameText.equals("");
    }

    @Background
    void attemptSignUp(SignUpRequest request) {
        try {
            restApi.signup(request);
            signUpSuccess();
        } catch (RestClientException e) {
            showToast(getString(R.string.email_taken));
        }
    }

    @UiThread
    void signUpSuccess() {
        showToast(getString(R.string.signup_success));
        finish();
    }

    @Click
    void signIn() {
        finish();
    }

    @EditorAction
    void password(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            signUp();
        }
    }

    @UiThread
    void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
