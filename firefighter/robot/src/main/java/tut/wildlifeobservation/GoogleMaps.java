package tut.wildlifeobservation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class GoogleMaps extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationClickListener {
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;

            getDeviceLocation();
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    return;
                }
            mMap.setMyLocationEnabled(true);

        }
    }


    private static final String TAG = "GoogleMaps";
    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_CODE = 1234;
    LocationManager locationManager;

    private boolean mlocationPermissionGranted = false;
    private GoogleMap mMap;
    public FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_activity);

        getLocationPermission();
    }

    private void initMaps(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

       mapFragment.getMapAsync(GoogleMaps.this);
    }
    private void getDeviceLocation()
    {
        Log.d(TAG, "getting Device's current location");

       /*  mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
                if(mlocationPermissionGranted){

                   Task location = mFusedLocationProviderClient.getLastLocation();
                    location.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                Log.d(TAG, "onComplete : found the device location");
                                Location currentLocation = (Location) task.getResult();
                                Log.d(TAG, "onComplete : location " + task.getResult());

                              //  moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),15f);

                            }else {
                                Log.d(TAG, "onComplete : failed to find location");
                                Toast.makeText(GoogleMaps.this,"Failed to retrieve GPS Location",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });*/



                    if (mlocationPermissionGranted) {
                        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                        Log.d(TAG, "locationManager : get System services");
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                            return;
                        }}

                            Log.d(TAG, "locationManager : isProviderEnabled NETWORK_PROVIDER : true");
                            moveCamera(new LatLng(-25.5403,28.0969),15f,"TUT Soshanguve");
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 1, new LocationListener() {
                                @Override
                                public void onLocationChanged(Location location) {
                                    double latitude = location.getLatitude();
                                    double longitude = location.getLongitude();

                                    Geocoder geocoder = new Geocoder(getApplicationContext());
                                    try {
                                        List<Address> addressList = geocoder.getFromLocation(latitude,longitude,1);
                                        String markTitle = addressList.get(0).getLocality() + " ,";
                                        markTitle += addressList.get(0).getCountryName();
                                        Log.d(TAG, "moveCamera : Lat : "+latitude+"long : "+location);
                                        moveCamera(new LatLng(latitude,longitude),15f,markTitle);


                                    } catch (IOException e) {
                                        e.printStackTrace();
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
                            });
                         if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                        {
                            Log.d(TAG, "locationManager : isProviderEnabled GPS_PROVIDER: true");
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                                @Override
                                public void onLocationChanged(Location location) {
                                    double latitude = location.getLatitude();
                                    double longitude = location.getLongitude();
                                    LatLng latLng = new LatLng(latitude,longitude);

                                    Geocoder geocoder = new Geocoder(getApplicationContext());
                                    try {
                                        List<Address> addressList = geocoder.getFromLocation(latitude,longitude,1);
                                        String markTitle = addressList.get(0).getLocality() + " ,";
                                        markTitle += addressList.get(0).getCountryName();

                                        Log.d(TAG, "moveCamera : Lat : "+latitude+"long : "+location);
                                        moveCamera(new LatLng(latitude,longitude),15f,markTitle);

                                    } catch (IOException e) {
                                        e.printStackTrace();
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
                            });
                        }


                }



    private void moveCamera (LatLng latlng, float zoom, String title){
        Log.d(TAG, "moveCamera : lat : "+ latlng.latitude + " ,lon : "+ latlng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoom));
        mMap.addMarker(new MarkerOptions().position(latlng).title(title));
    }
    private void getLocationPermission() {
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mlocationPermissionGranted = true;
                initMaps();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_CODE);
            }
        }else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        mlocationPermissionGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mlocationPermissionGranted = false;
                            return;
                        }
                        mlocationPermissionGranted = true;
                        //Initialize google maps
                        initMaps();
                    }
                }

            }
        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }
}


