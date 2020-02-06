package com.example.mmapplication.Chatbot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mmapplication.R;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import static android.media.CamcorderProfile.get;

public class ChatAdapter extends BaseAdapter {


    public class ListCount{
        String msg;
        int type;
        ListCount(String msg,int type){
            this.msg = msg;
            this.type = type;
        }
    }

    private ArrayList<ListCount> m_List;
    public ChatAdapter(){
        m_List = new ArrayList<>();
    }
    public void add(String msg, int type){
        m_List.add(new ListCount(msg,type));

    }

    // 외부에서 아이템 삭제 요청 시 사용
    public void remove(int _position) {
        m_List.remove(_position);
    }


    @Override
    public int getCount() {
        return m_List.size();
    }

    @Override
    public Object getItem(int position) {
        return m_List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        Log.e("positionc",Integer.toString(position));


        TextView text = null;
        CustomHolder holder = null;
        LinearLayout layout = null;
        LinearLayout background = null;
        View viewRight = null;
        View viewLeft = null;
        ImageView bot_images = null;
        ImageView contents_image = null;
        if(convertView == null){
            //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_chatitem,parent,false);
            background = (LinearLayout)convertView.findViewById(R.id.backgrounds);
            layout = (LinearLayout)convertView.findViewById(R.id.layout_c);
            text = (TextView)convertView.findViewById(R.id.text_c);
            viewLeft = (View)convertView.findViewById(R.id.imageViewleft_c);
            viewRight = (View)convertView.findViewById(R.id.imageViewright_c);
            bot_images = (ImageView)convertView.findViewById(R.id.chatbot_image);
            contents_image = (ImageView)convertView.findViewById(R.id.using_image);

            holder = new CustomHolder();
            holder.m_TextView = text;
            holder.layout = layout;
            holder.background_layout = background;
            holder.viewLeft = viewLeft;
            holder.viewRight = viewRight;
            holder.bot_image = bot_images;
            holder.contents_image = contents_image;
            convertView.setTag(holder);
        }
        else{
            holder = (CustomHolder)convertView.getTag();
            text = holder.m_TextView;
            layout = holder.layout;
            background = holder.background_layout;
            viewLeft = holder.viewLeft;
            viewRight = holder.viewRight;
            bot_images = holder.bot_image;
            contents_image = holder.contents_image;
        }


        // Text 등록
        if(m_List.get(position).type == 2 ){
            String data = m_List.get(position).msg;
            int index = 0;
            for(int i =1; i<data.length();i++){
                if(data.charAt(i) == '_'){
                    index = i;
                    break;
                }
            }
            String textdata = data.substring(1,index);
            text.setText(textdata);
            String data2 = data.substring(index+1);
            byte[] bytePlainOrg = Base64.decode(data2, 0);
            //byte[] 데이터  stream 데이터로 변환 후 bitmapFactory로 이미지 생성
            ByteArrayInputStream inStream = new ByteArrayInputStream(bytePlainOrg);
            Bitmap bm = BitmapFactory.decodeStream(inStream) ;
            contents_image.setImageBitmap(bm);
            background.setBackgroundResource(R.drawable.message_bot);
            layout.setGravity(Gravity.LEFT);
            bot_images.setVisibility(View.VISIBLE);
            contents_image.setVisibility(View.VISIBLE);
            viewRight.setVisibility(View.GONE);
            viewLeft.setVisibility(View.GONE);


        }
        else{
            text.setText(m_List.get(position).msg);
            if( m_List.get(position).type == 0 ) {
                background.setBackgroundResource(R.drawable.message_bot);
                layout.setGravity(Gravity.LEFT);
                bot_images.setVisibility(View.VISIBLE);
                contents_image.setVisibility(View.GONE);
                viewRight.setVisibility(View.GONE);
                viewLeft.setVisibility(View.GONE);
            }else if(m_List.get(position).type == 1){
                background.setBackgroundResource(R.drawable.message_me);
                layout.setGravity(Gravity.RIGHT);
                bot_images.setVisibility(View.GONE);
                contents_image.setVisibility(View.GONE);
                viewRight.setVisibility(View.GONE);
                viewLeft.setVisibility(View.GONE);
            }
        }

        return convertView;
    }
    public class CustomHolder {
        TextView m_TextView;
        LinearLayout layout;
        LinearLayout background_layout;
        View viewRight;
        View viewLeft;
        ImageView bot_image;
        ImageView contents_image;
    }
}
