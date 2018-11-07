package com.byunlove.pc.quick_attendance;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by pc on 2018-09-05.
 */

public class Attendance extends AppCompatActivity {

    private WebView mWebView;

    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_webview);
        mWebView = (WebView) findViewById(R.id.smart_attendance);

        String id = getIntent().getStringExtra("ID");
        String pw = getIntent().getStringExtra("PW");
        String url_Home = "https://elearning.jejunu.ac.kr/MMain.do?cmd=viewIndexPage";
        String url_Login = "https://elearning.jejunu.ac.kr/MUser.do";
        String url_Destination = "https://elearning.jejunu.ac.kr/MSmartatt.do?cmd=viewAttendCourseList";
        String host = "elearning.jejunu.ac.kr";
        String referer = "http://elearning.jejunu.ac.kr/MMain.do?cmd=viewIndexPage";
        String origin = "http://elearning.jejunu.ac.kr";
        String body = "cmd=loginUser&userDTO.userId=" + id + "&userDTO.password=" + pw + "&userDTO.localeKey=ko";

        Log.d("@ID","ID : " + id);
        Log.d("@PW","PW : " + pw);

        WebViewSettings webViewSettings = new WebViewSettings(mWebView);
        webViewSettings.setWebSettings();
        webViewSettings.setWebChromeClient();
        webViewSettings.setWebViewClient(this);

        HttpsConnection httpsConnection = new HttpsConnection(mWebView, Attendance.this);
        httpsConnection.setUrl_Home(url_Home);
        httpsConnection.setUrl_Login(url_Login);
        httpsConnection.setUrl_Destination(url_Destination);
        httpsConnection.setHost(host);
        httpsConnection.setReferer(referer);
        httpsConnection.setOrigin(origin);
        httpsConnection.setBody(body);
        httpsConnection.execute(id, pw);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (mWebView.canGoBack()) {

                mWebView.goBack();
                return false;

            }

        }

        return super.onKeyDown(keyCode, event);

    }

}
