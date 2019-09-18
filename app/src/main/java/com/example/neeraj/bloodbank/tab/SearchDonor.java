package com.example.neeraj.bloodbank.tab;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.neeraj.bloodbank.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.json.JSONObject.NULL;

public class SearchDonor extends Fragment {

    Spinner Sp_blood_group;
    static String bloodtype;
    ListView donorList;
    private ArrayAdapter<String> adapter;
    static int pos;
    static ArrayList<String> item_name = new ArrayList<String>();
    static ArrayList<String> item_phone = new ArrayList<String>();
    //private static final String url = "http://192.168.0.6/BloodApp/searchdonor.php";

    private static final String url = "http://apkuniversal.16mb.com/BloodApp/searchdonor.php";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_donor, container, false);
        Sp_blood_group = (Spinner) v.findViewById(R.id.search_donor);
        donorList = (ListView) v.findViewById(R.id.donor_list);
        final ArrayAdapter<CharSequence> adapterblood = ArrayAdapter.createFromResource
                (getActivity(), R.array.bloodtype, android.R.layout.simple_spinner_item);
        adapterblood.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Sp_blood_group.setAdapter(adapterblood);
        Sp_blood_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodtype = adapterblood.getItem(position).toString();
                //bloodtype="neeraj";
                if (bloodtype.equals("Select Blood")) {
                    return;
                } else {
                    if(isConnection()) {
                        fetch_user_data();
                    }
                    else{
                        showConnectionErrorDialog();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        donorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(getContext(),"Clicked "+item_phone.get(position),Toast.LENGTH_SHORT).show();
                pos = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("More Information");
                builder.setMessage("Do you Want to Call this person");
                builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*Toast.makeText(getContext(), "Clicked " + item_phone.get(pos), Toast.LENGTH_SHORT).show();*/
                        if (isPermissionGranted()) {
                            call_action(pos);
                        }

                    }
                });
                builder.setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                Dialog dialog = builder.create();
                dialog.show();
            }
        });
        return v;
    }


    private void fetch_user_data() {

        new JSONAsyncTask().execute();
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
                item_phone.clear();
                item_name.clear();
                //HttpGet httppost = new HttpGet(urls[0]);
                HttpPost httppostt = new HttpPost(url);
                HttpClient httpclient = new DefaultHttpClient();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("bloodtype", bloodtype));
                httppostt.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppostt);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();

                    String data = EntityUtils.toString(entity);
                    JSONObject jsono = new JSONObject(data);
                    JSONArray jarray = jsono.getJSONArray("DonorResult");
                    System.out.println("MyName: " + bloodtype);
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);
                        item_name.add(object.getString("name"));
                        item_phone.add(object.getString("number"));
                        // System.out.println("MyName: inner loop "+items);
                    /*Sname=object.getString("name");
                    Semail=object.getString("email");*/
                    }
                    return true;
                }

            } catch (ParseException e1) {
                e1.printStackTrace();
                System.out.println("MyName A: " + e1);
            } catch (IOException e) {
                System.out.println("MyName B: " + e);
                e.printStackTrace();
            } catch (JSONException e) {
                System.out.println("MyName C: " + e);
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();

            if (result == false) {
                Toast.makeText(getContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
                return;
            } else {

                System.out.println("MyName: inner loop " + item_phone);
                adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_text_row, item_name);
                if (item_name.isEmpty()) {
                    Toast.makeText(getActivity(), "No any Donor", Toast.LENGTH_SHORT).show();
                    donorList.setAdapter(adapter);
                } else {
                    donorList.setAdapter(adapter);

                    return;
                }


                /*Ename.setText(Sname);
                Eemail.setText(Semail);
                Ephone.setText(Sphone);
                Epincode.setText(Spincode);
                Ebloodgroup.setText(Sbloodgroup);
                Egender.setText(Sgender);*/
            }


        }

    }

    public void call_action(int s) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        System.out.println("MyName Call" + item_phone.get(s));
        callIntent.setData(Uri.parse("tel:+91" + item_phone.get(s)));
        startActivity(callIntent);
    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
                    call_action(pos);
                } else {
                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
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
                //DonarRegistrationActivity.this.finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
