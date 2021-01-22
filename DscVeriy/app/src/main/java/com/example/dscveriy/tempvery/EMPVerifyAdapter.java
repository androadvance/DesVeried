package com.example.dscveriy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EMPVerifyAdapter extends BaseAdapter {

    private static final String NAME_SPACE = "";
    public List<EMPVerifyList> arraylist;
    public Context context;
    ArrayList<String> status_arrayList;
    ArrayAdapter<String> status_adapters;
    public Activity activity;
    String str_status;

    public EMPVerifyAdapter(List<EMPVerifyList> arraylist, Context context, Activity activity) {
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

        convertView = LayoutInflater.from(context).inflate(R.layout.emp_verify_listitem, parent, false);

        final TextView machinename = convertView.findViewById(R.id.machinename);
        final Spinner status = convertView.findViewById(R.id.status);
        TextView update = convertView.findViewById(R.id.update);
        TextView checkdate = convertView.findViewById(R.id.checkdate);
        TextView ip = convertView.findViewById(R.id.ip);
        TextView user = convertView.findViewById(R.id.user);

        machinename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String machineip = arraylist.get(position).getMachineip();

                Toast.makeText(context, machineip, Toast.LENGTH_SHORT).show();
            }
        });

        checkdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userverified = arraylist.get(position).getUserverifieddate();

                Toast.makeText(context, userverified, Toast.LENGTH_SHORT).show();
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EMPVerifyList item = arraylist.get(position);
                if (status.getSelectedItem().toString().equalsIgnoreCase("ok")) {
                    String[] result = {item.getEmpcode(), item.getMachinename(), item.getUnitname(),
                            status.getSelectedItem().toString(), LoginActivity.username, "", null};
                    (new I_VerifyPunchTemplate()).execute(result);

                } else if (status.getSelectedItem().toString().equalsIgnoreCase("Select Status")) {


                } else {
                    Intent intent = new Intent(v.getContext(), ImageUpload.class);
                    intent.putExtra("empcode", item.getEmpcode());
                    intent.putExtra("machineid", item.getMachinename());
                    intent.putExtra("unitname", item.getUnitname());
                    intent.putExtra("status", status.getSelectedItem().toString());
                    intent.putExtra("username", LoginActivity.username);
                    activity.startActivity(intent);
                }
            }
        });

        final EMPVerifyList listItem = arraylist.get(position);

        machinename.setText(arraylist.get(position).getMachinename());
        update.setText(arraylist.get(position).getUpdate());
        checkdate.setText(arraylist.get(position).getLastCheckdate());
        user.setText(arraylist.get(position).getUserverifieddate());
        ip.setText(arraylist.get(position).getMachineip());

        status_arrayList = new ArrayList<String>();
        status_arrayList.add("Select Status");
        status_arrayList.add("Ok");
        status_arrayList.add("Not Ok");
        status_adapters = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, status_arrayList);
        status.setAdapter(status_adapters);

        str_status = arraylist.get(position).getStatus();
        status.setSelection(((ArrayAdapter<String>) status.getAdapter()).getPosition(str_status));

        status.setTag(position);

        if (arraylist.get(position).getLastCheckdate().equalsIgnoreCase("null")) {

            checkdate.setText("-");
        }

        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                {
                    Spinner parentRow = (Spinner) view.getParent();
                    int row = 0;
                    try {
                        String s = parentRow.getTag().toString();
                        row = Integer.parseInt(s);
                    } catch (Exception er) {
                        er.printStackTrace();
                    }
                    String selectedItem = parentRow.getSelectedItem().toString();

                    if (!selectedItem.equalsIgnoreCase("select Status")) {
                        EmployeeTemplateVerification.listitem.get(row).Status = selectedItem;

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return convertView;
    }


    public class I_VerifyPunchTemplate extends AsyncTask<String, String, String> {

        SweetAlertDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE);
            p.setTitleText("Loading, Please Wait...");
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String[] paras = {"empcode", "MachineID", "Unitname", "VerifyStatus", "VerifiedUser", "VerifyRemarks", "verifyfile"};
            String[] values = new String[]{params[0], params[1], params[2], params[3], params[4], params[5], null};
            String methodname = "I_VerifyPunchTemplate";
            String URL = "";
            return Helper.WebServiceCall(paras, values, methodname, NAME_SPACE, URL);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            p.dismiss();
            try {
                JSONArray jsonArray1 = new JSONArray(result);
                if (jsonArray1.getJSONObject(0).getString("col1").equalsIgnoreCase("warning")) {
                    Helper.dialog_warning(activity, "Warning", jsonArray1.getJSONObject(0).getString("col2"), "Ok");
                } else if (jsonArray1.getJSONObject(0).getString("col1").equalsIgnoreCase("success")) {
                    Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show();

                } else {
                    Helper.dialog_warning(activity, "Warning", "Verification failed", "Ok");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}



