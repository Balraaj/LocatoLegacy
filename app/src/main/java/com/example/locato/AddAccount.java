package com.example.locato;

import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Balraj on 3/26/2017.
 */

public class AddAccount extends AsyncTask<String,Void,String>
{
    String myUrl;

    protected void onPreExecute()
    {
        myUrl = "http://locato.net16.net/add_info.php";
    }

    @Override
    protected String doInBackground(String... params)
    {
        String email,name,lat,lng;

        email = params[0];
        name = params[1];
        lat = params[2];
        lng = params[3];


        try
        {
            URL url = new URL(myUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

            String data_string = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
                    URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+
                    URLEncoder.encode("lat","UTF-8")+"="+URLEncoder.encode(lat,"UTF-8")+"&"+
                    URLEncoder.encode("lng","UTF-8")+"="+URLEncoder.encode(lng,"UTF-8");

            bufferedWriter.write(data_string);
            bufferedWriter.flush();
            bufferedWriter.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            inputStream.close();
            httpURLConnection.disconnect();
            return "One row inserted";

        }
        catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

}
