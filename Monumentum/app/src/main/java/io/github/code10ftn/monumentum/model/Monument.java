package io.github.code10ftn.monumentum.model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.github.code10ftn.monumentum.utils.ApiServiceConstants;
import io.github.code10ftn.monumentum.utils.Constants;

@DatabaseTable(tableName = "monuments")
public class Monument implements ClusterItem {

    @DatabaseField(generatedId = true)
    private int monumentId;

    @DatabaseField(columnName = "id", canBeNull = false)
    private long id;

    @DatabaseField(columnName = "name", canBeNull = false)
    private String name;

    @DatabaseField(columnName = "description", canBeNull = false)
    private String description;

    @DatabaseField(columnName = "photoUri", canBeNull = false)
    private String photoUri;

    @DatabaseField(columnName = "user", canBeNull = false, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private User uploaded;

    @DatabaseField(columnName = "location", canBeNull = false)
    private String location;

    @DatabaseField(columnName = "longitude", canBeNull = false)
    private double latitude;

    @DatabaseField(columnName = "latitude", canBeNull = false)
    private double longitude;

    @DatabaseField(columnName = "user_rating", canBeNull = false)
    private double userRating;

    @DatabaseField(columnName = "average_rating", canBeNull = false)
    private double averageRating;

    @DatabaseField(columnName = "favorite", canBeNull = false)
    private boolean favorite;

    @DatabaseField(columnName = "visited", canBeNull = false)
    private boolean visited;

    @ForeignCollectionField(columnName = "monuments", foreignFieldName = "monument", eager = true)
    private Collection<UserComment> userComments;

    private Double distance;

    public Monument() {
    }

    public Monument(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Monument(String name, String description, double latitude, double longitude) {
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getMonumentId() {
        return monumentId;
    }

    public void setMonumentId(int monumentId) {
        this.monumentId = monumentId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
        this.photoUri = String.format("%s%s/%d/image", ApiServiceConstants.ROOT_URL, ApiServiceConstants.MONUMENTS_PATH, this.id);
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

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
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

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public Collection<UserComment> getUserComments() {
        return userComments;
    }

    public void setUserComments(Collection<UserComment> userComments) {
        this.userComments = userComments;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(latitude, longitude);
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getSnippet() {
        return null;
    }

    public void updateDistance(Location location) {
        final double latDistance = Math.toRadians(latitude - location.getLatitude());
        final double lonDistance = Math.toRadians(longitude - location.getLongitude());

        final double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(location.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        distance = Constants.EARTH_RADIUS * c * 1000;
    }

    public void addComment(UserComment userComment) {
        final List<UserComment> comments = new ArrayList<>(this.userComments);
        comments.add(0, userComment);
        this.userComments = comments;
    }
}
