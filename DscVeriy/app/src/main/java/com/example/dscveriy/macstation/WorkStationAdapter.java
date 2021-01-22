package com.example.dscveriy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class WorkStationAdapter extends BaseAdapter {

    public List<WorkStationList> workStationLists;
    public Context context;
    public WorkStation activity;

    public WorkStationAdapter(List<WorkStationList> workStationLists, Context context, Activity activity) {
        this.workStationLists = workStationLists;
        this.context = context;
        this.activity = (WorkStation)activity;
    }

    @Override
    public int getCount() {
        return workStationLists.size();
    }

    @Override
    public Object getItem(int position) {
        return workStationLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        @SuppressLint("ViewHolder")
        View v = View.inflate(context, R.layout.workstation_listitem, null);

        TextView tv_unitname = v.findViewById(R.id.tv_unitname);
        TextView tv_workstation = v.findViewById(R.id.tv_workstation);
        TextView tv_problem = v.findViewById(R.id.tv_problem);
        TextView tv_problemon = v.findViewById(R.id.tv_problemon);
        TextView tv_raisedby = v.findViewById(R.id.tv_raisedby);
        TextView tv_sn = v.findViewById(R.id.tv_sn);
        TextView tv_isresponse = v.findViewById(R.id.tv_isresponse);
        Button btn_workstart = v.findViewById(R.id.btn_workstart);
        Button btn_workcomplete = v.findViewById(R.id.btn_workcomplete);
        Button btn_qcverify = v.findViewById(R.id.btn_qcverify);

        btn_workcomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), WorkCompleteActivity.class);
                intent.putExtra("sno",workStationLists.get(position).getSno());
                activity.startActivity(intent);
            }
        });

        btn_qcverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), QCVerify.class);
                intent.putExtra("sno",workStationLists.get(position).getSno());
                intent.putExtra("IssueTypeInTamil",workStationLists.get(position).getBreakdowntype());
                intent.putExtra("IssueTypeInEnglish",workStationLists.get(position).getBreakdowntype());
                activity.startActivity(intent);
            }
        });

        btn_workstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BarcodeScan.class);
                intent.putExtra("sno",workStationLists.get(position).getSno());
                activity.startActivity(intent);
            }
        });

        final WorkStationList listItem = workStationLists.get(position);

        tv_unitname.setText(workStationLists.get(position).getUnitlineno());
        tv_workstation.setText(workStationLists.get(position).getBkworkstation());
        tv_problem.setText(workStationLists.get(position).getBreakdowntype());
        tv_problemon.setText(workStationLists.get(position).getDate());
        tv_raisedby.setText(workStationLists.get(position).getIssueraisedby());
        tv_sn.setText(workStationLists.get(position).getSno());
        btn_workstart.setText(workStationLists.get(position).getSearch());
        btn_qcverify.setText(workStationLists.get(position).getQcverify());
        btn_workcomplete.setText(workStationLists.get(position).getWorkcompleted());
        tv_isresponse.setText(workStationLists.get(position).getIsresponse());

        if (workStationLists.get(position).getWorkcompleted().contains("Work Completed")){
            btn_workcomplete.setVisibility(View.VISIBLE);
            btn_qcverify.setVisibility(View.INVISIBLE);
            btn_workstart.setVisibility(View.INVISIBLE);
        } else if (workStationLists.get(position).getSearch().contains("Work Starting")){
            btn_workstart.setVisibility(View.VISIBLE);
            btn_workcomplete.setVisibility(View.INVISIBLE);
            btn_qcverify.setVisibility(View.INVISIBLE);
        } else if (workStationLists.get(position).getQcverify().contains("QC Verify")){
            btn_workstart.setVisibility(View.INVISIBLE);
            btn_workcomplete.setVisibility(View.INVISIBLE);
            btn_qcverify.setVisibility(View.VISIBLE);
        }

        return v;
    }
}
