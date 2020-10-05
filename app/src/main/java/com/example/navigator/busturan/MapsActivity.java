package com.example.navigator.busturan;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.example.navigator.busturan.MainActivity.ROUTE_KEY;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    String viewableRouteKey;

    //String routeName;
    //double routeSydneyLat, routeSydneyLong;
    private GoogleMap gMap;
    private int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        viewableRouteKey = getIntent().getStringExtra(ROUTE_KEY);

        color = getColor();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        LatLng sydney = new LatLng(43.2303263, 76.8995214);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));

        InitMapElements();
    }

    private void InitMapElements() {
        DatabaseReference fireRef = FirebaseDatabase.getInstance().getReference();

        fireRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                gMap.clear();

                DataSnapshot routeFireObject = dataSnapshot.child("routes").child(viewableRouteKey);

                //routeName = routeFireObject.child("name").getValue(String.class);
                //routeSydneyLat = routeFireObject.child("sydney_lat").getValue(double.class);
                //routeSydneyLong = routeFireObject.child("sydney_long").getValue(double.class);

                //LatLng sydney = new LatLng(routeSydneyLat, routeSydneyLong);
                //gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));

                PolylineOptions polylineOptions = new PolylineOptions().geodesic(true)
                        .color(color).width(18);

                for (DataSnapshot polylineFireID : routeFireObject.child("polylines").getChildren()) {
                    LatLng latlng = new LatLng(
                            polylineFireID.child("latitude").getValue(Double.class),
                            polylineFireID.child("longtitude").getValue(Double.class)
                    );
                    polylineOptions.add(latlng);
                }
                gMap.addPolyline(polylineOptions);

                DataSnapshot markersFireObject = dataSnapshot.child("markers");
                for (DataSnapshot markerFireID : routeFireObject.child("markers").getChildren()) {
                    String marker = markerFireID.getValue(String.class);

                    Double markerLat = markersFireObject.child(marker)
                            .child("latitude").getValue(Double.class);
                    Double markerLong = markersFireObject.child(marker)
                            .child("longtitude").getValue(Double.class);

                    String markerBusName = markersFireObject.child(marker)
                            .child("bus").getValue(String.class);

                    gMap.addMarker(new MarkerOptions()
                            .position(new LatLng(
                                    markerLat,
                                    markerLong))
                            .title(markerBusName));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("TAG", error.getMessage());
            }
        });
    }

    // Method (abilities: things the object can do)
    public int getColor() {

        String[] mColors = {
                "#39add1", // light blue
                "#3079ab", // dark blue
                "#c25975", // mauve
                "#e15258", // red
                "#f9845b", // orange
                "#838cc7", // lavender
                "#7d669e", // purple
                "#53bbb4", // aqua
                "#51b46d", // green
                "#e0ab18", // mustard
                "#637a91", // dark gray
                "#f092b0", // pink
                "#b7c0c7"  // light gray
        };

        Random randomGenerator = new Random(); // Construct a new Random number generator
        int randomNumber = randomGenerator.nextInt(mColors.length);
        return Color.parseColor(mColors[randomNumber]);

    }
}
