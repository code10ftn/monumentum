package io.github.code10.monumentumservice.model.dto;

import io.github.code10.monumentumservice.model.domain.*;

import java.util.ArrayList;
import java.util.List;

public class MonumentDetailDto {

    private long id;

    private String name;

    private String description;

    private String imageUri;

    private User uploaded;

    private String location;

    private double latitude;

    private double longitude;

    private int userRating;

    private double averageRating;

    private boolean favorite;

    private boolean visited;

    private List<UserCommentDto> userComments;

    public MonumentDetailDto() {
        this.userComments = new ArrayList<>();
    }

    public MonumentDetailDto(Monument monument, UserRatedMonument ratedMonument, UserFavoriteMonument favoriteMonument, UserVisitedMonument visitedMonument, List<UserCommentDto> userComments) {
        this();
        this.id = monument.getId();
        this.name = monument.getName();
        this.description = monument.getDescription();
        this.imageUri = monument.getImageUri();
        this.uploaded = monument.getUploaded();
        this.location = monument.getLocation();
        this.latitude = monument.getLatitude();
        this.longitude = monument.getLongitude();
        this.userRating = ratedMonument == null ? 0 : ratedMonument.getRating();
        this.averageRating = monument.getAverageRating();
        this.favorite = favoriteMonument != null;
        this.visited = visitedMonument != null;
        this.userComments = userComments;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public User getUploaded() {
        return uploaded;
    }

    public void setUploaded(User uploaded) {
        this.uploaded = uploaded;
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

    public int getUserRating() {
        return userRating;
    }

    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean getVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public List<UserCommentDto> getUserComments() {
        return userComments;
    }

    public void setUserComments(List<UserCommentDto> userComments) {
        this.userComments = userComments;
    }
}
