package com.example.lijie.csci571hw9.Utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import java.util.List;

public class LocationUtility {
    private volatile static LocationUtility uniqueInstance;
    private LocationManager locationManager;
    private String locationProvider;
    private Location location;
    private Context Context;

    private LocationUtility(Context context) {
        this.Context = context;
        getLocation();
    }

    public static LocationUtility getInstance(Context context) {
        if (uniqueInstance == null) {
            synchronized (LocationUtility.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new LocationUtility(context);
                }
            }
        }
        return uniqueInstance;
    }

    private void getLocation() {
        locationManager = (LocationManager)Context.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders( true );
        if (providers.contains( LocationManager.NETWORK_PROVIDER )) {
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains( LocationManager.GPS_PROVIDER )) {
            locationProvider = LocationManager.GPS_PROVIDER;
        } else {
            return;
        }
        if (Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission( Context, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission( Context, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (ActivityCompat.checkSelfPermission( Context, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Context, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location location = locationManager.getLastKnownLocation( locationProvider );
        if (location != null) {
            setLocation( location );
        }
        locationManager.requestLocationUpdates( locationProvider, 0, 0, locationListener );
    }

    private void setLocation(Location location) {
        this.location = location;
        String address = "latitude：" + location.getLatitude() + "lontitude：" + location.getLongitude();
    }

    public Location showLocation() {
        return location;
    }

    public void removeLocationUpdatesListener() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission( Context, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission( Context, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (locationManager != null) {
            uniqueInstance = null;
            locationManager.removeUpdates( locationListener );
        }
    }

    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onLocationChanged(Location location) {
            location.getAccuracy();
            setLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }
    };
}
