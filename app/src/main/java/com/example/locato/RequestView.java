package com.example.locato;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RequestView extends AppCompatActivity
{

    RequestAdapter requestAdapter;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_view);
        setupNavigationDrawer();

        requestAdapter = new RequestAdapter(this,R.layout.requestview_row);
        listview = (ListView)findViewById(R.id.listview_requestView);
        listview.setAdapter(requestAdapter);

        Intent intent = getIntent();
        String jsonString = intent.getStringExtra("result");

        if(jsonString!=null)
        {
            try
            {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("Requests");

                JSONObject JO = jsonArray.getJSONObject(0);
                String response = JO.getString("requestFrom");

                if (response.equals("NULL"))
                {
                    requestAdapter.add("No New Requests");
                }

                else
                {
                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                        {
                            final String email = (String)requestAdapter.getItem(position);
                            delegate(email);
                        }
                    });

                    int count = 0;
                    while (count < jsonArray.length())
                    {
                        JSONObject temp = jsonArray.getJSONObject(count);
                        requestAdapter.add(temp.getString("requestFrom"));
                        count++;
                    }
                }
            }

            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }

        else
        {
            requestAdapter.add("Loading...");
            Database database = new Database(this);
            database.getRequests();
        }

    }

    private void setupNavigationDrawer()
    {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout =(DrawerLayout)findViewById(R.id.drawer_requestView);
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

    private void delegate(String email)
    {
        Intent intent = new Intent(this,ProcessRequestActivity.class);
        intent.putExtra("email",email);
        startActivity(intent);
    }

}
