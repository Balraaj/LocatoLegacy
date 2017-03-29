package com.example.locato;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class FindUserLocationActivity extends AppCompatActivity implements View.OnClickListener
{
    Button submitButton;
    TextView userEmail;
    String getLocationUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user_location);
        submitButton = (Button)findViewById(R.id.emailSubmit);
        userEmail = (TextView)findViewById(R.id.userEmail);
        getLocationUrl = "https://locato.000webhostapp.com/get_location.php";

        submitButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        try
        {
            URL url = new URL(getLocationUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String data_string = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(userEmail.getText().toString(),"UTF-8");

            bufferedWriter.write(data_string);
            bufferedWriter.flush();
            bufferedWriter.close();

            StringBuilder stringBuilder = new StringBuilder();
            String jsonString = bufferedReader.readLine();
            stringBuilder.append(jsonString);

            inputStream.close();
            httpURLConnection.disconnect();

            Toast.makeText(this,jsonString,Toast.LENGTH_LONG).show();

        }
        catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
