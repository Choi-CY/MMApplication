package com.example.mmapplication.market;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mmapplication.Dialog.CustomDialog;
import com.example.mmapplication.Diarys.SubOkHttp;
import com.example.mmapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Sub2Activity extends AppCompatActivity {
    String User_id,imagePath = "";
    Uri selectedImageUri;
    Bitmap img;
    TextView textView;
    Button SaveButton,Get_Picture;
    EditText editText,title_edit;
    String url = "http://113.198.229.223:3000/marketsend";
    ImageView imageView,test;
    final int GALLERY_CODE = 1300;
    final int LOAD_CODY_DATABASE = 1301;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Intent intent = getIntent();
        User_id = intent.getExtras().getString("id");
        SaveButton = (Button)findViewById(R.id.usersavebtn);
        Get_Picture = (Button)findViewById(R.id.usergetpic);
        editText = (EditText)findViewById(R.id.Content_Edit);
        title_edit = (EditText)findViewById(R.id.subject);
        textView = (TextView)findViewById(R.id.textview);
        imageView = (ImageView)findViewById(R.id.userimageview);

        textView.setText("");
        textView.setText(User_id);

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Save_shows();
            }
        });
        Get_Picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Gallery_Intent = new Intent(Intent.ACTION_PICK);
                Gallery_Intent.setData(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                Gallery_Intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                Gallery_Intent.setType("image/*");
                startActivityForResult(Gallery_Intent, GALLERY_CODE);
            }
        });
    }

    public void Save_shows(){
        if(imageView.getDrawable() == null){
            CustomDialog customDialog = new CustomDialog(Sub2Activity.this);
            customDialog.callFunction("이미지를 추가해주세요");
        }
        else{
            if(!selectedImageUri.toString().isEmpty()){
                try {
                    String check = new SubOkHttp().execute(url, make_json(User_id, title_edit.getText().toString(),selectedImageUri.toString(), editText.getText().toString()).toString()).get();
                    if(check.equals("yes")){
                        onBackPressed();
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    // --------------------------이미지 갤러리에서 선택 결과 ----------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    selectedImageUri = data.getData();
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
                    imageView.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public JSONObject make_json(String id, String title, String image, String contents){
        JSONObject json = new JSONObject();
        JSONObject jsons = new JSONObject();
        try {
            json.put("id", id);
            json.put("title", title);
            json.put("image", image);
            json.put("contents", contents);

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
