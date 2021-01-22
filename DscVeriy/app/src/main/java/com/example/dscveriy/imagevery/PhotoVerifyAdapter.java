package com.example.dscveriy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class PhotoVerifyAdapter extends BaseAdapter {

    public List<PhotoVerifyList> arraylist;
    public Context context;
    public Activity activity;

    public PhotoVerifyAdapter(List<PhotoVerifyList> arraylist, Context context, Activity activity) {
        this.arraylist = arraylist;
        this.context = context;
        this.activity = activity;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.emp_photoverify_listitem, parent, false);

        TextView empno = convertView.findViewById(R.id.empno);
        TextView name = convertView.findViewById(R.id.name);
        TextView photostatus = convertView.findViewById(R.id.photostatus);
        TextView aadharstatus = convertView.findViewById(R.id.aadharstatus);
        TextView photourl = convertView.findViewById(R.id.photourl);
        TextView aadharurl = convertView.findViewById(R.id.aadharurl);
        TextView aaharno = convertView.findViewById(R.id.aadharno);

        empno.setText(arraylist.get(position).getEmpNo());
        name.setText(arraylist.get(position).getName());
        photostatus.setText(arraylist.get(position).getPhotoverify());
        aadharstatus.setText(arraylist.get(position).getAadharVerify());
        photourl.setText(arraylist.get(position).getPhotourl());
        aadharurl.setText(arraylist.get(position).getAadharurl());
        aaharno.setText(arraylist.get(position).getAadharNo());

        if (arraylist.get(position).getPhotoverify().equalsIgnoreCase("false")){
            photostatus.setText("Not Verify");
            photostatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotoVerifyList item = arraylist.get(position);
                    Intent intent = new Intent(v.getContext(), PhotoVerifyPreview.class);
                    intent.putExtra("Photourl",item.photourl);
                    intent.putExtra("Empno",item.EmpNo);
                    activity.startActivity(intent);
                }
            });
        } else {
            photostatus.setText("Verify");
            photostatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotoVerifyList item = arraylist.get(position);
                    Intent intent = new Intent(v.getContext(), PhotoVerifyPreview.class);
                    intent.putExtra("Photourl",item.photourl);
                    intent.putExtra("Photoverify",item.Photoverify);
                    intent.putExtra("Empno",item.EmpNo);
                    activity.startActivity(intent);
                }
            });
        }

        if (arraylist.get(position).getAadharVerify().equalsIgnoreCase("false")){
         //   aadharstatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.wrong, 0, 0, 0);
            aadharstatus.setText("Not Verify");
            aadharstatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotoVerifyList item = arraylist.get(position);
                    Intent intent = new Intent(v.getContext(), AadharVerifyPreview.class);
                    intent.putExtra("Aadharurl",item.aadharurl);
                    intent.putExtra("AadharNo",item.AadharNo);
                    intent.putExtra("Empno",item.EmpNo);
                    activity.startActivity(intent);
                }
            });

        } else {
          //  aadharstatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_24, 0, 0, 0);
            aadharstatus.setText("Verify");
            aadharstatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotoVerifyList item = arraylist.get(position);
                    Intent intent = new Intent(v.getContext(), AadharVerifyPreview.class);
                    intent.putExtra("Aadharurl",item.aadharurl);
                    intent.putExtra("AadharNo",item.AadharNo);
                    intent.putExtra("AadharVerify",item.AadharVerify);
                    intent.putExtra("Empno",item.EmpNo);
                    activity.startActivity(intent);
                }
            });
        }

        return convertView;
    }
}
