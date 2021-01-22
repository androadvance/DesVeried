package com.example.dscveriy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;


import java.util.List;

public class PhotoVerifyAdapterHeader extends BaseAdapter {

    public List<PhotoVerifyListHeader> arraylist;
    public Context context;

    public PhotoVerifyAdapterHeader(List<PhotoVerifyListHeader> arraylist, Context context) {
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

        convertView = LayoutInflater.from(context).inflate(R.layout.emp_photoverify_listitem_header, parent, false);

        final TextView empno = convertView.findViewById(R.id.empno);
        TextView name = convertView.findViewById(R.id.name);
        TextView photoverify = convertView.findViewById(R.id.photoverify);
        TextView adharverify = convertView.findViewById(R.id.aadharverify);

        return convertView;
    }
}
