package com.example.mmapplication;

import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mmapplication.Chatbot.OkHttpAsync;
import com.example.mmapplication.Dialog.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class JoinActivity extends AppCompatActivity {

    TextInputEditText user_Id,user_pw,user_nick,user_name,user_pw_check;
    Button join_button,overlap_id,overlap_nick;
    String url = "http://113.198.229.223:3000/join";
    Boolean check_id=false,check_nick=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        user_Id = (TextInputEditText)findViewById(R.id.user_id);
        user_pw = (TextInputEditText)findViewById(R.id.user_pw);
        user_pw_check = (TextInputEditText)findViewById(R.id.user_pw_check);
        user_nick = (TextInputEditText)findViewById(R.id.user_nick);
        user_name = (TextInputEditText)findViewById(R.id.user_name);
        join_button = (Button)findViewById(R.id.join_button);
        overlap_id = (Button)findViewById(R.id.overlap_search_id);
        overlap_nick = (Button)findViewById(R.id.overlap_search_nick);

        //중복 아이디 검출
        overlap_id .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id;
                id = user_Id.getText().toString();
                try {
                    String check = new JoinOkHttp().execute(url, make_json(id," "," "," ","1").toString()).get();
                    if(check.equals("okid")){
                        CustomDialog customDialog = new CustomDialog(JoinActivity.this);
                        customDialog.callFunction("중복된 아이디입니다.");
                    }
                    else if(check.equals("noid")){
                        CustomDialog customDialog = new CustomDialog(JoinActivity.this);
                        customDialog.callFunction("사용 가능한 아이디입니다.");
                        check_id = true;
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        //중복 닉네임 검출
        overlap_nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nick;
                nick = user_nick.getText().toString();
                try {
                    String check = new JoinOkHttp().execute(url, make_json(" "," "," ",nick,"2").toString()).get();
                    if(check.equals("oknick")){
                        CustomDialog customDialog = new CustomDialog(JoinActivity.this);
                        customDialog.callFunction("중복된 닉네임 입니다.");
                    }
                    else if(check.equals("nonick")){
                        CustomDialog customDialog = new CustomDialog(JoinActivity.this);
                        customDialog.callFunction("사용 가능한 닉네임입니다.");
                        check_nick = true;
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = null,pw= null,nick= null,name= null,pw_check = null;
                id = user_Id.getText().toString();
                pw = user_pw.getText().toString();
                nick = user_nick.getText().toString();
                name = user_name.getText().toString();
                pw_check = user_pw_check.getText().toString();
                try {
                    if(!check_id){
                        CustomDialog customDialog = new CustomDialog(JoinActivity.this);
                        customDialog.callFunction("아이디 중복확인해주세요.");
                    }
                    else if(!check_nick){
                        CustomDialog customDialog = new CustomDialog(JoinActivity.this);
                        customDialog.callFunction("닉네임 중복확인해주세요.");
                    }
                    else if(!pw_check.equals(pw)){
                        CustomDialog customDialog = new CustomDialog(JoinActivity.this);
                        customDialog.callFunction("패스워드 확인해주세요.");
                    }
                    else if(!check_id && !check_nick){
                        CustomDialog customDialog = new CustomDialog(JoinActivity.this);
                        customDialog.callFunction("아이디,닉네임 중복확인해주세요.");
                    }
                    else if(!check_id && !pw_check.equals(pw)){
                        CustomDialog customDialog = new CustomDialog(JoinActivity.this);
                        customDialog.callFunction("아이디,패스워드 확인해주세요.");
                    }
                    else if(!pw_check.equals(pw) && !check_nick){
                        CustomDialog customDialog = new CustomDialog(JoinActivity.this);
                        customDialog.callFunction("패스워드,닉네임 확인해주세요.");
                    }
                    else if(!check_id && !pw_check.equals(pw) && !check_nick){
                        CustomDialog customDialog = new CustomDialog(JoinActivity.this);
                        customDialog.callFunction("아이디,패스워드,닉네임 확인해주세요.");
                    }
                    else if(check_nick && check_id && pw_check.equals(pw)){
                        String check = new JoinOkHttp().execute(url, make_json(id,pw,name,nick,"3").toString()).get();
                        if(check.equals("true")){
                            Toast.makeText(getApplicationContext(),"회원가입이 완료되었습니다.",Toast.LENGTH_LONG).show();
                            onBackPressed();
                        }
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public JSONObject make_json(String id, String pw, String nick, String name,String check){
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("pw", pw);
            json.put("name", nick);
            json.put("nick", name);
            json.put("checking",check);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
