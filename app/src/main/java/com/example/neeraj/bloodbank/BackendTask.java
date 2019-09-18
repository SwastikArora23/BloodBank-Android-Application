package com.example.neeraj.bloodbank;


import android.app.ProgressDialog;
import android.content.Context;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


/**
 * Created by Neeraj on 5/5/2017.
 */

public class BackendTask extends AsyncTask<String,Void,String> {

    ProgressDialog progressDialog;
    Context ctx;
    String value;
    String username_pass;
    BackendTask(Context ctx){
        this.ctx=ctx;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog=new ProgressDialog(ctx);
        progressDialog.setTitle("Connecting Server..");
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    @Override
    protected String doInBackground(String... params) {

       // final String reg_url="http://192.168.0.6/bloodApp/register.php";
       // final String re_login_url="http://192.168.0.6/bloodApp/login.php";
        //final String update_url="http://192.168.0.6/bloodApp/update.php";

       final String reg_url="http://apkuniversal.16mb.com/BloodApp/register.php";
        final String re_login_url="http://apkuniversal.16mb.com/BloodApp/login.php";
        final String update_url="http://apkuniversal.16mb.com/BloodApp/update.php";

        String method=params[0];
        if (method.equals("DonorRegister")) {
            value="DonorRegister";
            String name = params[1];
            String email = params[2];
            String phone = params[3];
            String city = params[4];
            String pincode = params[5];
            String bloodgroup = params[6];
            String gender = params[7];
            String username = params[8];
            String password = params[9];
            String checkdonor = params[10];

            try {
                URL url = new URL(reg_url);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =
                    URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                    URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                    URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8") + "&" +
                    URLEncoder.encode("city", "UTF-8") + "=" + URLEncoder.encode(city, "UTF-8") + "&" +
                    URLEncoder.encode("pincode", "UTF-8") + "=" + URLEncoder.encode(pincode, "UTF-8") + "&" +
                    URLEncoder.encode("bloodgroup", "UTF-8") + "=" + URLEncoder.encode(bloodgroup, "UTF-8")+ "&" +
                    URLEncoder.encode("gender", "UTF-8") + "=" + URLEncoder.encode(gender, "UTF-8")+ "&" +
                    URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")+ "&" +
                    URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8")+ "&" +
                    URLEncoder.encode("checkdonor", "UTF-8") + "=" + URLEncoder.encode(checkdonor, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

      else if (method.equals("Login")) {
            value="Login";
            String username = params[1];
            username_pass= params[1];
            String password = params[2];

            try {
                URL url = new URL(re_login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if(method.equals("update")){
            value="update";
            String name = params[1];
            String email = params[2];
            String phone = params[3];
            String pincode = params[4];
            String bloodgroup = params[5];
            String gender = params[6];
            String checkdonor = params[7];
            String username=params[8];
            try {
                URL url = new URL(update_url);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =
                        URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8") + "&" +
                        URLEncoder.encode("pincode", "UTF-8") + "=" + URLEncoder.encode(pincode, "UTF-8") + "&" +
                        URLEncoder.encode("bloodgroup", "UTF-8") + "=" + URLEncoder.encode(bloodgroup, "UTF-8")+ "&" +
                        URLEncoder.encode("gender", "UTF-8") + "=" + URLEncoder.encode(gender, "UTF-8")+ "&" +
                        URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                        URLEncoder.encode("checkdonor", "UTF-8") + "=" + URLEncoder.encode(checkdonor, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
        if(value.equals("DonorRegister")) {
            if (result.trim().equals("Username already present")) {
               // Toast.makeText(ctx, "Username Already present", Toast.LENGTH_LONG).show();
                System.out.println(result);
            }
            if (result.equals("Data insertion sucess")) {
               // Toast.makeText(ctx, "Registration Success..", Toast.LENGTH_LONG).show();
                Intent i = new Intent(ctx, LoginActivity.class);
                ctx.startActivity(i);

            }
        }
         if(value.equals("Login"))
         {

            if(result.trim().equals("Login Success..Welcome")){
                //Toast.makeText(ctx, "Sucess..", Toast.LENGTH_LONG).show();
                Intent i=new Intent(ctx,MainActivity.class);
                i.putExtra("username",username_pass);
                ctx.startActivity(i);

            }
            else if(result.trim().equals("Login Failed..Try Again..")){

            }
            else {
                Toast.makeText(ctx,"Something Wrong Try Again Later",Toast.LENGTH_LONG).show();
            }
        }
        if(value.equals("update")){

        }

        progressDialog.dismiss();
    }
}
