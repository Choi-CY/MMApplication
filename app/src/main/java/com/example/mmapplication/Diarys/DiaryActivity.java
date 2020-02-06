package com.example.mmapplication.Diarys;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mmapplication.Dialog.CustomDialog;
import com.example.mmapplication.R;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mmapplication.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DiaryActivity extends AppCompatActivity {
    static final int ITEM_SIZE = 4;
    int Page_Num = 1;
    String url = "http://113.198.229.223:3000/diary";
    String Email = null;
    String Name;
    static List<Item> items = new ArrayList<>();
    private SwipeRefreshLayout refreshLayout = null;
    static RecyclerView recyclerView;
    static RecyclerView.Adapter Adapter;
    static RecyclerView.LayoutManager layoutManager;
    Button w_btn;
    String User_id;
    String[] permission_list ={
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        w_btn = (Button)findViewById(R.id.w_b_btn);

        Intent intent = getIntent();
        User_id = intent.getExtras().getString("id");

        CheckPermission();
        //----------------------- 새로고침 --------------------------------------
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                items.clear();
                try {
                    String str = new upload().execute(url, make_json(User_id).toString()).get();
                    if(str.equals("No")){
                        CustomDialog customDialog = new CustomDialog(DiaryActivity.this);
                        customDialog.callFunction("게시글이 없습니다.");
                    }else{

                        try {
                            JSONArray jsonArray = new JSONArray(str);
                            for(int i =0; i< jsonArray.length(); i++){
                                JSONObject json = jsonArray.getJSONObject(i);
                                items.add(new Item(json.getInt("num"),
                                                json.getString("title"),
                                                json.getString("image"),
                                                json.getString("id"),
                                                json.getString("content"),
                                                json.getString("day")
                                        )
                                );
                                Log.e("diary err 1 ",items.get(0).image);
                                refreshLayout.setRefreshing(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                recyclerView.setHasFixedSize(true);
                // adapter

                recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), items, R.layout.activity_diary));
                recyclerView.setLayoutManager( new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL) );
            }
        });
        w_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),subActivity.class);
                intent.putExtra("id",User_id);
                startActivity(intent);
            }
        });
        items.clear();
        try {
            String str = new upload().execute(url, make_json(User_id).toString()).get();
            if(str.equals("No")){
                CustomDialog customDialog = new CustomDialog(DiaryActivity.this);
                customDialog.callFunction("게시글이 없습니다.");
            }else{
                try {
                    JSONArray jsonArray = new JSONArray(str);
                    for(int i =0; i< jsonArray.length(); i++){
                        JSONObject json = jsonArray.getJSONObject(i);
                        items.add(new Item(json.getInt("num"),
                                           json.getString("title"),
                                           json.getString("image"),
                                           json.getString("id"),
                                           json.getString("content"),
                                           json.getString("day")
                            )
                        );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        recyclerView.setHasFixedSize(true);
        // adapter

        recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), items, R.layout.activity_diary));
        recyclerView.setLayoutManager( new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL) );
        // adapterView - ListView, GridView

    }

    private void CheckPermission() {
       if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){ // 현 버전이 6버전 마쉬멜로우 보다 아래라면 종료
           return;
       }
       for(String permission:permission_list){
           int chk = checkCallingOrSelfPermission(permission);
           if(chk == PackageManager.PERMISSION_DENIED){
               requestPermissions(permission_list,0);
           }
       }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode == 0){
            for(int i =0; i<grantResults.length;i++){
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED){

                }
                else{
                    Toast.makeText(getApplicationContext(),"앱의 권한을 설정하세요.",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }

    public JSONObject make_json(String id){
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
    public class upload extends AsyncTask<String, Void, String> {
        public final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... params) {

            client = new OkHttpClient().newBuilder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .writeTimeout(5,TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .build();

            Log.e("check ",params[1]);

            RequestBody body = RequestBody.create(JSON,params[1]);

            Request request = new Request.Builder()
                    .addHeader("Authorization", "Bearer 648b2964ea91400791b65d90f08e3a8a")
                    .addHeader("Content-Type", "application/json")
                    .url(params[0])
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String str = response.body().string();
                Log.e("result",str);
                return str;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String b) {
            //Handle result here
            super.onPostExecute(b);
        }
    }


}
