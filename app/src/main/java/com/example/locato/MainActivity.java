package com.example.locato;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    Location deviceLocation;
    MainActivity m1;

    final int MY_PERMISSION_REQUEST_LOCATION = 1;

    boolean gpsEnabled = false;
    boolean gpsRequested = false;
    boolean serviceRunning = false;
    boolean permissionsGranted = false;


    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            deviceLocation = intent.getParcelableExtra("location");
            updateGUI();
        }
    };

    private BroadcastReceiver gpsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().matches(LocationManager.PROVIDERS_CHANGED_ACTION))
            {
                if(!gpsEnabled)
                {
                    gpsEnabled = true;
                    startMyService();
                }
                else
                {
                    gpsEnabled = false;
                    updateGUI();
                }
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setGpsStatus();
        setHasPermissions();
        setContentView(R.layout.activity_main);
        m1 = this;



        if (!permissionsGranted)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION);
        }
    }

    public void requestGps()
    {
        if(!gpsRequested)
        {
            gpsRequested = true;
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("This app requires location services to work. please turn on Location services on next screen")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void setGpsStatus()
    {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
        gpsEnabled =  manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void setHasPermissions()
    {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            permissionsGranted = true;
        }
        else
        {
            permissionsGranted = false;
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(!gpsEnabled)
        {
            requestGps();
        }
        updateGUI();
        startMyService();
        registerReceiver(receiver, new IntentFilter(MyService.LOCATION_UPDATED));
        registerReceiver(gpsReceiver,new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        unregisterReceiver(receiver);
    }

    protected synchronized void  updateGUI()
    {
        //Toast.makeText(this, "updateGUI called", Toast.LENGTH_SHORT).show();
        String msg;
        double latitude = 0.0;
        double longitude = 0.0;

        if (deviceLocation != null)
        {
            latitude = deviceLocation.getLatitude();
            longitude = deviceLocation.getLongitude();
        }

        msg = "Latitude    : " + latitude;
        msg += "\nLongitude : " + longitude;
        TextView tv = (TextView) findViewById(R.id.text_box);
        TextView gpstv = (TextView)findViewById(R.id.gps_Status);
        tv.setText(msg);
        if(gpsEnabled)
        {
            gpstv.setText("GPS : ENABLED");
        }
        else
        {
            gpstv.setText("GPS : DISABLED");
        }

    }

    public void myfunc(View view)
    {
        if (deviceLocation != null)
        {
            Intent myIntent = new Intent(this, MapsActivity.class);
            myIntent.putExtra("location", deviceLocation);
            this.startActivity(myIntent);
        }
    }

    /*    public void updateLocation()
        {
            Toast.makeText(this,"updateLocation is called",Toast.LENGTH_SHORT).show();
            deviceLocation = locationResolver.deviceLocation;
            updateGUI();
        }
    */
    public void startMyService()
    {
        if((!serviceRunning)&&(gpsEnabled)&&(permissionsGranted))
        {
            Intent myIntent = new Intent(this, MyService.class);
            serviceRunning = true;
            startService(myIntent);
        }
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSION_REQUEST_LOCATION:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        setGpsStatus();
                        permissionsGranted = true;

                        if(!gpsEnabled)
                        {
                            requestGps();
                        }
                        startMyService();
                    }
                }

                else
                {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("This app requires location permission to work correctly")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    m1.finish();
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        }
    }
}
