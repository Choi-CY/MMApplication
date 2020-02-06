package com.example.mmapplication.market;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mmapplication.R;

public class ChecklistActivity extends AppCompatActivity {
    String User_id,Image,Content,Day,Title;
    TextView title,id,day,content;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        Intent intent = getIntent();

        User_id = intent.getExtras().getString("user_id");
        Title = intent.getExtras().getString("title");
        Day = intent.getExtras().getString("day");
        Image = intent.getExtras().getString("image");
        Content = intent.getExtras().getString("content");

        title = (TextView)findViewById(R.id.c_title);
        id = (TextView)findViewById(R.id.c_userid);
        day = (TextView)findViewById(R.id.c_post_date);
        content = (TextView)findViewById(R.id.c_textview);
        image = (ImageView)findViewById(R.id.c_content_image);

        String result_day = Day.substring(0,10);

        title.setText(Title);
        id.setText(User_id);
        day.setText(result_day);
        content.setText(Content);
        image.setImageURI(Uri.parse(Image));
    }
}
