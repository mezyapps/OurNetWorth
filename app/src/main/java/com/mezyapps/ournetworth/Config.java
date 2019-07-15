package com.mezyapps.ournetworth;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.widget.Button;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Config {

    public static void moveTo(Context context, Class targetClass) {
        Intent intent = new Intent(context, targetClass);
        context.startActivity(intent);
    }
    public static void showCustomAlertDialog(Context context, String title, String msg,int type) {
        SweetAlertDialog alertDialog = new SweetAlertDialog(context, type);
        alertDialog.setTitleText(title);

        if (msg.length() > 0)
            alertDialog.setContentText(msg);
        alertDialog.show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
    }
}