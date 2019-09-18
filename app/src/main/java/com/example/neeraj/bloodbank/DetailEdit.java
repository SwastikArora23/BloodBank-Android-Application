
package com.example.neeraj.bloodbank;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

/**
 * Created by Neeraj on 9/30/2017.
 */

public class DetailEdit extends AppCompatActivity{

    Button btn_update,btn_cancel;
    EditText Ename,Eemail,Ephone,Epincode,Ebloodgroup,Egender;
   String Sname,Semail,Sphone,Spincode,Sbloodgroup,Sgender,Scheckdonor;
    Boolean bol_check_donor;
    CheckBox checkBoxDonor;
    static String username;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_edit);
        Intent i=getIntent();
        Ename=(EditText)findViewById(R.id.det_e_name);
        Eemail=(EditText)findViewById(R.id.det_e_email);
        Ephone=(EditText)findViewById(R.id.det_e_phone);
        Epincode=(EditText)findViewById(R.id.det_e_pincode);
        btn_update=(Button)findViewById(R.id.det_ed_update);
        Ebloodgroup=(EditText)findViewById(R.id.det_e_bloodgroup);
        Egender=(EditText)findViewById(R.id.det_e_gender);
        checkBoxDonor=(CheckBox)findViewById(R.id.det_e_checkbox);
        passDataEditText(i);
        btn_cancel=(Button)findViewById(R.id.det_ed_cancel);
       btn_cancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
               overridePendingTransition(R.anim.tr_c, R.anim.tr_d);
               //getActivity().overridePendingTransition(R.anim.tr_a, R.anim.tr_b);

           }
       });

       btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( !validateName() || !validateEmail() || !validatePhone()
                        || !validatepincode() ||!validateGender()){

                    return ;
                } else {

                    donor_modify(v);
                    finish();
                    overridePendingTransition(R.anim.tr_c, R.anim.tr_d);
                }
            }
        });
    }

    private void donor_modify(View v) {


        Sname = Ename.getText().toString();
        Semail=Eemail.getText().toString();
        Sphone = Ephone.getText().toString();
        Spincode = Epincode.getText().toString();
        Sbloodgroup=(Ebloodgroup).getText().toString();
        Sgender=(Egender).getText().toString();
        bol_check_donor=checkBoxDonor.isChecked();
        Scheckdonor=bol_check_donor.toString();
        String method = "update";
        BackendTask backendTask = new BackendTask(this);
        backendTask.execute(method,Sname,Semail,Sphone,Spincode,Sbloodgroup,Sgender,Scheckdonor,username);
    }


    private void passDataEditText(Intent i) {
        System.out.println("name " +Sname);
        Sname=i.getStringExtra("name");
        Semail=i.getStringExtra("email");
        Sphone=i.getStringExtra("phone");
        Spincode=i.getStringExtra("pincode");
        Sbloodgroup=i.getStringExtra("bloodgroup");
        Sgender=i.getStringExtra("gender");
        Scheckdonor=i.getStringExtra("checkdonor");
        System.out.println("LOg 2:"+Scheckdonor);
        if(Scheckdonor.equalsIgnoreCase("true"))
        {
            checkBoxDonor.setChecked(true);
        }
        else{
            checkBoxDonor.setChecked(false);
        }
        username=i.getStringExtra("username");
        Ename.setText(Sname);
        Eemail.setText(Semail);
        Ephone.setText(Sphone);
        Epincode.setText(Spincode);
        Ebloodgroup.setText(Sbloodgroup);
        Egender.setText(Sgender);

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
        if (Eemail.getText().toString().trim().isEmpty()) {
            Eemail.setError(("First Enter Email"));
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

    private boolean validateGender() {
        if (Egender.getText().toString().trim().isEmpty()) {
            Egender.setError(("First Enter Gender"));
            return false;
        } else if(Egender.getText().toString().trim().equalsIgnoreCase("Male")||
                Egender.getText().toString().trim().equalsIgnoreCase("Female")){
            // user_name.setErrorEnabled(false);
            return true;
        }
        else{
            Egender.setError(("Enter Male or Female only"));
        }
        return true;
    }

}