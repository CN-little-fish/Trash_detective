//此代码要用

package com.zwk.trash_detective;

import android.app.ProgressDialog;
import android.content.Context;

public abstract class X_LoadingDialog {
    private static ProgressDialog mDialog;

    public static void show(Context context) {
        try{
            mDialog = new ProgressDialog(context);
            mDialog.setMessage("正在加载...");
            mDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
        }finally {

        }
    }

    public static void dismiss(){
        mDialog.dismiss();
    }

    public static ProgressDialog getmDialog() {
        return mDialog;
    }
}
