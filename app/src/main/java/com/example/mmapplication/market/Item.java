package com.example.mmapplication.market;

import android.util.Log;

public class Item {
    int ID;
    String User_Name;
    String User_Picture;  /*************** 이거 String 바꿔야함 ************/
    String image;
    String title;
    String content;
    String day;

    int getID (){
        return this.ID;
    }
    String getUser_Name(){
        return this.User_Name;
    }
    String getImage() {
        return this.image;
    }
    String getTitle() {
        return this.title;
    }
    String getContent() { return  this.content;}
    String getDay() { return  this.day;}
    Item(int id, String title, String image, String user_name,String content,String day) {
        this.ID = id;
        this.User_Name = user_name;
        int idx = image.indexOf("_");
        String images = image.substring(idx+1);
        this.image = images;
        this.title = title;
        this.content = content;
        this.day = day;
    }
}

