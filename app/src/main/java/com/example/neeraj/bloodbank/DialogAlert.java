package com.example.neeraj.bloodbank;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by Neeraj on 10/2/2017.
 */

public class DialogAlert extends DialogFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
   /*     final int pos=position;
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("More Information");
        builder.setMessage("Do you Want to Call this person");
        builder.setMessage("Phone: "+item_phone.get(position));
        builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(),"Clicked "+item_phone.get(pos),Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        Dialog dialog=builder.create();
        return dialog;*/
    }
}
