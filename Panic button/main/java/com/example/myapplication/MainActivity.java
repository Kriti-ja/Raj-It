package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Handler mHandler;
    private boolean mIsLongPressed = false;
    private static final int LONG_PRESS_TIME = 5000; // 5 seconds

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button emergencyButton = findViewById(R.id.emergencyButton);
        emergencyButton.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mHandler = new Handler();
                    mHandler.postDelayed(mLongPressed, LONG_PRESS_TIME);
                    Toast.makeText(MainActivity.this, "Long pressed", Toast.LENGTH_SHORT).show();
                    break;
                case MotionEvent.ACTION_UP:
                    if (!mIsLongPressed) {
                        Toast.makeText(this, "Press at least for 5 seconds", Toast.LENGTH_SHORT).show();
                    }

                    mHandler.removeCallbacks(mLongPressed);
                    mIsLongPressed = false;
                    break;
            }
            return true;
        });
    }

    private final Runnable mLongPressed = () -> {
        mIsLongPressed = true;
        // Handle long press
        sendEmergencySMS();
    };

    private void sendEmergencySMS() {
        // Get the GPS location
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                Log.i("INFO",""+latitude);

                // Get the phone number list from the preferences or database
                ArrayList<String> phoneNumberList = new ArrayList<>();
                phoneNumberList.add("9079393837");
                phoneNumberList.add("9079934276");

                // Construct the SMS message
                String message = "Emergency: Help needed at location: https://maps.google.com/maps?q=" + latitude + "," + longitude;

                // Send the SMS to each phone number in the list
        SmsManager smsManager = SmsManager.getDefault();
                for (String phoneNumber : phoneNumberList) {
                    smsManager.sendTextMessage("+919079393837", null, "Help Me", null, null);
                }

        }
    }
