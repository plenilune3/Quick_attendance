package com.byunlove.pc.quick_attendance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;

public class Dreamy extends AppCompatActivity {

    private WebView mWebView;
    private String encodingId, encodingPw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dreamy_webview);
        mWebView = (WebView) findViewById(R.id.hayoung_dreamy);

        String id = getIntent().getStringExtra("ID");
        String pw = getIntent().getStringExtra("PW");
        encodingId = Base64.encodeToString(id.getBytes(), Base64.DEFAULT);
        encodingPw = Base64.encodeToString(pw.getBytes(),Base64.DEFAULT);

        String url_Home = "https://dreamy.jejunu.ac.kr/frame/index.do";
        String url_Login = "https://dreamy.jejunu.ac.kr/frame/sysUser.do?next=";
        String url_Destination = "https://dreamy.jejunu.ac.kr/frame/main.do";
        String host = "dreamy.jejunu.ac.kr";
        String referer = "https://dreamy.jejunu.ac.kr/frame/index.do?dummy=&loginerror=1&next=";
        String origin = "https://dreamy.jejunu.ac.kr";
        String body = "tmpu=" + encodingId + "&tmpw=" + encodingPw + "&mobile=&app=&z=Y&userid=&password=";


        Log.d("ID","ID : " + id);
        Log.d("PW","PW : " + pw);

        WebViewSettings webViewSettings = new WebViewSettings(mWebView);
        webViewSettings.setWebSettings();
        webViewSettings.setWebChromeClient();
        webViewSettings.setWebViewClient(this);

        HttpsConnection httpsConnection = new HttpsConnection(mWebView, Dreamy.this);
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
