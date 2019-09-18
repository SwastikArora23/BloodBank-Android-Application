package com.example.neeraj.bloodbank;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.regex.Pattern;

public class DonarRegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    EditText Ename,EEmail, Ephone, Epincode,Eusername,Epassword;
    String Sname, Semail,Sphone, Scity, Spincode, Sbloodgroup,Sgender,Susername,Spassword,Scheckdonor;
    Button Register;
    Spinner Sp_states, Sp_blood_group;
    RadioGroup radioGroup;
    RadioButton R_radio_btn;
    CheckBox checkBoxDonor;
    Boolean Bcheck_donor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donar_registration);
        //typecaste
        Register = (Button) findViewById(R.id.reg_donor_submit);
       /* Login = (Button) findViewById(R.id.donor_login_btn);*/
        Ename = (EditText) findViewById(R.id.d_reg_name);
        EEmail=(EditText)findViewById(R.id.d_reg_emailid);
        Ephone = (EditText) findViewById(R.id.d_reg_phone);
        Sp_states = (Spinner) findViewById(R.id.d_reg_state);
        Epincode = (EditText) findViewById(R.id.d_reg_pincode);
        Sp_blood_group = (Spinner) findViewById(R.id.d_reg_blood);
        radioGroup = (RadioGroup) findViewById(R.id.d_reg_radioGroupbtn);
        Eusername=(EditText)findViewById(R.id.d_reg_username);
        Epassword = (EditText) findViewById(R.id.d_reg_password);
        checkBoxDonor=(CheckBox)findViewById(R.id.d_reg_donorcheck);
        ArrayAdapter<CharSequence> adapterstate = ArrayAdapter.createFromResource(this, R.array.State, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterblood = ArrayAdapter.createFromResource(this, R.array.bloodtype, android.R.layout.simple_spinner_item);
        adapterstate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterblood.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Sp_states.setAdapter(adapterstate);
        Sp_blood_group.setAdapter(adapterblood);
        Register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reg_donor_submit:

             if ( !validateName() || !validateEmail() || !validatePhone() || !validateSpinnerStates()
                     || !validatepincode() || !validateSpinnerBlood() || !validateGender() || !validateUsername()  || !validatePassword()){
                    return;
                } else {


                 if (isConnection()) {
                     donor_registeration(v);
                     Ename.setText("");
                     Ephone.setText("");
                     Eusername.setText("");
                     EEmail.setText("");
                     Epassword.setText("");
                     Epincode.setText("");
                 }
                 else{
                     showConnectionErrorDialog();
                 }

                }
                break;
           /* case R.id.donor_login_btn:
                Intent login_launch = new Intent(this, DonarLoginActivity.class);
                startActivity(login_launch);
                break;*/
        }

    }

    public void donor_registeration(View view) {
        Sname = Ename.getText().toString();
        Semail=EEmail.getText().toString();
        Sphone = Ephone.getText().toString();
        Scity=String.valueOf(Sp_states.getSelectedItem()).toString();
        Spincode = Epincode.getText().toString();
        Sbloodgroup=String.valueOf(Sp_blood_group.getSelectedItem()).toString();
        int selectedId = radioGroup.getCheckedRadioButtonId();
        R_radio_btn = (RadioButton) findViewById(selectedId);
        Sgender=String.valueOf(R_radio_btn.getText()).toString();
        Susername=Eusername.getText().toString();
        Spassword = Epassword.getText().toString();
        Bcheck_donor=checkBoxDonor.isChecked();
        Scheckdonor=Bcheck_donor.toString();
        String method = "DonorRegister";
        BackendTask backendTask = new BackendTask(this);
        backendTask.execute(method,Sname,Semail,Sphone,Scity,Spincode,Sbloodgroup,Sgender,Susername,Spassword,Scheckdonor);

    }

    private boolean validateName() {
        if (Ename.getText().toString().trim().isEmpty()) {
            Ename.setError(("First Enter Name"));
            return false;
        } else {
            String NAME_PATTERN = "[a-zA-Z ]";
            if(Pattern.compile(NAME_PATTERN).matcher(Ename.getText().toString()).matches()){
                Ename.setError("Please fill your Name correctly");
            }

        }

        return true;
    }
    private boolean validateEmail() {
        if (EEmail.getText().toString().trim().isEmpty()) {
            EEmail.setError(("First Enter Email"));
            return false;
        } else {
            // user_name.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePhone() {
        if (Ephone.getText().toString().trim().isEmpty()) {
            Ephone.setError(("First Enter Phone"));
            return false;
        }
        else if(Ephone.getText().toString().length() < 10){
            Ephone.setError("Wrong Number ");
            return false;
        }
        else {

        }

        return true;
    }

    private boolean validatepincode() {
        if (Epincode.getText().toString().trim().isEmpty()) {
            Epincode.setError(("First Enter pincode"));
            return false;
        }
        else if(Epincode.getText().toString().length() < 6){
            Epincode.setError("Wrong Pincode");
            return false;
        }
        else {
            // user_name.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateGender(){
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please Select gender ", Toast.LENGTH_SHORT).show();
            return false;
        } else {
           /* int selectedId = radioGroup.getCheckedRadioButtonId();
            R_radio_btn = (RadioButton) findViewById(selectedId);
            Toast.makeText(this, String.valueOf(R_radio_btn.getText()), Toast.LENGTH_LONG).show();*/

        }
        return true;
    }
    private boolean validateSpinnerStates(){
        if(String.valueOf(Sp_states.getSelectedItem()).equals("Select State.."))
        {
            Toast.makeText(this, "Please Select States", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{}
        return true;
    }
    private boolean validateSpinnerBlood(){
        if(String.valueOf(Sp_blood_group.getSelectedItem()).equals("Select Blood"))
        {
            Toast.makeText(this, "Please Select Blood group", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{}
        return true;
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
        ConnectivityManager manage = (ConnectivityManager) DonarRegistrationActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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
