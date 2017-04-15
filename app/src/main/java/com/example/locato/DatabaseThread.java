package com.example.locato;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

import static android.provider.LiveFolders.INTENT;

/**
 * Created by Balraj on 4/1/2017.
 */

public class DatabaseThread extends AsyncTask<String,Void,String>
{
    static final String CREATE_RECORD = "1";
    static final String UPDATE_LOCATION = "2";
    static final String GET_LOCATION = "3";
    static final String SEND_REQUEST = "4";
    static final String GET_REQUESTS = "5";
    static final String ACCEPT_REQUEST = "6";
    static final String GET_MEMBERS = "7";
    static final String REJECT_REQUEST = "8";


    static final String MEMBERS_DATA_RECEIVED = "20";
    static final String REQUESTS_DATA_RECEIVED = "21";

    Context context = null;
    String operationType = null;

    public DatabaseThread(Context context)
    {
        this.context = context.getApplicationContext();
    }

    void createRecord()
    {
        String serverUrl = "http://locato.net16.net/add_info.php";

        try
        {
            URL url = new URL(serverUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String data_string;

            data_string = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(User.getEmail(), "UTF-8") + "&" +
                    URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(User.getName(), "UTF-8") + "&" +
                    URLEncoder.encode("lat", "UTF-8") + "=" + URLEncoder.encode("0.0", "UTF-8") + "&" +
                    URLEncoder.encode("lng", "UTF-8") + "=" + URLEncoder.encode("0.0", "UTF-8");


            bufferedWriter.write(data_string);
            bufferedWriter.flush();
            bufferedWriter.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            inputStream.close();

            httpURLConnection.disconnect();

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

    void updateLocation()
    {
        String serverUrl = "http://locato.net16.net/update_info.php";

        try
        {
            URL url = new URL(serverUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String data_string = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(User.getEmail(),"UTF-8")+"&"+
                    URLEncoder.encode("lat","UTF-8")+"="+URLEncoder.encode(String.valueOf(User.getLatitude()),"UTF-8")+"&"+
                    URLEncoder.encode("lng","UTF-8")+"="+URLEncoder.encode(String.valueOf(User.getLongitude()),"UTF-8");

            bufferedWriter.write(data_string);
            bufferedWriter.flush();
            bufferedWriter.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            inputStream.close();
            httpURLConnection.disconnect();
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

    String getLocation(String email)
    {
        String serverUrl = "https://locato.000webhostapp.com/get_location.php";
        String result = null;

        try
        {
            String jsonString;

            URL url = new URL(serverUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String data_string = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8");

            bufferedWriter.write(data_string);
            bufferedWriter.flush();
            bufferedWriter.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder stringBuilder = new StringBuilder();

            while((jsonString = bufferedReader.readLine())!=null)
            {
                stringBuilder.append(jsonString);
            }

            inputStream.close();
            bufferedReader.close();
            httpURLConnection.disconnect();
            result = stringBuilder.toString();
        }
        catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return result;
    }

    void sendRequest(String friendEmail)
    {

        String serverUrl = "http://locato.net16.net/send_request.php";

        try
        {
            URL url = new URL(serverUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String data_string;

            data_string = URLEncoder.encode("userEmail", "UTF-8") + "=" + URLEncoder.encode(User.getEmail(), "UTF-8") + "&" +
                    URLEncoder.encode("friendEmail", "UTF-8") + "=" + URLEncoder.encode(friendEmail, "UTF-8");


            bufferedWriter.write(data_string);
            bufferedWriter.flush();
            bufferedWriter.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            inputStream.close();

            httpURLConnection.disconnect();

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

    String getRequests()
    {
        String serverUrl = "https://locato.000webhostapp.com/get_requests.php";
        String result = null;

        try
        {
            String jsonString;

            URL url = new URL(serverUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String data_string = URLEncoder.encode("circleOwner","UTF-8")+"="+URLEncoder.encode(User.getEmail(),"UTF-8");

            bufferedWriter.write(data_string);
            bufferedWriter.flush();
            bufferedWriter.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder stringBuilder = new StringBuilder();

            while((jsonString = bufferedReader.readLine())!=null)
            {
                stringBuilder.append(jsonString);
            }

            inputStream.close();
            bufferedReader.close();
            httpURLConnection.disconnect();
            result = stringBuilder.toString();
        }
        catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return result;
    }

    void acceptRequest(String email)
    {

        String serverUrl = "http://locato.net16.net/accept_request.php";

        try
        {
            URL url = new URL(serverUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String data_string;

            data_string = URLEncoder.encode("circleOwner", "UTF-8") + "=" + URLEncoder.encode(User.getEmail(), "UTF-8") + "&" +
                    URLEncoder.encode("memberEmail", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");


            bufferedWriter.write(data_string);
            bufferedWriter.flush();
            bufferedWriter.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            inputStream.close();

            httpURLConnection.disconnect();

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

    void rejectRequest(String email)
    {

        String serverUrl = "http://locato.net16.net/reject_request.php";

        try
        {
            URL url = new URL(serverUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String data_string;

            data_string = URLEncoder.encode("circleOwner", "UTF-8") + "=" + URLEncoder.encode(User.getEmail(), "UTF-8") + "&" +
                    URLEncoder.encode("memberEmail", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");


            bufferedWriter.write(data_string);
            bufferedWriter.flush();
            bufferedWriter.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            inputStream.close();

            httpURLConnection.disconnect();

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

    String getMembers()
    {

        String serverUrl = "https://locato.000webhostapp.com/get_members.php";
        String result = null;

        try
        {
            String jsonString;

            URL url = new URL(serverUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String data_string = URLEncoder.encode("circleOwner","UTF-8")+"="+URLEncoder.encode(User.getEmail(),"UTF-8");

            bufferedWriter.write(data_string);
            bufferedWriter.flush();
            bufferedWriter.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder stringBuilder = new StringBuilder();

            while((jsonString = bufferedReader.readLine())!=null)
            {
                stringBuilder.append(jsonString);
            }

            inputStream.close();
            bufferedReader.close();
            httpURLConnection.disconnect();
            result = stringBuilder.toString();
        }
        catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected String doInBackground(String... params)
    {
        operationType = params[0];

        if(operationType==DatabaseThread.CREATE_RECORD)
        {
            createRecord();
        }
        else if(operationType==DatabaseThread.UPDATE_LOCATION)
        {
            updateLocation();
        }
        else if(operationType==DatabaseThread.GET_LOCATION)
        {
            String email = params[1];
            return getLocation(email);
        }
        else if(operationType==DatabaseThread.SEND_REQUEST)
        {
            String friendEmail = params[1];
            sendRequest(friendEmail);
        }
        else if(operationType==DatabaseThread.GET_REQUESTS)
        {
            return getRequests();
        }
        else if(operationType==DatabaseThread.ACCEPT_REQUEST)
        {
            String email = params[1];
            acceptRequest(email);
        }
        else if(operationType==DatabaseThread.REJECT_REQUEST)
        {
            String email = params[1];
            rejectRequest(email);
        }
        else if(operationType==DatabaseThread.GET_MEMBERS)
        {
            return getMembers();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result)
    {
        if(operationType==DatabaseThread.GET_LOCATION)
        {
            try
            {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("Location");

                JSONObject JO = jsonArray.getJSONObject(0);

                String latitude = JO.getString("Lat");
                String longitude = JO.getString("Lng");

                Intent intent = new Intent(context,FriendLocationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                context.startActivity(intent);
            }

            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        else if(operationType==DatabaseThread.GET_REQUESTS)
        {
                Intent intent = new Intent(context,RequestView.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("result",result);
                context.startActivity(intent);
        }

        else if(operationType==DatabaseThread.GET_MEMBERS)
        {
            Intent myIntent = new Intent();
            myIntent.setAction(MEMBERS_DATA_RECEIVED);
            myIntent.putExtra("result",result);
            context.sendBroadcast(myIntent);
        }
    }
}


