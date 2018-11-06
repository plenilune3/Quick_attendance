package com.byunlove.pc.quick_attendance;

import android.app.Activity;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.support.v7.app.AlertDialog;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewSettings {

    WebView mWebView;

    public WebViewSettings(WebView webView){ mWebView = webView; }

    protected void setWebSettings(){

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

    }

    protected void setWebViewClient(final Activity activity){

        mWebView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;

            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {

                SSLAlertDialog dialog =  new SSLAlertDialog(handler, activity);
                dialog.show();

            }

        });

    }

    protected void setWebChromeClient(){

        mWebView.setWebChromeClient(new WebChromeClient(){

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {

                new AlertDialog.Builder(view.getContext())
                        .setTitle("알림")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int which) { result.confirm();
                                    }
                                })
                        .setCancelable(false)
                        .create()
                        .show();

                return true;

            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {

                new AlertDialog.Builder(view.getContext())
                        .setTitle("확인")
                        .setMessage(message)
                        .setPositiveButton("예",
                                new AlertDialog.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int which) { result.confirm();
                                    }
                                })
                        .setNegativeButton("아니오",
                                new AlertDialog.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int which) { result.cancel();
                                    }
                                })
                        .setCancelable(false)
                        .create()
                        .show();

                return true;

            }

        });

    }

}
