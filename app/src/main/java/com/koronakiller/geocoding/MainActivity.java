package com.koronakiller.geocoding;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private FusedLocationProviderClient client;
    private Location currentLocation;
    private Address a1;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        client = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        Log.d(TAG, "onCreate: getting last location");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) {
                            Log.d(TAG, "onSuccess: latitude" + location.getLatitude());
                            Log.d(TAG, "onSuccess: log" + location.getLongitude());
                            currentLocation = location;
                            Geocoder geocoder = new Geocoder(getApplicationContext(), new Locale("EN", "IN"));
                            List<Address> addresses = null;
                            try {
                                addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                                a1 = addresses.get(0);
                            } catch (IOException e) {
                                Log.e(TAG, "onCreate: exception occures", e);
                            }
                            Log.d(TAG, "onCreate: address is " + a1.getPostalCode());
                            Log.d(TAG, "onCreate: address is " + a1);
                            tv.setText(a1.toString());
                            Log.d(TAG, "onCreate: address is " + a1.getSubAdminArea());
                        } else Log.e(TAG, "onSuccess: ", new Exception("location is null<<<<----"));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                    }
                });

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {
                    Log.d(TAG, "onSuccess: latitude" + location.getLatitude());
                    Log.d(TAG, "onSuccess: log" + location.getLongitude());
                } else Log.e(TAG, "onSuccess: ", new Exception("location is null<<<<----"));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: ", e);
            }
        });
    }
}
