package com.example.locato;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by balraj on 8/4/17.
 */

public class SendRequestActivity extends AppCompatActivity
{
    EditText ID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_send_request);
        setupNavigationDrawer();
        Button submitButton = (Button)findViewById(R.id.btn_sr_submitID);
        ID = (EditText)findViewById(R.id.text_sr_userID);
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Database database = new Database(getApplicationContext());
                database.sendRequest(ID.getText().toString());
                startMainActivity();
            }
        });

    }
    private void setupNavigationDrawer()
    {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout =(DrawerLayout)findViewById(R.id.drawer_sendRequest);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        actionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        final NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view_sendRequest);
        navigationView.setNavigationItemSelectedListener(new NavigationBarItemsHandler(this,navigationView,drawerLayout));

        View headerView = navigationView.getHeaderView(0);

        TextView name = (TextView)headerView.findViewById(R.id.text_navigationHeader_name);
        name.setText(User.getName());
        TextView email = (TextView)headerView.findViewById(R.id.text_navigationHeader_email);
        email.setText(User.getEmail());

    }

    private void startMainActivity()
    {
        startActivity(new Intent(this,MainActivity.class));
    }
}
