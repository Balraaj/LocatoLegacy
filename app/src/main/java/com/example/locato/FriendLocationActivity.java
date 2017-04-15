package com.example.locato;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FriendLocationActivity extends AppCompatActivity implements OnMapReadyCallback
{
    GoogleMap googleMap = null;

    String latitude ="0.0";
    String longitude = "0.0";
    String locationMessage;

    final static int QUERY_RETURNED_NULL = 0;
    final static int QUERY_RETURNED_VALUE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupNavigationDrawer();
        setupMap();

        Intent intent = getIntent();
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");

        if(latitude.equals("NULL"))
        {
            setLocationMessage(FriendLocationActivity.QUERY_RETURNED_NULL);
        }
        else
        {
            setLocationMessage(FriendLocationActivity.QUERY_RETURNED_VALUE);
        }

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        TextView friendLocation = (TextView)findViewById(R.id.text_m_userLocation);
        TextView gpsStatus = (TextView)findViewById(R.id.text_m_gpsStatus);
        gpsStatus.setText("");
        friendLocation.setText(locationMessage);
    }

    protected void setLocationMessage(int result)
    {
        if(result==FriendLocationActivity.QUERY_RETURNED_NULL)
        {
            locationMessage = "         Sorry!\n This user does not exist";
        }
        else
        {
            locationMessage = " Your friends location\n\n";
            locationMessage +="Latitude   : "+latitude;
            locationMessage +="\nLongitude : "+longitude;
        }
    }

    private void setupNavigationDrawer()
    {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout =(DrawerLayout)findViewById(R.id.drawer_main);
        ActionBarDrawerToggle  actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
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

    private void setupMap()
    {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.googleMap = googleMap;

        if(!latitude.equals("NULL"))
        {
            LatLng latLng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
            googleMap.addMarker(new MarkerOptions().position(latLng));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
        }

    }

}
