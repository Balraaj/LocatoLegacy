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
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback
{
    MainActivity thisActivity;
    GoogleMap googleMap;

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
            LatLng latLng = new LatLng(User.getLatitude(),User.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(latLng));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));

            Database database = new Database(thisActivity);
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
        thisActivity = this;
        setContentView(R.layout.activity_main);
        setupNavigationDrawer();
        setupMap();
        setPermissionsGranted();

        if (!permissionsGranted)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
        }
    }

    private void setupNavigationDrawer()
    {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout =(DrawerLayout)findViewById(R.id.drawer_main);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        actionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        final NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view_requestView);
        navigationView.setNavigationItemSelectedListener(new NavigationBarItemsHandler(this,navigationView,drawerLayout));

        View headerView = navigationView.getHeaderView(0);

        TextView name = (TextView)headerView.findViewById(R.id.text_navigationHeader_name);
        name.setText(User.getName());
        TextView email = (TextView)headerView.findViewById(R.id.text_navigationHeader_email);
        email.setText(User.getEmail());

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        checkAndRequestGps();
        updateGUI();
        startMyService();
        registerReceiver(receiver, new IntentFilter(MyService.LOCATION_UPDATED));
        registerReceiver(gpsReceiver,new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
    }

    protected void checkAndRequestGps()
    {
        setGpsStatus();
        if((!gpsRequested)&&(!gpsStatus))
        {
            requestGps();
            gpsRequested = true;
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        unregisterReceiver(receiver);
        unregisterReceiver(gpsReceiver);
    }

    private void requestGps()
    {
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
                        checkAndRequestGps();
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
                                    thisActivity.finish();
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.googleMap = googleMap;
    }

    private void setupMap()
    {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
}
