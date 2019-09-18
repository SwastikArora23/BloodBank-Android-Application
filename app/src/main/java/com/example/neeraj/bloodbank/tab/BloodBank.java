package com.example.neeraj.bloodbank.tab;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.neeraj.bloodbank.LoginActivity;
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

public class
BloodBank extends Fragment {

    TextView bloodO1,bloodO2,bloodA1,bloodA2,bloodAB1,bloodAB2,bloodB1,bloodB2;
    static String O1,O2,A1,A2,AB1,AB2,B1,B2;
    //private static final String url = "http://192.168.0.6/BloodApp/bloodcounting.php";
    private static final String url="http://apkuniversal.16mb.com/BloodApp/bloodcounting.php";
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bloodbank, container, false);
        bloodO1=(TextView)v.findViewById(R.id.bloodO1);
        bloodO2=(TextView)v.findViewById(R.id.bloodO2);
        bloodA1=(TextView)v.findViewById(R.id.bloodA1);
        bloodA2=(TextView)v.findViewById(R.id.bloodA2);
        bloodB1=(TextView)v.findViewById(R.id.bloodB1);
        bloodB2=(TextView)v.findViewById(R.id.bloodB2);
        bloodAB1=(TextView)v.findViewById(R.id.bloodAB1);
        bloodAB2=(TextView)v.findViewById(R.id.bloodAB2);
        if(isConnection()) {
            fetch_user_data();
        }
        else{
            showConnectionErrorDialog();
        }
        return v;
    }


    private void fetch_user_data(){

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
                HttpPost httppostt = new HttpPost(url);
                HttpClient httpclient = new DefaultHttpClient();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
               // nameValuePairs.add(new BasicNameValuePair("username",username));
                httppostt.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppostt);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

                    JSONObject jsono = new JSONObject(data);
                    System.out.println("Checking Data 1");
                    JSONArray jarray = jsono.getJSONArray("BloodCount");
                    System.out.println("Checking Data 2");
                    /*for (int i = 0; i < jarray.length(); i++) {*/
                    JSONObject object = jarray.getJSONObject(0);
                    O1=object.getJSONObject("O1").getString("COUNT(bloodgroup)");
                    O2=object.getJSONObject("O2").getString("COUNT(bloodgroup)");
                    A1=object.getJSONObject("A1").getString("COUNT(bloodgroup)");
                    A2=object.getJSONObject("A2").getString("COUNT(bloodgroup)");
                    B1=object.getJSONObject("B1").getString("COUNT(bloodgroup)");
                    B2=object.getJSONObject("B2").getString("COUNT(bloodgroup)");
                    AB1=object.getJSONObject("AB1").getString("COUNT(bloodgroup)");
                    AB2=object.getJSONObject("AB2").getString("COUNT(bloodgroup)");
                        System.out.println("Checking Data "+object.getJSONObject("B2").getString("COUNT(bloodgroup)"));
                        /*try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }*/

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

            if(result == false) {
                Toast.makeText(getContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
            }
            else{

                bloodO1.setText(O1);
                bloodO2.setText(O2);
                bloodA1.setText(A1);
                bloodA2.setText(A2);
                bloodB1.setText(B1);
                bloodB2.setText(B2);
                bloodAB1.setText(AB1);
                bloodAB2.setText(AB2);

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
