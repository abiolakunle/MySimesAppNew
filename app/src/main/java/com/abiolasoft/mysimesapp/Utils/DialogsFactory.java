package com.abiolasoft.mysimesapp.Utils;

import android.content.Context;

import com.abiolasoft.mysimesapp.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DialogsFactory {
    private static SweetAlertDialog pDialog;
    private static SweetAlertDialog eDialog;
    private static SweetAlertDialog sDialog;
    private Context context;


    public DialogsFactory(Context context) {
        this.context = context;
        initMsgDialogs();
    }

    public SweetAlertDialog sDialog() {
        return sDialog;
    }

    public SweetAlertDialog eDialog() {
        return eDialog;
    }

    public SweetAlertDialog pDialog() {
        return pDialog;
    }


    private void initMsgDialogs() {
        eDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        eDialog.getProgressHelper().setBarColor(R.color.primary_dark);
        eDialog.setCancelable(false);
        eDialog.setTitleText("Error");
        eDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        });

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(R.color.primary_dark);
        pDialog.setCancelable(false);

        sDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
        sDialog().getProgressHelper().setBarColor(R.color.primary_dark);
        sDialog().setTitleText("Success");

    }
}
