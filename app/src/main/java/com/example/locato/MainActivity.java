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
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    MainActivity mainActivity;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

    final int LOCATION_PERMISSION_REQUEST = 1;

    boolean gpsStatus = false;
    boolean gpsRequested = false;
    boolean permissionsGranted = false;
    boolean locationSet = false;

    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Location location = intent.getParcelableExtra("location");
            locationSet = true;

            User.setLatitude(location.getLatitude());
            User.setLongitude(location.getLongitude());

            Database database = new Database(mainActivity);
            database.updateLocation();

            updateGUI();
        }
    };

    private BroadcastReceiver gpsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().matches(LocationManager.PROVIDERS_CHANGED_ACTION))
            {
                setGpsStatus();
                updateGUI();
                startMyService();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        drawerLayout =(DrawerLayout)findViewById(R.id.drawer_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        setSupportActionBar(toolbar);



        setGpsStatus();
        setPermissionsGranted();
        mainActivity = this;

        if (!permissionsGranted)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
        }
    }

    @Override

    protected void onPostCreate(Bundle bundle)
    {
        super.onPostCreate(bundle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setGpsStatus();
        if(!gpsStatus)
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
        unregisterReceiver(gpsReceiver);
    }

    public void requestGps()
    {
        if(!gpsRequested)
        {
            gpsRequested = true;
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("This app requires your location to work. please turn on Location services on next screen")
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
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsStatus =  manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void setPermissionsGranted()
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

    protected synchronized void  updateGUI()
    {
        if(locationSet)
        {
            String msg = "       Your Location : \n\n";
            msg += "Latitude    : " + User.getLatitude();
            msg += "\nLongitude : " + User.getLongitude();
            TextView tv = (TextView) findViewById(R.id.text_m_userLocation);
            tv.setText(msg);
        }

        TextView textGpsStatus = (TextView) findViewById(R.id.text_m_gpsStatus);

        if(gpsStatus)
        {
            textGpsStatus.setVisibility(View.INVISIBLE);
        }
        else
        {
            textGpsStatus.setVisibility(View.VISIBLE);
            textGpsStatus.setText("GPS IS DISABLED");
        }


    }

    public void showOnMap(View view)
    {
        if (locationSet)
        {
            Intent myIntent = new Intent(this, MapsActivity.class);
            myIntent.putExtra("latitude",User.getLatitude());
            myIntent.putExtra("longitude",User.getLongitude());
            this.startActivity(myIntent);
        }
    }
    public void startMyService()
    {
        if(permissionsGranted)
        {
            Intent myIntent = new Intent(this, MyService.class);
            startService(myIntent);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case LOCATION_PERMISSION_REQUEST:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        permissionsGranted = true;
                        setGpsStatus();

                        if(!gpsStatus)
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
                                    mainActivity.finish();
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        }
    }

    @Override
    public void onClick(View v)
    {
        Intent myIntent = new Intent(this,FindFriendActivity.class);
        this.startActivity(myIntent);
    }

}
