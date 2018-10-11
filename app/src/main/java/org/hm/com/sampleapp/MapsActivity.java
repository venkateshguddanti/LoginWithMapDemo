package org.hm.com.sampleapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,LocationListener {

    private GoogleMap mMap;
    private Button next;
    private Boolean mLocationPermissionGranted = false;
    int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100;
    SupportMapFragment mapFragment;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    private Location currentLoc;
    String addressTitle="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        next = (Button)findViewById(R.id.buttonNext);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        getLocationPermission();


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MapsActivity.this,RegistrationScreen.class));
            }
        });
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

        locationUpdates();
        // Add a marker in Sydney and move the camera
      /*  LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    private void locationUpdates() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mMap.clear();
                List<Address> addresses;
                try {
                    Geocoder geocoder = new Geocoder(MapsActivity.this,Locale.getDefault());
                    addresses = geocoder.getFromLocation(currentLoc.getLatitude(), currentLoc.getLongitude(), 1); //1 num of possible location returned
                    String address = addresses.get(0).getAddressLine(0); //0 to obtain first possible address
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    //create your custom title
                     addressTitle = address +"/n"+city+"/n"+state;
                    MarkerOptions options = new MarkerOptions();
                    options.position(new LatLng(currentLoc.getLatitude(),currentLoc.getLongitude()));
                    options.title(addressTitle);
                    //marker.setTitle(addressTitle);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            mapFragment.getMapAsync(this);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    mapFragment.getMapAsync(this);
                }else
                {
                    Toast.makeText(this, "Please allow Permissions to access map", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        currentLoc = location;
        if (mMap == null) {
            return;
        }
        if (mLocationPermissionGranted) {
            mMap.clear();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            MarkerOptions options = new MarkerOptions();
            options.position(new LatLng(location.getLatitude(),location.getLongitude()));
            options.title(addressTitle);
            mMap.addMarker(options).showInfoWindow();

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(currentLoc.getLatitude(),
                            currentLoc.getLongitude()), 18f));

        } else {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            getLocationPermission();
        }




    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
