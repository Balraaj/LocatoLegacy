package com.example.locato;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

/**
 * Created by balraj on 6/4/17.
 */

public class NavigationBarItemsHandler implements NavigationView.OnNavigationItemSelectedListener
{
    Context context;
    NavigationView navigationView;
    private DrawerLayout drawerLayout;


    public NavigationBarItemsHandler(Context context,NavigationView navigationView,DrawerLayout drawerLayout)
    {
        this.context = context;
        this.navigationView = navigationView;
        this.drawerLayout = drawerLayout;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {

        if(item!=null)
        {
            if (item.getItemId() == R.id.home_id)
            {
                navigationView.setCheckedItem(R.id.home_id);

                if(context.getClass()!=MainActivity.class)
                {
                    context.startActivity(new Intent(context, MainActivity.class));
                }
                else
                {
                    drawerLayout.closeDrawers();
                }
            }
            else if (item.getItemId() == R.id.find_friend_id)
            {
                navigationView.setCheckedItem(R.id.find_friend_id);
                context.startActivity(new Intent(context, FindFriendActivity.class));
            }

            else if(item.getItemId()== R.id.send_request_id)
            {

                navigationView.setCheckedItem(R.id.send_request_id);
                context.startActivity(new Intent(context, SendRequestActivity.class));
            }
            return false;
        }
        return false;
    }
}
