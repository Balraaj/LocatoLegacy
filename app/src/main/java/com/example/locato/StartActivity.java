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

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_pref),Context.MODE_PRIVATE);
        boolean chkSignIn = sharedPreferences.getBoolean(getString(R.string.sign_in_status),false);

        if(chkSignIn)
        {
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
}
