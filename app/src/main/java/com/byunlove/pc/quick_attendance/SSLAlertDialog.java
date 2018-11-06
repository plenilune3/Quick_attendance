package com.byunlove.pc.quick_attendance;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;

public class SSLAlertDialog {

    private SslErrorHandler handler = null;
    private AlertDialog dialog = null;

    public SSLAlertDialog(SslErrorHandler errorHandler, final Activity activity) {

        String SSLAlertMassege = "연결이 비공개로 설정되어 있지 않습니다.\n" +
                "공격자가 dreamy.jejunu.ac.kr에서 사용자의 정보를 도용하려고 " +
                "시도할 수 있습니다.\n" +
                "계속하시겠습니까?";

        if(errorHandler == null || activity == null ) return;

        this.handler = errorHandler;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(SSLAlertMassege);
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
                activity.finish();
            }
        });

        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    public void show(){
        dialog.show();
    }
}
