package com.android.lazyloading.recyclerview.alert;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.android.lazyloading.recyclerview.R;


/**
 * Alert dialog class
 */
public class LazyLoadAlertDialog {

    public static void alertDilaog(Context context, String message) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Alert");
        alert.show();
    }
}
