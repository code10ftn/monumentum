package io.github.code10ftn.monumentum.model.dto;

public class SigninResponse {

    private long id;

    private String token;

    public SigninResponse() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
