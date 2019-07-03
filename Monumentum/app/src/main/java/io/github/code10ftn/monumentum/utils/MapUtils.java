package io.github.code10ftn.monumentum.utils;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationManager;

import java.util.List;

public class MapUtils {

    public static Location getLastKnownLocation(LocationManager locationManager) {
        final List<String> providers = locationManager.getProviders(true);
        Location location = null;

        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }

            if (location == null || l.getAccuracy() < location.getAccuracy()) {
                location = l;
            }
        }

        return location;
    }
}
