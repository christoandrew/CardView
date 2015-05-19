package com.iconasystems.christo.utils;

import android.content.Context;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Christo on 1/29/2015.
 */
public class AlertDialogManager {
    public Context _context;
    public String mTitle;
    public String mMessage;
    public int type;
    private int mImageResId;


    public AlertDialogManager(Context context, int type) {
        this._context = context;
        this.type = type;
    }

    public AlertDialogManager(Context context, String message, String title) {
        this._context = context;
        this.mMessage = message;
        this.mTitle = title;
    }


    public AlertDialogManager(Context context, int type, String message, String title) {
        this._context = context;
        this.mTitle = title;
        this.mMessage = message;
        this.type = type;
    }

    public AlertDialogManager(Context context, int type, int imageResId, String message, String title){
        this._context = context;
        this.mTitle = title;
        this.mMessage = message;
        this.type = type;
        this.mImageResId = imageResId;
    }

    public SweetAlertDialog alertDialog(){
        SweetAlertDialog alertDialog = new SweetAlertDialog(_context);
        alertDialog.setTitleText(mTitle);
        alertDialog.setContentText(mMessage);
        alertDialog.changeAlertType(type);
        return alertDialog;
    }
}
