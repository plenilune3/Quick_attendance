package com.byunlove.pc.quick_attendance;

import android.content.DialogInterface;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;
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

public class Dreamy extends AppCompatActivity {
    private WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String id = getIntent().getStringExtra("ID");
        String pw = getIntent().getStringExtra("PW");
        Log.d("ID","ID : " + id);
        Log.d("PW","PW : " + pw);

        setContentView(R.layout.dreamy);
        mWebView = (WebView) findViewById(R.id.hayoung_dreamy);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //매개변수로 액티비티를 넘기기...?
                SSLAlertDialog dialog =  new SSLAlertDialog(handler, Dreamy.this);
                dialog.show();
            }
        });


        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Alert")
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

        HttpAsyncTask httpAsyncTask = new HttpAsyncTask();
        httpAsyncTask.execute(id, pw);

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


    private class HttpAsyncTask extends AsyncTask<String, Void, Void>{

        private HttpURLConnection conn;
        private String cookie;
        private List<String> cookies;
        private String[] cookies_str;
        private String loginCheck;


        private String url_home = "https://dreamy.jejunu.ac.kr/frame/main.do";

        @Override
        protected void onPreExecute() { super.onPreExecute(); }

        @Override
        protected Void doInBackground(String... params) {

            Log.d("ID","ID : " + params[0]);
            Log.d("PW","PW : " + params[1]);
            String body = "tmpu=MjAxNDEwODE3Mg==&tmpw=cmtkNzEzMTk3MyE=&mobile=&app=&z=Y&userid=&password=";

            try {
                URL url = new URL("https://dreamy.jejunu.ac.kr/frame/index.do");

                trustAllHosts();
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) { return true;
                    }
                });

                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST"); // POST방식 통신
                conn.setDoOutput(true); // 쓰기모드 지정
                conn.setDoInput(true); // 읽기모드 지정
                conn.setUseCaches(false); // 캐싱데이터를 받을지 안받을지
                conn.setDefaultUseCaches(false); // 캐싱데이터 디폴트 값 설정

                cookies = conn.getHeaderFields().get("Set-Cookie");
                cookies_str = cookies.toArray(new String[cookies.size()]);

                if (cookies != null) {
                    for (String cookie : cookies) {
                        Log.d("@COOKIE", cookie.split(";\\s*")[0]);
                    }
                }

                if (cookies_str != null){
                    for (int i = 0; i < cookies_str.length; i++){
                        cookie += cookies_str[i].toString().split(";\\s*")[0] + "; ";
                    }
                    cookie=cookie.substring(0, cookie.length()-1);
                }
                Log.d("@COOKIE_STRING", cookie);

            } catch (MalformedURLException | ProtocolException exception) { exception.printStackTrace(); }
            catch (IOException io) { io.printStackTrace(); }
            finally { conn.disconnect(); }

            try{
                URL url = new URL("https://dreamy.jejunu.ac.kr/frame/sysUser.do?next=");

                trustAllHosts();
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) { return true;
                    }
                });

                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST"); // POST방식 통신
                conn.setDoOutput(true); // 쓰기모드 지정
                conn.setDoInput(true); // 읽기모드 지정
                conn.setUseCaches(false); // 캐싱데이터를 받을지 안받을지
                conn.setDefaultUseCaches(false); // 캐싱데이터 디폴트 값 설정

                /*Request Header*/
                conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
                conn.setRequestProperty("Accept-Encoding", "gzip,deflate,br");
                conn.setRequestProperty("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
                conn.setRequestProperty("Cache-Control", "max-age=0");
                conn.setRequestProperty("Cookie", cookie);
                conn.setRequestProperty("Connection", "keep-alive");
                //conn.setRequestProperty("Content-Length", "91");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Host", "dreamy.jejunu.ac.kr");
                conn.setRequestProperty("Origin", "https://dreamy.jejunu.ac.kr");
                conn.setRequestProperty("Referer", "https://dreamy.jejunu.ac.kr/frame/index.do?dummy=&loginerror=1&next=");
                conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
                conn.setRequestProperty("User-Agent",
                        "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) " +
                                "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Mobile Safari/537.36");

                OutputStream os = conn.getOutputStream();
                os.write(body.getBytes("UTF-8")); // 출력 스트림에 출력.
                os.flush(); // 출력 스트림을 플러시(비운다)하고 버퍼링 된 모든 출력 바이트를 강제 실행.
                os.close(); // 출력 스트림을 닫고 모든 시스템 자원을 해제.

                Log.d("LOG",url+"로 HTTP 요청 전송");
                long temp = conn.getExpiration();
                loginCheck = "" + temp;
                Log.d("로그인확인", loginCheck);
                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) { //이때 요청이 보내짐.
                    Log.d("LOG", "HTTP_OK를 받지 못했습니다.");
                    return null;
                }

            } catch (MalformedURLException | ProtocolException exception) { exception.printStackTrace(); }
            catch (IOException io) { io.printStackTrace(); }
            finally { conn.disconnect(); }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if (loginCheck.equals("0")){
                CookieSyncManager.createInstance(mWebView.getContext());
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.setAcceptCookie(true);
                cookieManager.removeAllCookie();

                for (String cookie : cookies) {
                    Log.d("@COOKIE", cookie);
                    cookieManager.setCookie(url_home, cookie); //
                }

                CookieSyncManager.getInstance().sync();
                mWebView.loadUrl(url_home);
            }
            else {
                AlertDialog dialog = null;
                String loginFailedMassege = "로그인에 실패하셨습니다. \n다시 로그인 해주세요.";

                AlertDialog.Builder builder = new AlertDialog.Builder(Dreamy.this);
                builder.setMessage(loginFailedMassege);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });

                dialog = builder.create();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        }

        private void trustAllHosts() {

            // Create a trust manager that does not validate certificate chains

            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {

                    return new java.security.cert.X509Certificate[]{};

                }


                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)

                        throws java.security.cert.CertificateException {
                    // TODO Auto-generated method stub
                }

                @Override
                public void checkServerTrusted(

                        java.security.cert.X509Certificate[] chain, String authType)

                        throws java.security.cert.CertificateException {
                    // TODO Auto-generated method stub
                }

            }};

            // Install the all-trusting trust manager

            try {
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}
