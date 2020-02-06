package com.example.mmapplication;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class JoinOkHttp extends AsyncTask<String, Void, String> {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
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
                .addHeader("Authorization", "Bearer 6e34f6f94e584a819ed46b5ce406ad0a")
                .addHeader("Content-Type", "application/json")
                .url(params[0])
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String str = response.body().string();
            //Log.e("check ",str);
            String sended_text = null;
            Log.e("check 1 ",str);
            JSONObject json;
            try {
                json = new JSONObject(str);
                sended_text = new JSONObject(json.getString("message")).getString("text"); // 받아온 데이터 json 해제
                Log.e("check 1 ",sended_text);
            }catch (JSONException e) {
                e.printStackTrace();
            }
            return sended_text;
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
