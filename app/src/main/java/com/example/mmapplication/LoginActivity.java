package com.example.mmapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mmapplication.Dialog.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {
    Button login_btn,join_btn;


    String url = "http://113.198.229.223:3000/login";
    TextInputEditText login_Id,login_pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_Id = (TextInputEditText)findViewById(R.id.loginID);
        login_pw = (TextInputEditText)findViewById(R.id.loginPW);

        login_btn = (Button)findViewById(R.id.login_btn);
        join_btn = (Button)findViewById(R.id.regist_btn);



        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id, pw;
                id = login_Id.getText().toString();
                pw = login_pw.getText().toString();
                try {
                    if(id.equals("")){
                        CustomDialog customDialog = new CustomDialog(LoginActivity.this);
                        customDialog.callFunction("아이디를 입력하세요.");
                    }
                    else if(pw.equals("")){
                        CustomDialog customDialog = new CustomDialog(LoginActivity.this);
                        customDialog.callFunction("비밀번호를 입력하세요.");
                    }
                    else {
                        String check = new JoinOkHttp().execute(url, make_json(id, pw, "1").toString()).get();
                        if (check.equals("noextantID")) {
                            CustomDialog customDialog = new CustomDialog(LoginActivity.this);
                            customDialog.callFunction("존재하지 않는 아이디입니다.");
                        } else if (check.equals("falsePW")) {
                            CustomDialog customDialog = new CustomDialog(LoginActivity.this);
                            customDialog.callFunction("비밀번호가 틀렸습니다.");
                        } else if (check.equals("oklogin")) {
                            CustomDialog customDialog = new CustomDialog(LoginActivity.this);
                            customDialog.callFunction("로그인 성공.");
                            //MainActivity.loginState=true;
                            // 로그인값 세션 만들고
                            // 메인화면으로 이동
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("user_id",login_Id.getText().toString());
                            intent.putExtra("user_pw",login_pw.getText().toString());
                            startActivity(intent);

                        }
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });
    }

    public JSONObject make_json(String id, String pw, String check){
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("pw", pw);
            json.put("checking",check);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }


}
