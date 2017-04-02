package com.example.locato;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FindFriendActivity extends AppCompatActivity implements View.OnClickListener
{
    EditText userEmail =null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_find_friend);
        Button submitButton = (Button)findViewById(R.id.btn_ff_submitEmail);
        userEmail = (EditText)findViewById(R.id.text_ff_userEmail);
        submitButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        String email = userEmail.getText().toString();
        Database database = new Database(this);
        database.getLocation(email);
    }
}
