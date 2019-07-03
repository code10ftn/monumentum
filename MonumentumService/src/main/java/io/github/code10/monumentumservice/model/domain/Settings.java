package io.github.code10.monumentumservice.model.domain;

import javax.persistence.Entity;

@Entity
public class Settings extends BaseModel {

    private boolean keepLoggedIn;

    private DistanceUnit distanceUnit;

    public Settings() {
        keepLoggedIn = false;
        distanceUnit = DistanceUnit.Kilometers;
    }

    public boolean isKeepLoggedIn() {
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
