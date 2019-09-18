package com.example.neeraj.bloodbank.tab;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.neeraj.bloodbank.DetailEdit;
import com.example.neeraj.bloodbank.MainActivity;
import com.example.neeraj.bloodbank.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by Neeraj on 9/29/2017.
 */

public class MyDetails extends Fragment {
    Button btn_edit;
    ProgressDialog pDialog;
    TextView Ename, Eemail, Ephone, Epincode, Ebloodgroup, Egender;
    String Sname, Semail, Sphone, Spincode, Sbloodgroup, Sgender, Scheckdonor;
    CheckBox DonorCheck;
    static String username;
    // private static final String url = "http://192.168.0.6/BloodApp/fetchingdetails.php";
    private static final String url = "http://apkuniversal.16mb.com/BloodApp/fetchingdetails.php";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mydetails, container, false);
        MainActivity mainActivity = new MainActivity();
        username = mainActivity.getUsername_pass();
        System.out.println("username Mydetail" + username);
        btn_edit = (Button) v.findViewById(R.id.det_edit);
        Ename = (TextView) v.findViewById(R.id.det_name);
        Eemail = (TextView) v.findViewById(R.id.det_email);
        Ephone = (TextView) v.findViewById(R.id.det_phone);
        Epincode = (TextView) v.findViewById(R.id.det_pincode);
        Ebloodgroup = (TextView) v.findViewById(R.id.det_bloodgroup);
        Egender = (TextView) v.findViewById(R.id.det_gender);
        DonorCheck = (CheckBox) v.findViewById(R.id.det_checkbox);
        if(isConnection()) {
            fetch_user_data();
        }
        else{
            showConnectionErrorDialog();
        }
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DetailEdit.class);
                intent.putExtra("name", Ename.getText().toString());
                intent.putExtra("email", Eemail.getText().toString());
                intent.putExtra("phone", Ephone.getText().toString());
                intent.putExtra("pincode", Epincode.getText().toString());
                intent.putExtra("bloodgroup", Ebloodgroup.getText().toString());
                intent.putExtra("gender", Egender.getText().toString());
                intent.putExtra("username", username);
                if (DonorCheck.isChecked()) {
                    Scheckdonor = "true";
                } else {
                    Scheckdonor = "false";
                }
                intent.putExtra("checkdonor", Scheckdonor);
                System.out.println("LOg 1:" + Scheckdonor);
                startActivity(intent);
            }
        });
        return v;
    }

    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    private void fetch_user_data() {

        new JSONAsyncTask().execute(url);
    }


    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getContext());
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                //HttpGet httppost = new HttpGet(urls[0]);
                HttpPost httppostt = new HttpPost(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("username", username));
                httppostt.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppostt);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

                    JSONObject jsono = new JSONObject(data);
                    JSONArray jarray = jsono.getJSONArray("server_result");

                    //for (int i = 0; i < jarray.length(); i++) {
                    JSONObject object = jarray.getJSONObject(0);
                    Sname = object.getString("name");
                    Semail = object.getString("email");
                    Sphone = object.getString("number");
                    Spincode = object.getString("pincode");
                    Sbloodgroup = object.getString("bloodgroup");
                    Sgender = object.getString("gender");
                    Scheckdonor = object.getString("checkdonor");

                    // }
                    return true;
                }

            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();

            if (result == false) {
                Toast.makeText(getContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
            } else {
                Ename.setText(Sname);
                Eemail.setText(Semail);
                Ephone.setText(Sphone);
                Epincode.setText(Spincode);
                Ebloodgroup.setText(Sbloodgroup);
                Egender.setText(Sgender);
                System.out.println("check donor: " + Scheckdonor);
                if (Scheckdonor.equalsIgnoreCase("true")) {
                    DonorCheck.setChecked(true);
                } else {
                    DonorCheck.setChecked(false);
                }
            }

        }

    }

    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            return false;
        }
    }

    public void showConnectionErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("No Internet !!");
        builder.setMessage("Not connected to the network right now. Please turn it on and try again later");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
