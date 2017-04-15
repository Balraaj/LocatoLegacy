package com.example.locato;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProcessRequestActivity extends AppCompatActivity
{
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_request);
        setupNavigationDrawer();

        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        TextView requestEmail = (TextView)findViewById(R.id.text_pr_email);
        requestEmail.setText(email);

        Button btnAccept = (Button)findViewById(R.id.btn_pr_accept);
        btnAccept.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                accept();
            }
        });

        Button btnReject = (Button)findViewById(R.id.btn_pr_reject);
        btnReject.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                reject();
            }
        });
    }

    private void setupNavigationDrawer()
    {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout =(DrawerLayout)findViewById(R.id.drawer_processRequest);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        actionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        final NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view_processRequest);
        navigationView.setNavigationItemSelectedListener(new NavigationBarItemsHandler(this,navigationView,drawerLayout));

        View headerView = navigationView.getHeaderView(0);

        TextView name = (TextView)headerView.findViewById(R.id.text_navigationHeader_name);
        name.setText(User.getName());
        TextView email = (TextView)headerView.findViewById(R.id.text_navigationHeader_email);
        email.setText(User.getEmail());
    }


    private void accept()
    {
        Database database = new Database(this);
        database.acceptRequest(email);
        Toast.makeText(this,"Request Accepted",Toast.LENGTH_LONG).show();
        startActivity(new Intent(this,MainActivity.class));
    }

    private void reject()
    {
        Database database = new Database(this);
        database.rejectRequest(email);
        Toast.makeText(this,"Request Rejected",Toast.LENGTH_LONG).show();
        startActivity(new Intent(this,MainActivity.class));
    }
}
