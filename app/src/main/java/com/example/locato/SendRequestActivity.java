package com.example.locato;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by balraj on 8/4/17.
 */

public class SendRequestActivity extends AppCompatActivity
{
    EditText friendEmail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_find_friend);
        Button submitButton = (Button)findViewById(R.id.btn_ff_submitEmail);
        friendEmail = (EditText)findViewById(R.id.text_ff_userEmail);
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Database database = new Database(getApplicationContext());
                database.sendRequest(friendEmail.getText().toString());
            }
        });

    }
}
