package com.example.dscveriy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;


import java.util.List;

public class HeaderAdapter extends BaseAdapter {

    public List<ListHeader> arraylist;
    public Context context;

    public HeaderAdapter(List<ListHeader> arraylist, Context context) {
        this.arraylist = arraylist;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.templateverify_header, parent, false);

        final TextView machinename = convertView.findViewById(R.id.machinename);
        final TextView status = convertView.findViewById(R.id.status);
        TextView update = convertView.findViewById(R.id.update);
        TextView checkdate = convertView.findViewById(R.id.checkdate);

        return convertView;
    }
}
