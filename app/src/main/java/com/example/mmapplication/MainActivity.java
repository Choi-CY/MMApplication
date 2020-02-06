package com.example.mmapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mmapplication.Chatbot.ChatbotActivity;
import com.example.mmapplication.Dialog.CustomDialog;
import com.example.mmapplication.Diarys.DiaryActivity;
import com.example.mmapplication.Log.LogActivity;
import com.example.mmapplication.Map.MapActivity;
import com.example.mmapplication.market.MarketActivity;
import com.example.mmapplication.web.FacebookActivity;
import com.example.mmapplication.web.InstaActivity;
import com.example.mmapplication.web.YoutubeActivity;
import com.example.mmapplication.web.twiterActivity;

public class MainActivity extends AppCompatActivity {
    ImageView facebook,twiter,youtube,insta;
    TextView login,chat_logs;
    LinearLayout diarys,markets,chatbot,maps;
    String User_id,User_pw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chat_logs = (TextView)findViewById(R.id.chat_log);
        facebook = (ImageView)findViewById(R.id.facebook);
        youtube = (ImageView)findViewById(R.id.youtube);
        twiter = (ImageView)findViewById(R.id.twiter);
        insta = (ImageView)findViewById(R.id.insta);
        login = (TextView) findViewById(R.id.login);
        chatbot = (LinearLayout) findViewById(R.id.chatbot);
        maps = (LinearLayout) findViewById(R.id.map);
        diarys = (LinearLayout) findViewById(R.id.diary);
        markets = (LinearLayout)findViewById(R.id.market_btn);
        Intent intent = getIntent();
        User_id = intent.getExtras().getString("user_id");
        User_pw = intent.getExtras().getString("user_pw");



        chatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatbotActivity.class);
                intent.putExtra("id",User_id);
                startActivity(intent);
            }
        });
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                intent.putExtra("id",User_id);
                startActivity(intent);
            }
        });
        diarys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DiaryActivity.class);
                intent.putExtra("id",User_id);
                startActivity(intent);
            }
        });
        markets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MarketActivity.class);
                intent.putExtra("id",User_id);
                startActivity(intent);
            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FacebookActivity.class);
                startActivity(intent);
            }
        });
        twiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), twiterActivity.class);
                startActivity(intent);
            }
        });
        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InstaActivity.class);
                startActivity(intent);
            }
        });
        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), YoutubeActivity.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                User_id = "";
                User_pw = "";
                startActivity(intent);
            }
        });


    }


}