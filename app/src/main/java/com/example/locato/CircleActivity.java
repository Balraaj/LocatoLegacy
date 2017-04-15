package com.example.locato;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CircleActivity extends AppCompatActivity
{
    RequestAdapter requestAdapter;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        setupNavigationDrawer();

        requestAdapter = new RequestAdapter(this,R.layout.requestview_row);
        listview = (ListView)findViewById(R.id.listview_circle);
        listview.setAdapter(requestAdapter);

        requestAdapter.add("Loading...");
        Database database = new Database(this);
        database.getMembers();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(DatabaseThread.MEMBERS_DATA_RECEIVED));
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        unregisterReceiver(receiver);
    }

    private void setupNavigationDrawer()
    {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Select User");
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout =(DrawerLayout)findViewById(R.id.drawer_circle);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        actionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        final NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view_circle);
        navigationView.setNavigationItemSelectedListener(new NavigationBarItemsHandler(this,navigationView,drawerLayout));

        View headerView = navigationView.getHeaderView(0);

        TextView name = (TextView)headerView.findViewById(R.id.text_navigationHeader_name);
        name.setText(User.getName());
        TextView email = (TextView)headerView.findViewById(R.id.text_navigationHeader_email);
        email.setText(User.getEmail());

    }


    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            requestAdapter.clear();
            requestAdapter.list.remove(0);

            String jsonString = intent.getStringExtra("result");

                try
                {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArray = jsonObject.getJSONArray("Members");

                    JSONObject JO = jsonArray.getJSONObject(0);
                    String response = JO.getString("memberEmail");

                    if (response.equals("NULL"))
                    {
                        requestAdapter.add("No Members ");
                    }

                    else
                    {
                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                final String email = (String)requestAdapter.getItem(position);
                                Database database = new Database(getApplicationContext());
                                database.getLocation(email);
                            }
                        });


                        int count = 0;
                        while (count < jsonArray.length())
                        {
                            JSONObject temp = jsonArray.getJSONObject(count);
                            requestAdapter.add(temp.getString("memberEmail"));
                            count++;
                        }
                    }
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

    };

}
