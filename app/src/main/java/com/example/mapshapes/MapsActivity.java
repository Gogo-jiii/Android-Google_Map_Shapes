package com.example.mapshapes;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mapshapes.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private Button btnOpenMap;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private PermissionManager permissionManager;
    private LocationManager locationManager;
    private String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        btnOpenMap = findViewById(R.id.btnOpenMap);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        permissionManager = PermissionManager.getInstance(this);
        locationManager = LocationManager.getInstance(this);

        btnOpenMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!permissionManager.checkPermissions(permissions)) {
                    permissionManager.askPermissions(MapsActivity.this, permissions, 100);
                } else {
                    if (locationManager.isLocationEnabled()) {
                        mapFragment.getMapAsync(MapsActivity.this);
                    } else {
                        locationManager.createLocationRequest();
                    }
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        showPolyLine();
        showPolygon();
        showHollowPolygon();
        showCircle();
    }

    private void showCircle() {
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(27.4, -67.1))
                .radius(1000); // In meters

        Circle circle = mMap.addCircle(circleOptions);

    }

    private void showHollowPolygon() {
        List<LatLng> hole = Arrays.asList(new LatLng(1, 1),
                new LatLng(1, 2),
                new LatLng(2, 2),
                new LatLng(2, 1),
                new LatLng(1, 1));
        Polygon hollowPolygon = mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(0, 0),
                        new LatLng(0, 5),
                        new LatLng(3, 5),
                        new LatLng(3, 0),
                        new LatLng(0, 0))
                .addHole(hole)
                .fillColor(Color.BLUE));

    }

    private void showPolygon() {
        PolygonOptions polygonOptions = new PolygonOptions()
                .add(new LatLng(37.35, -66.0),
                        new LatLng(37.45, -66.0),
                        new LatLng(37.45, -66.2),
                        new LatLng(37.35, -66.2),
                        new LatLng(37.35, -66.0));

        Polygon polygon = mMap.addPolygon(polygonOptions);
    }

    private void showPolyLine() {
        PolylineOptions polylineOptions = new PolylineOptions()
                .add(new LatLng(37.35, -122.0))
                .add(new LatLng(37.45, -122.0))
                .add(new LatLng(37.45, -122.2))
                .add(new LatLng(37.35, -122.2))
                .add(new LatLng(37.35, -122.0));

        Polyline polyline = mMap.addPolyline(polylineOptions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && permissionManager.handlePermissionResult(MapsActivity.this, 100,
                permissions,
                grantResults)) {

            if (locationManager.isLocationEnabled()) {
                mapFragment.getMapAsync(MapsActivity.this);
            } else {
                locationManager.createLocationRequest();
            }
        }
    }
}