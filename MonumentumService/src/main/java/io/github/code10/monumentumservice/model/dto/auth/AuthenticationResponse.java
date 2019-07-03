package io.github.code10.monumentumservice.model.dto.auth;

public class AuthenticationResponse {

    private long id;

    private String token;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(long id, String token) {
        this.id = id;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
