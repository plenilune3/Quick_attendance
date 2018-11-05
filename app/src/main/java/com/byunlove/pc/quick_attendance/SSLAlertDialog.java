package com.byunlove.pc.quick_attendance;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.webkit.SslErrorHandler;

public class SSLAlertDialog {

    private SslErrorHandler handler = null;
    private AlertDialog dialog = null;

    public SSLAlertDialog(SslErrorHandler errorHandler, Activity activity) {

        if(errorHandler == null || activity == null ) return;

        this.handler = errorHandler;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("SSL 인증서가 올바르지 않습니다. 계속 진행하시겠습니까?");
        builder.setPositiveButton("계속", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.proceed();
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.cancel();
            }
        });

        dialog = builder.create();
    }

    public void show(){
        dialog.show();
    }
}
