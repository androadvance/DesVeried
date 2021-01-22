package com.example.dscveriy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class helpp {


    private static final String NAMESPACE = "";
    private static String URL;
    public static String[] iplist;

    public static void getListViewSize(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += listItem.getMeasuredHeight();

        }
        // if(totalHeight<40)
        //   totalHeight=50;
        //setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
        // print height of adapter on log
        Log.i("height of listItem:", String.valueOf(totalHeight));
    }


    public static String getip(Context context, int index) {
        String text = null;
        if (LoginActivity.shared.getString("ip", "").equals("")) {
            try {
                text = "";
                iplist = text.split("/");
                text = iplist[index];
                // Finally stick the string into the text view.

            } catch (Exception e) {
                // Should never happen!
                throw new RuntimeException(e);
            }
        } else {
            try {
                text = LoginActivity.shared.getString("ip", "");
            } catch (Exception e) {
                // Should never happen!
                throw new RuntimeException(e);
            }
        }
        return text;
    }

    public static String whatsnew(Context cn) {

        InputStream is = null;
        try {
            is = cn.getApplicationContext().getAssets().open("whatnew.txt");    // We guarantee that the available method returns the total
            // size of the asset...  of course, this does mean that a single
            // asset can't be more than 2 gigs.
            int size = is.available();
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            // Convert the buffer into a string.
            String txt = new String(buffer);
            return txt;
        } catch (IOException e) {

            e.printStackTrace();
            return "";
        }
    }

    public static String CheckRights(String Username, String Module, String Submodule, Context cntxt, Activity act, String ip) {
        URL = "";

        SoapObject request = new SoapObject(NAMESPACE, "GetViewPermission");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        request.addProperty("username", Username);
        request.addProperty("module", Module);
        request.addProperty("submodule", Submodule);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            androidHttpTransport.call(NAMESPACE + "GetViewPermission",
                    envelope);
        } catch (IOException | XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SoapPrimitive response = null;

        try {
            response = (SoapPrimitive) envelope.getResponse();
        } catch (SoapFault e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Msg(e.toString(), Toast.LENGTH_LONG, cntxt);
        }

        if (response.toString().equalsIgnoreCase("true")) {
            return "true";
        } else {
            Toast.makeText(act, "Access Denied For " + Submodule, Toast.LENGTH_SHORT).show();
            //    warning("Warning", "Access Denied For " + Submodule, act);
            return "false";
        }
    }

    public static String CheckRightsNoMsg(String Username, String Module, String Submodule, Context cntxt, Activity act, String ip) {
        URL = "";

        SoapObject request = new SoapObject(NAMESPACE, "GetViewPermission");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        request.addProperty("username", Username);
        request.addProperty("module", Module);
        request.addProperty("submodule", Submodule);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            androidHttpTransport.call(NAMESPACE + "GetViewPermission",
                    envelope);
        } catch (IOException | XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SoapPrimitive response = null;

        try {
            response = (SoapPrimitive) envelope.getResponse();
        } catch (SoapFault e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Msg(e.toString(), Toast.LENGTH_LONG, cntxt);
        }

        if (response.toString().equalsIgnoreCase("true")) {
            return "true";
        } else {

            return "false";
        }

    }

    public static String CheckRights2(String Username, String Module, String Submodule, Context cntxt, Activity act, String ip) {
        URL = "";

        SoapObject request = new SoapObject(NAMESPACE, "GetViewPermission");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        request.addProperty("username", Username);
        request.addProperty("module", Module);
        request.addProperty("submodule", Submodule);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            androidHttpTransport.call(NAMESPACE + "GetViewPermission",
                    envelope);
        } catch (IOException | XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SoapPrimitive response = null;

        try {
            response = (SoapPrimitive) envelope.getResponse();
        } catch (SoapFault e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Msg(e.toString(), Toast.LENGTH_LONG, cntxt);
        }

        if (response.toString().equalsIgnoreCase("true")) {
            return "true";
        } else {
            // warning("Warning", "Access Denied For " + Submodule, act);
            return "false";
        }

    }

    public static String isNetworkAvailable(String ip) {
        HttpGet httpGet = new HttpGet("http://" + ip);
        HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is established.
        // The default value is zero, that means the timeout is not used.
        int timeoutConnection = 3000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.
        int timeoutSocket = 5000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        String text = "";
        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
        try {
            Log.e("checking", "Checking network connection...");
            httpClient.execute(httpGet);
            text = "OK";
            Log.e("checking", "Connection OK " + ip);
            return text;
        } catch (ClientProtocolException e) {
            text = "";
            e.printStackTrace();
        } catch (IOException e) {
            text = "";
            e.printStackTrace();
        }
        Log.e("checking", "Connection unavailable");
        return text;

    }

    static void Msg(String string, int i, Context cntxt) {
        Toast.makeText(cntxt, string, i).show();
    }

    static void warning(String title, String msg, Activity cntxt) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(cntxt, R.style.AppCompatAlertDialogStyle);

        // set title
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(title);
        alertDialogBuilder.setIcon(R.drawable.warning);
        // set dialog message
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    public static String WebServiceCall(String[] a, String[] b, String Method, String Namespace, String URL) {
        String responsestring = "";
        try {
            SoapObject request = new SoapObject(Namespace, Method);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            if (a != null) {
                for (int i = 0; i <= a.length - 1; i++) {
                    if (b[i] == null) {
                        request.addProperty(a[i], null);
                    } else
                        request.addProperty(a[i], b[i]);
                }
            }
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            try {
                androidHttpTransport.call(Namespace + Method,
                        envelope);
            } catch (IOException | XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            SoapPrimitive response = null;
            try {
                response = (SoapPrimitive) envelope.getResponse();
                responsestring = response.toString();
            } catch (SoapFault e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "false " + e.toString();
            }
            return responsestring;
        } catch (Exception e) {
            return "false " + e.toString();       //	return false;
        }
    }

    public static String activeUsers(String pagename, Context cntxt, String ip) {
        URL = "";
        String responsetring = "";
        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.FRANCE);
            String strDate = mdformat.format(calendar.getTime());

            SoapObject request = new SoapObject(NAMESPACE, "DailyUserReport_Insertion");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            request.addProperty("username", LoginActivity.username);
            request.addProperty("date", strDate);
            request.addProperty("systemname", LoginActivity.devname);
            request.addProperty("systemip", LoginActivity.DeviceId.getText().toString());
            request.addProperty("pagename", pagename);
            request.addProperty("IsAndroid", "true");

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            try {
                androidHttpTransport.call(NAMESPACE + "DailyUserReport_Insertion", envelope);
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
            SoapPrimitive response;
            try {
                response = (SoapPrimitive) envelope.getResponse();
                responsetring = response.toString();


            } catch (SoapFault e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responsetring;
    }

    public static void InfoMsg(String title, String msg, Activity act) {

        if (((Activity) act).isFinishing()) {
            return;
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(act, R.style.color_dialog);

        // set title
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(title);
        alertDialogBuilder.setIcon(R.drawable.ic_alert_error);
        // set dialog message
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(true)
                .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                        dialog.dismiss();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    public static void dialog_success(Context cntxt, String title, String message, String button) {
        new PromptDialog(cntxt)
                .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                .setAnimationEnable(true)
                .setTitleText(title).setContentText(message)
                .setPositiveListener(button, new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }


    static void dialog_help(Context cntxt, String title, String message, String button) {
        new PromptDialog(cntxt)
                .setDialogType(PromptDialog.DIALOG_TYPE_HELP)
                .setAnimationEnable(true)
                .setTitleText(title).setContentText(message)
                .setPositiveListener(button, new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    static void dialog_info(Context cntxt, String title, String message, String button) {
        new PromptDialog(cntxt)
                .setDialogType(PromptDialog.DIALOG_TYPE_INFO)
                .setAnimationEnable(true)
                .setTitleText(title).setContentText(message)
                .setPositiveListener(button, new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public static void dialog_warning(Context cntxt, String title, String message, String button) {
        new PromptDialog(cntxt)
                .setDialogType(PromptDialog.DIALOG_TYPE_WARNING)
                .setAnimationEnable(true)
                .setTitleText(title).setContentText(message)
                .setPositiveListener(button, new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    static void dialog_error(Activity cntxt, String title, String message, String button) {
        new PromptDialog(cntxt)
                .setDialogType(PromptDialog.DIALOG_TYPE_WRONG)
                .setAnimationEnable(true)
                .setTitleText(title).setContentText(message)
                .setPositiveListener(button, new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }


    public static AnimationSet getInAnimationTest(Context context) {
        AnimationSet out = new AnimationSet(context, null);
        AlphaAnimation alpha = new AlphaAnimation(0.0f, 1.0f);
        alpha.setDuration(150);
        ScaleAnimation scale = new ScaleAnimation(0.6f, 1.0f, 0.6f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(150);
        out.addAnimation(alpha);
        out.addAnimation(scale);
        return out;
    }

    public static AnimationSet getOutAnimationTest(Context context) {
        AnimationSet out = new AnimationSet(context, null);
        AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.0f);
        alpha.setDuration(150);
        ScaleAnimation scale = new ScaleAnimation(1.0f, 0.6f, 1.0f, 0.6f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(150);
        out.addAnimation(alpha);
        out.addAnimation(scale);
        return out;
    }

    public static String InsertAuditInsert(String module, String subModule, String formName, String operation, String valueChanged, Context cntxt, String ip) {
        URL = "";
        String responsetring = "";
        try {
            SoapObject request = new SoapObject(NAMESPACE, "InsertAuditInsert");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            request.addProperty("userName", LoginActivity.username);
            request.addProperty("module", module);
            request.addProperty("subModule", subModule);
            request.addProperty("formName", formName);
            request.addProperty("operation", operation);
            request.addProperty("valueChanged", valueChanged);
            request.addProperty("fyear", LoginActivity.FinanceYear);
            request.addProperty("systemName", LoginActivity.devname);
            request.addProperty("systemIP", LoginActivity.DeviceId.getText().toString());

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            try {
                androidHttpTransport.call(NAMESPACE + "InsertAuditInsert", envelope);
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
            SoapPrimitive response;
            try {
                response = (SoapPrimitive) envelope.getResponse();
                responsetring = response.toString();
            } catch (SoapFault e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responsetring;
    }

    public static String CheckRights(String Username, String project, String Module, String Submodule, Context cntxt, Activity act, String ip) {
        URL = "";

        SoapObject request = new SoapObject(NAMESPACE, "");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        request.addProperty("username", Username);
        request.addProperty("project", project);
        request.addProperty("module", Module);
        request.addProperty("submodule", Submodule);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            androidHttpTransport.call(NAMESPACE + "", envelope);
        } catch (IOException | XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SoapPrimitive response = null;

        try {
            response = (SoapPrimitive) envelope.getResponse();
        } catch (SoapFault e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Msg(e.toString(), Toast.LENGTH_LONG, cntxt);
        }

        if (response.toString().equalsIgnoreCase("true")) {
            return "true";
        } else {
            warning("Warning", "Access Denied For " + Submodule, act);
            return "false";
        }
    }

    public static String CheckRightsTab(String Username, String project, String Module, String Submodule, Context cntxt, Activity act, String ip) {
        URL = "";

        SoapObject request = new SoapObject(NAMESPACE, "GetUserManagerViewPer");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        request.addProperty("username", Username);
        request.addProperty("project", project);
        request.addProperty("module", Module);
        request.addProperty("submodule", Submodule);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            androidHttpTransport.call(NAMESPACE + "GetUserManagerViewPer", envelope);
        } catch (IOException | XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SoapPrimitive response = null;

        try {
            response = (SoapPrimitive) envelope.getResponse();
        } catch (SoapFault e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Msg(e.toString(), Toast.LENGTH_LONG, cntxt);
        }

        if (response.toString().equalsIgnoreCase("true")) {
            return "true";
        } else {
            // warning("Warning", "Access Denied For " + Submodule, act);
            return "false";
        }
    }

}

