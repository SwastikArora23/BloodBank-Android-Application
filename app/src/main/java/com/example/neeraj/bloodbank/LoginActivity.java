package com.example.neeraj.bloodbank;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Neeraj on 9/29/2017.
 */

public class LoginActivity extends AppCompatActivity {
    Button btn_login,btn_register;
    EditText Eusername,Epassword;
    String Susername,Spassword;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Eusername=(EditText)findViewById(R.id.login_username);
        Epassword=(EditText)findViewById(R.id.login_password);
        btn_login=(Button)findViewById(R.id.main_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent jump_mainActivity=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(jump_mainActivity);*/

                if ( !validateUsername()  || !validatePassword()){
                    return;
                } else {

                    if (isConnection()) {
                        donor_login(v);
                        Eusername.setText("");
                        Epassword.setText("");
                    }
                    else {
                        showConnectionErrorDialog();
                    }


                }

            }
        });
        btn_register=(Button)findViewById(R.id.main_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jump_registration=new Intent(getApplicationContext(),DonarRegistrationActivity.class);
                startActivity(jump_registration);
            }
        });

    }

    private void donor_login(View v) {
        String method = "Login";
        Susername=Eusername.getText().toString();
        Spassword=(Epassword.getText().toString());
        BackendTask donorbackgroundtask = new BackendTask(this);
        donorbackgroundtask.execute(method,Susername,Spassword);

    }

    private boolean validateUsername() {
        if (Eusername.getText().toString().trim().isEmpty()) {
            Eusername.setError(("First Enter Username"));
            return false;
        }

       /* else if{

        }*/
        else {
            // user_name.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword() {
        if (Epassword.getText().toString().trim().isEmpty()) {
            Epassword.setError(("First Enter Password "));
            return false;
        } else {
            // user_name.setErrorEnabled(false);
        }
        return true;
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.ShareApps:

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent
                        .putExtra( Intent.EXTRA_TEXT,
                                "I am using "
                                        + getString(R.string.app_name)
                                        + " App ! Why don't you try it out...\nInstall "
                                        + getString(R.string.app_name)
                                        + " now !\nhttps://play.google.com/store/apps/details?id="
                                        + getPackageName());

                sendIntent.putExtra(Intent.EXTRA_SUBJECT,  getString(R.string.app_name) + " App !");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent,getString(R.string.share)));
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) LoginActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            return false;
        }
    }

    public void showConnectionErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
