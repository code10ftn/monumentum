package io.github.code10ftn.monumentum.network;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

import io.github.code10ftn.monumentum.utils.Preferences_;

@EBean(scope = EBean.Scope.Singleton)
public class AuthClientInterceptor implements ClientHttpRequestInterceptor {

    @Pref
    Preferences_ prefs;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        final HttpHeaders headers = request.getHeaders();
        headers.set("X-Auth-Token", prefs.token().get());

        return execution.execute(request, body);
    }
}
