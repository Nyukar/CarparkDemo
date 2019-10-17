package com.example.carparkdemo;

import android.os.Bundle;
import android.util.Log;


import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.MapStyleOptions;

import java.util.ArrayList;

//import android.support.v4.app.FragmentActivity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest mLocationRequest;
    private ArrayList<LatLng> points;

    private static final float SMALLEST_DISPLACEMENT = 0.5F;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //init location//
      //  createLocationRequest();
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

        boolean success = googleMap.setMapStyle(new MapStyleOptions(getResources()
                .getString(R.string.style_json)));

        if (!success) {
            String TAG = "MapsActivity";
            Log.e(TAG, "Style parsing failed.");
        }

        // Add a marker in carpark 2 and move the camera
        LatLng cp2 = new LatLng(-37.722441, 145.045720);


        mMap.addMarker(new MarkerOptions().position(cp2).title("Marker in Carpark 2"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(cp2));
       // mMap.animateCamera(CameraUpdateFactory.zoomIn());
         mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cp2, 18.5f), 2000, null);
        //CameraUpdateFactory.newLatLngZoom(cp2, 18.55f);
    }

   /* protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setSmallestDisplacement(SMALLEST_DISPLACEMENT);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, this.getMainLooper());

    }

    */
}
