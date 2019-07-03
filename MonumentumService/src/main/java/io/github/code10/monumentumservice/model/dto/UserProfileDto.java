package io.github.code10.monumentumservice.model.dto;

import io.github.code10.monumentumservice.model.domain.User;

public class UserProfileDto {

    private String name;

    private String email;

    private String imageUri;

    public UserProfileDto() {
    }

    public UserProfileDto(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.imageUri = null;
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

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
