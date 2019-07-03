package io.github.code10.monumentumservice.model.dto;

import io.github.code10.monumentumservice.model.domain.DistanceUnit;
import io.github.code10.monumentumservice.model.domain.Settings;

public class SettingsDto {

    private boolean keepLoggedIn;

    private DistanceUnit distanceUnit;

    public SettingsDto() {
    }

    public SettingsDto(Settings settings) {
        this.keepLoggedIn = settings.isKeepLoggedIn();
        this.distanceUnit = settings.getDistanceUnit();
    }

    public boolean getKeepLoggedIn() {
        return keepLoggedIn;
    }

    public void setKeepLoggedIn(boolean keepLoggedIn) {
        this.keepLoggedIn = keepLoggedIn;
    }

    public DistanceUnit getDistanceUnit() {
        return distanceUnit;
    }

    public void setDistanceUnit(DistanceUnit distanceUnit) {
        this.distanceUnit = distanceUnit;
    }
}
