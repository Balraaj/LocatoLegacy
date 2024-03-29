package com.example.locato;

import android.content.Context;

/**
 * Created by Balraj on 4/1/2017.
 */

public class Database
{
    Context context = null;
    DatabaseThread databaseThread;

    public Database(Context context)
    {
        this.context = context;
    }

    public void createRecord()
    {
        databaseThread = new DatabaseThread(context);
        databaseThread.execute(DatabaseThread.CREATE_RECORD);
    }

    public void updateLocation()
    {
        databaseThread = new DatabaseThread(context);
        databaseThread.execute(DatabaseThread.UPDATE_LOCATION);
    }

    public void getLocation(String email)
    {
        databaseThread = new DatabaseThread(context);
        databaseThread.execute(DatabaseThread.GET_LOCATION,email);
    }

    public void sendRequest(String friendEmail)
    {
        databaseThread = new DatabaseThread(context);
        databaseThread.execute(DatabaseThread.SEND_REQUEST,friendEmail);
    }

    public void getRequests()
    {
        databaseThread = new DatabaseThread(context);
        databaseThread.execute(DatabaseThread.GET_REQUESTS);
    }

    public void acceptRequest(String email)
    {
        databaseThread = new DatabaseThread(context);
        databaseThread.execute(DatabaseThread.ACCEPT_REQUEST,email);
    }

    public void rejectRequest(String email)
    {
        databaseThread = new DatabaseThread(context);
        databaseThread.execute(DatabaseThread.REJECT_REQUEST,email);
    }

    public void getMembers()
    {
        databaseThread = new DatabaseThread(context);
        databaseThread.execute(DatabaseThread.GET_MEMBERS);
    }
}
