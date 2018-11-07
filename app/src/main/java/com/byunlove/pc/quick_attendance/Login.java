package com.byunlove.pc.quick_attendance;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.Toast;

public class Login {

    public Boolean getAutoLogin(String id, String pw, Activity activity){

        if(loginValidation(id, pw)){ return true; }

        else{

            Toast.makeText(activity, "아이디와 패스워드를 입력하세요", Toast.LENGTH_SHORT).show();
            return false;

        }

    }

    public Boolean setAutoLogin(String id, String pw, CheckBox autoLogin, SharedPreferences.Editor editor, Activity activity){

        if(loginValidation(id,pw)){

            if (autoLogin.isChecked()){

                editor.putBoolean("autoLogin", true);
                editor.putString("id", id);
                editor.putString("pw", pw);
                editor.apply();

            }

            else{

                editor.clear();
                editor.apply();

            }

            return true;

        }

        else{

            Toast.makeText(activity, "아이디와 패스워드를 입력하세요", Toast.LENGTH_SHORT).show();
            return false;

        }

    }

    public Boolean isOnline(NetworkInfo networkInfo){

        if(networkInfo != null && networkInfo.isConnected()){ return true; }
        else{ return false; }

    }

    private Boolean loginValidation(String id, String pw){

        if (!id.isEmpty() && !pw.isEmpty()){ return true; }
        else{ return false; }

    }



}
