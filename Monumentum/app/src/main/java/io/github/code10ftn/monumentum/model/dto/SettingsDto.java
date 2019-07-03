package io.github.code10ftn.monumentum.model.dto;

public class SettingsDto {

    private boolean keepLoggedIn;

    private String distanceUnit;

    public SettingsDto() {
    }

    public SettingsDto(boolean keepLoggedIn, String distanceUnit) {
        this.keepLoggedIn = keepLoggedIn;
        this.distanceUnit = distanceUnit;
    }

    public boolean isKeepLoggedIn() {
        return keepLoggedIn;
    }

    public void setKeepLoggedIn(boolean keepLoggedIn) {
        this.keepLoggedIn = keepLoggedIn;
    }

    public String getDistanceUnit() {
        return distanceUnit;
    }

    public void setDistanceUnit(String distanceUnit) {
        this.distanceUnit = distanceUnit;
    }
}
