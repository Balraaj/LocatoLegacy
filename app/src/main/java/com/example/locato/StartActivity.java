package com.example.locato;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("myfile",Context.MODE_PRIVATE);
        boolean chkSignIn = sharedPreferences.getBoolean(getString(R.string.sign_in_status),false);

        if(chkSignIn)
        {
            loadUserProfile();
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }

        else
        {
            Intent intent = new Intent(this,SignInActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void loadUserProfile()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("myfile",Context.MODE_PRIVATE);

        User.setName(sharedPreferences.getString(getString(R.string.user_name),""));
        User.setEmail(sharedPreferences.getString(getString(R.string.user_email),""));
        User.setLatitude(0.0);
        User.setLongitude(0.0);
    }
}
