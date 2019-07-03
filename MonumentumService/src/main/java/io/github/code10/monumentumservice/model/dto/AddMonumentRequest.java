package io.github.code10.monumentumservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class AddMonumentRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    private String location;

    private double longitude;

    private double latitude;

    public AddMonumentRequest() {
    }

    public AddMonumentRequest(@JsonProperty(value = "name", required = true) String name,
                              @JsonProperty(value = "description", required = true) String description,
                              @JsonProperty(value = "location", required = true) String location,
                              @JsonProperty(value = "longitude", required = true) double longitude,
                              @JsonProperty(value = "latitude", required = true) double latitude) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
