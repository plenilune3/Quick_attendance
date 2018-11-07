package com.byunlove.pc.quick_attendance;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class HttpsConnection {

    private WebView mWebView;
    private Activity activity;

    public HttpsConnection(WebView webView, Activity activity){
        mWebView = webView;
        this.activity = activity;
    }

    private String url_Home, url_Login, url_Destination, host, referer, origin, body;

    public void setUrl_Home(String url_Home) { this.url_Home = url_Home; }
    public void setUrl_Login(String url_Login) { this.url_Login = url_Login; }
    public void setUrl_Destination(String url_Destination) { this.url_Destination = url_Destination; }
    public void setHost(String host) { this.host = host; }
    public void setReferer(String referer) { this.referer = referer; }
    public void setOrigin(String origin) { this.origin = origin; }
    public void setBody(String body) { this.body = body; }

    public void execute(String id, String pw){

        HttpAsyncTask httpAsyncTask = new HttpAsyncTask();
        httpAsyncTask.execute(id, pw);

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, Void>{

        private HttpURLConnection conn;
        private List<String> cookies;

        @Override
        protected void onPreExecute() { super.onPreExecute(); }

        @Override
        protected Void doInBackground(String... strings) {

            Log.d("ID","ID : " + strings[0]);
            Log.d("PW","PW : " + strings[1]);

            try {

                URL url = new URL(url_Home);
                TrustAllHost trustAllHost = new TrustAllHost();
                trustAllHost.httpsConnection();
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setHostnameVerifier(new HostnameVerifier() {

                    @Override
                    public boolean verify(String s, SSLSession sslSession) { return true; }

                });

                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST"); // POST방식 통신
                conn.setDoOutput(true); // 쓰기모드 지정
                conn.setDoInput(true); // 읽기모드 지정
                conn.setUseCaches(false); // 캐싱데이터를 받을지 안받을지
                conn.setDefaultUseCaches(false); // 캐싱데이터 디폴트 값 설정

                cookies = conn.getHeaderFields().get("Set-Cookie");

            } catch (MalformedURLException | ProtocolException exception) { exception.printStackTrace(); }
            catch (IOException io) { io.printStackTrace(); }
            finally { conn.disconnect(); }

            try {

                URL url = new URL(url_Login);
                TrustAllHost trustAllHost = new TrustAllHost();
                trustAllHost.httpsConnection();
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setHostnameVerifier(new HostnameVerifier() {

                    @Override
                    public boolean verify(String s, SSLSession sslSession) { return true; }

                });

                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST"); // POST방식 통신
                conn.setDoOutput(true); // 쓰기모드 지정
                conn.setDoInput(true); // 읽기모드 지정
                conn.setUseCaches(false); // 캐싱데이터를 받을지 안받을지
                conn.setDefaultUseCaches(false); // 캐싱데이터 디폴트 값 설정

                RequestHeader();

                OutputStream os = conn.getOutputStream();
                os.write(body.getBytes("UTF-8")); // 출력 스트림에 출력.
                os.flush(); // 출력 스트림을 플러시(비운다)하고 버퍼링 된 모든 출력 바이트를 강제 실행.
                os.close(); // 출력 스트림을 닫고 모든 시스템 자원을 해제

                Log.d("LOG",url+"로 HTTP 요청 전송");

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

            if (LoginSuccess()){

                CookieSyncManager.createInstance(mWebView.getContext());
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.setAcceptCookie(true);
                cookieManager.removeAllCookie();

                for (String cookie : cookies) {
                    Log.d("@COOKIE", cookie);
                    cookieManager.setCookie(url_Destination, cookie); //
                }

                CookieSyncManager.getInstance().sync();
                mWebView.loadUrl(url_Destination);

            } else {

                AlertDialog dialog = null;
                String loginFailedMassege = "로그인에 실패하셨습니다. \n다시 로그인 해주세요.";

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage(loginFailedMassege);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { activity.finish(); }

                });

                dialog = builder.create();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

            }

        }

        private String Cookies(List<String> cookies) {

            String cookie = "";

            if (cookies != null) {

                for (String temp_cookie : cookies) {

                    cookie += temp_cookie.split(";\\s*")[0] + "; ";
                    Log.d("@COOKIE", temp_cookie.split(";\\s*")[0]);

                }

                cookie=cookie.substring(0, cookie.length()-1);

            }

            return cookie;

        }

        private void RequestHeader(){

            String cookie = Cookies(cookies);

            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            conn.setRequestProperty("Accept-Encoding", "gzip,deflate,br");
            conn.setRequestProperty("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
            conn.setRequestProperty("Cache-Control", "max-age=0");
            conn.setRequestProperty("Cookie", cookie);
            conn.setRequestProperty("Connection", "keep-alive");
            //conn.setRequestProperty("Content-Length", "91");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Host", host);
            conn.setRequestProperty("Origin", origin);
            conn.setRequestProperty("Referer", referer);
            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) "
                    + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Mobile Safari/537.36");

        }

        private boolean LoginSuccess() {

            String loginCheck = conn.getHeaderField(2);
            Log.d("로그인확인",loginCheck);

            return loginCheck.equals("text/html;charset=UTF-8");

        }

    }

}
