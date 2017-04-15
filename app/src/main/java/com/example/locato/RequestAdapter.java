package com.example.locato;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by balraj on 12/4/17.
 */

public class RequestAdapter extends ArrayAdapter
{
    List list = new ArrayList();

    public RequestAdapter(@NonNull Context context, @LayoutRes int resource)
    {
        super(context, resource);
    }

    public void add(String object)
    {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position)
    {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View row;
        row = convertView;
        ViewHolder viewHolder;


        if(row==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.requestview_row,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.requestId = (TextView)row.findViewById(R.id.text_rr_email);
            row.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)row.getTag();
        }

        String id = (String)this.getItem(position);
        viewHolder.requestId.setText(id);

        return row;
    }

    static class ViewHolder
    {
        TextView requestId;
    }
}
