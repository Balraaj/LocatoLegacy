package com.example.locato;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FriendLocationActivity extends AppCompatActivity
{
    String latitude;
    String longitude;

    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_location);

        Intent intent = getIntent();
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");

        if(latitude.equals("NULL"))
        {
            setErrorMessage();
            Button showOnMap = (Button)findViewById(R.id.btn_fl_showOnMap);
            showOnMap.setEnabled(false);
        }
        else
        {
            setMessage();
        }

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        TextView friendLocation = (TextView)findViewById(R.id.text_fl_friendLocation);
        friendLocation.setText(message);
    }

    public void showOnMap(View view)
    {
        Intent intent = new Intent(this,MapsActivity.class);
        intent.putExtra("latitude",Double.parseDouble(latitude));
        intent.putExtra("longitude",Double.parseDouble(longitude));

        startActivity(intent);
    }

    protected void setMessage()
    {
        message = " Your friends location\n\n";
        message+="Latitude : "+latitude;
        message+="\nLongitude : "+longitude;
    }

    protected void setErrorMessage()
    {
        message = "Sorry, This user does not exist";
    }

}
