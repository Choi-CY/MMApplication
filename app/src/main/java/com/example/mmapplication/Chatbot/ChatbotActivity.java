package com.example.mmapplication.Chatbot;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.example.mmapplication.Diarys.Item;
import com.example.mmapplication.R;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatbotActivity extends AppCompatActivity {

    EditText editText;
    ImageView button,send_button;
    String getPackageName,language="ko-KR";
    STTandTTS one;
    Context context;
    Switch switch_k_or_e ;
    TextView text;
    ListView m_ListView;
    ChatAdapter m_Adapter;


    int num = 0;
    static JSONObject jsonObject = null;
    static String texts = null;
    //final String dialog_url = "https://api.dialogflow.com/v1/query?v=20150910";
    final String dialog_url = "http://113.198.229.223:3000/post";
    final String dialog_log = "http://113.198.229.223:3000/logchecking";
    String User_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        editText = (EditText) findViewById(R.id.edit);
        text = (TextView) findViewById(R.id.textss);
        button = (ImageView) findViewById(R.id.btn);
        send_button = (ImageView) findViewById(R.id.btn_send);


        Intent intent = getIntent();
        User_id = intent.getExtras().getString("id");

        // 커스텀 어댑터 생성
        m_Adapter = new ChatAdapter();

        // Xml에서 추가한 ListView 연결
        m_ListView = (ListView) findViewById(R.id.listView1);

        // ListView에 어댑터 연결
        m_ListView.setAdapter(m_Adapter);


        // stt tts 퍼미션
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},5);
        }

        getPackageName =  this.getPackageName();
        context = getBaseContext();
        try {
            String logs = new log_uploading().execute(dialog_log, make_log_json(User_id).toString()).get();
            Log.e("#Chating logs",logs);
            try {
                JSONArray jsonArray = new JSONArray(logs);
                for(int i =0; i< jsonArray.length(); i++){
                    JSONObject json = jsonArray.getJSONObject(i);
                    m_Adapter.add(json.getString("ask"),1);
                    m_Adapter.add(json.getString("reply"),0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        m_ListView.setSelection(m_Adapter.getCount() -1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("go","hohohohoh");
                num = 0;
                one = new STTandTTS(context,getPackageName,dialog_url,User_id);
                one.inputVoice(language,m_Adapter);
            }
        });

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //num = 0;
                //one = new STTandTTS(context,getPackageName,dialog_url);
                //one.inputVoice(language);
                if(!editText.getText().toString().equals("")) {
                    m_Adapter.add(editText.getText().toString(), 1);
                    m_Adapter.notifyDataSetChanged();
                    try {
                        String a = new OkHttpAsync().execute(dialog_url, make_json(editText.getText().toString(), language, User_id).toString()).get();
                        m_Adapter.add(a, 0);

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    editText.setText("");
                }
            }
        });
    }

    public JSONObject make_log_json(String id){
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public JSONObject make_json(String str,String language,String id){
        JSONObject json = new JSONObject();
        try {
            json.put("query", str);
            json.put("sessionId", id);
            json.put("lang", language);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
    // 기존 대화 목록 불러오기 서버와 연결 부분

    public class log_uploading extends AsyncTask<String, Void, String> {
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



