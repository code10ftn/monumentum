package io.github.code10.monumentumservice.model.dto.auth;

import javax.validation.constraints.NotEmpty;

/**
 * Represents user's sign up request.
 * Used when user tries create an account.
 */
public class SignUpRequest {

    /**
     * User's name.
     */
    @NotEmpty
    private String name;

    /**
     * User's email.
     */
    @NotEmpty
    private String email;

    /**
     * User's password.
     */
    @NotEmpty
    private String password;

    public SignUpRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
