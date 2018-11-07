package com.byunlove.pc.quick_attendance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    EditText idInput, pwInput;
    CheckBox autoLogin;
    Button loginButton;
    Button dreamyButton;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String idString, pwString;
    Boolean autoCheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        idInput = (EditText) findViewById(R.id.idInput);
        pwInput = (EditText) findViewById(R.id.passwordInput);
        autoLogin = (CheckBox) findViewById(R.id.checkBox);
        loginButton = (Button) findViewById(R.id.Attendance);
        dreamyButton = (Button) findViewById(R.id.dreamy);

        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();

        autoCheck = pref.getBoolean("autoLogin", false);

        if(autoCheck){

            idString = pref.getString("id", null);
            pwString = pref.getString("pw", null);

            if(getAutoLogin(idString, pwString)){

                Intent intent = new Intent (MainActivity.this, Attendance.class);
                intent.putExtra("ID", idString);
                intent.putExtra("PW", pwString);
                startActivity(intent);

            }

        }

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(isOnline()){

                    idString = idInput.getText().toString();
                    pwString = pwInput.getText().toString();

                    if(setAutoLogin(idString, pwString)){

                        Intent intent = new Intent (MainActivity.this, Attendance.class);
                        intent.putExtra("ID", idString);
                        intent.putExtra("PW", pwString);
                        startActivity(intent);

                    }

                }

                else{ Toast.makeText(MainActivity.this, "인터넷 연결을 확인하세요", Toast.LENGTH_SHORT).show(); }

            }

        });

        dreamyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(isOnline()){

                    idString = idInput.getText().toString();
                    pwString = pwInput.getText().toString();

                    if(setAutoLogin(idString, pwString)){

                        Intent intent = new Intent (MainActivity.this, Dreamy.class);
                        intent.putExtra("ID", idString);
                        intent.putExtra("PW", pwString);
                        startActivity(intent);

                    }

                }

                else{ Toast.makeText(MainActivity.this, "인터넷 연결을 확인하세요", Toast.LENGTH_SHORT).show(); }

            }

        });

  }

  private Boolean getAutoLogin(String id, String pw){

      if(loginValidation(id, pw)){

          idInput.setText(id);
          pwInput.setText(pw);
          autoLogin.setChecked(true);
          return true;

      }

      else{

          Toast.makeText(MainActivity.this, "아이디와 패스워드를 입력하세요", Toast.LENGTH_SHORT).show();
          return false;

      }

  }
  private Boolean setAutoLogin(String id, String pw){

      if(loginValidation(id,pw)){

          Log.d("@loginValidation", "" + loginValidation(id, pw));
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

          Toast.makeText(MainActivity.this, "아이디와 패스워드를 입력하세요", Toast.LENGTH_SHORT).show();
          return false;

      }

  }

  private Boolean loginValidation(String id, String pw){

        if (!id.isEmpty() && !pw.isEmpty()){

            Log.d("@idString", id);
            Log.d("@pwString", pw);
            return true;

        }

        else{ return false; }

  }

  private Boolean isOnline(){

      ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

      if(networkInfo != null && networkInfo.isConnected()){ return true; }
      else{ return false; }

  }

}
