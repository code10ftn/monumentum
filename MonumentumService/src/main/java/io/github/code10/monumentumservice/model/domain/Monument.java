package io.github.code10.monumentumservice.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Monument extends BaseModel {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @JsonIgnore
    private String imageUri;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User uploaded;

    private String location;

    private double latitude;

    private double longitude;

    private int ratedCount;

    private double averageRating;

    public Monument() {
    }

    public Monument(String name, String description, String imageUri, User currentUser, String location, double latitude, double longitude) {
        this.name = name;
        this.description = description;
        this.imageUri = imageUri;
        this.uploaded = currentUser;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ratedCount = 0;
        this.averageRating = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public User getUploaded() {
        return uploaded;
    }

    public void setUploaded(User uploaded) {
        this.uploaded = uploaded;
    }

    public int getRatedCount() {
        return ratedCount;
    }

    public void setRatedCount(int ratedCount) {
        this.ratedCount = ratedCount;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
}
