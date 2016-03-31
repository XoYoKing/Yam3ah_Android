package com.ds.yam3ah.yam3ah;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by admin on 4/17/2015.
 */
public class CustomAlertDialog {

    @SuppressWarnings("deprecation")
    public void showAlertDialog(Context context, int projectname, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(projectname);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


}
