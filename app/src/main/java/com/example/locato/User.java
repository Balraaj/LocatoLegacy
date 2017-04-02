package com.example.locato;

/**
 * Created by Balraj on 4/1/2017.
 */

public class User
{
   private static String name;
   private static String email;
    private static double latitude;
    private static double longitude;


    public static void setName(String name)
    {
        User.name = name;
    }

    public static String getName()
    {
        return User.name;
    }

    public static void setEmail(String email)
    {
        User.email = email;
    }

    public static String getEmail()
    {
        return User.email;
    }

    public static double getLatitude()
    {
        return latitude;
    }

    public static void setLatitude(double latitude)
    {
        User.latitude = latitude;
    }

    public static double getLongitude()
    {
        return longitude;
    }

    public static void setLongitude(double longitude)
    {
        User.longitude = longitude;
    }
}
