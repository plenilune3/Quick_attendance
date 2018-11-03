package com.byunlove.pc.quick_attendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    EditText idInput, passwordInput;
    CheckBox autoLogin;
    Button loginBotton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idInput = (EditText) findViewById(R.id.idInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        autoLogin = (CheckBox) findViewById(R.id.checkBox);
        loginBotton = (Button) findViewById(R.id.loginButton);
        //인텐트로 아이디 비밀번호 넘겨서 처리해야함
        //자동로그인(SharedPreference) 구현해보기


        loginBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, Attendance.class);
                intent.putExtra("ID", "2014108172");
                intent.putExtra("PW", "rkd7131973!");
                startActivity(intent);
            }
        });

  }
}
