package org.ieselcaminas.pmdm.googlemaps1_2018;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_GPS = 123;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MapsActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_GPS);
        } else {
            setLocationEnabled();
        }
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        UiSettings mapSettings = mMap.getUiSettings();
        mapSettings.setZoomControlsEnabled(true);
        mapSettings.setCompassEnabled(true);
        mapSettings.setMyLocationButtonEnabled(true);
        mapSettings.setZoomGesturesEnabled(true);
        mapSettings.setScrollGesturesEnabled(true);
        mapSettings.setTiltGesturesEnabled(true);
        mapSettings.setRotateGesturesEnabled(true);

        Geocoder geocoder = new Geocoder(this, new Locale("ES"));
        List<Address> geocodeMatches = null;
        try {
            geocodeMatches = geocoder.getFromLocationName("Castellon de la Plana", 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!geocodeMatches.isEmpty()) {
            double lat = geocodeMatches.get(0).getLatitude();
            double lon = geocodeMatches.get(0).getLongitude();
            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Castellon de la Plana"));
        }

        String address1;
        String address2;
        String state;
        String zipcode;
        String country;

        try {
            geocodeMatches = geocoder.getFromLocation(38.8874245, -77.0200729, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!geocodeMatches.isEmpty()){
            address1 = geocodeMatches.get(0).getAddressLine(0);
            address2 = geocodeMatches.get(0).getAddressLine(1);
            state = geocodeMatches.get(0).getAdminArea();
            zipcode = geocodeMatches.get(0).getPostalCode();
            country = geocodeMatches.get(0).getCountryName();
            Log.d("ReverseGeoCoding", address1 == null? "" : address1);
            Log.d("ReverseGeoCoding", address2 == null? "" : address2);
            Log.d("ReverseGeoCoding", state == null? "" : state);
            Log.d("ReverseGeoCoding", zipcode == null? "" : zipcode);
            Log.d("ReverseGeoCoding", country == null? "" : country);

        }

        LatLng muesumCoord = new LatLng(38.8874245, -77.0200729);
        Marker museum = mMap.addMarker(new MarkerOptions()
                .position(muesumCoord)
                .title("Museum")
                .snippet("National Air and Space Museum"));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("My mark"));
            }
        });

    }

    public void setLocationEnabled() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_GPS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setLocationEnabled();
                } else {

                    Dialog d = new AlertDialog.Builder(MapsActivity.this).setTitle("Error").
                            setMessage("I need GPS permission").create();
                    d.show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
