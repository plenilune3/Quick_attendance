package com.byunlove.pc.quick_attendance;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    EditText idInput, passwordInput;
    CheckBox autoLogin;
    Button loginBotton;
    Button Dreamy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idInput = (EditText) findViewById(R.id.idInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        autoLogin = (CheckBox) findViewById(R.id.checkBox);
        loginBotton = (Button) findViewById(R.id.loginButton);
        Dreamy = (Button) findViewById(R.id.dreamy);


        loginBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isOnline()){
                    Intent intent = new Intent (MainActivity.this, Attendance.class);
                    intent.putExtra("ID", "2014108172");
                    intent.putExtra("PW", "rkd7131973!");
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this, "인터넷 연결을 확인하세요", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Dreamy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()){
                    Intent intent = new Intent (MainActivity.this, Dreamy.class);
                    intent.putExtra("ID", "2014108172");
                    intent.putExtra("PW", "rkd7131973!");
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this, "인터넷 연결을 확인하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

  }

  private Boolean isOnline(){

      ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

      if(networkInfo != null && networkInfo.isConnected()){
          return true;
      }

      return false;
  }
}
