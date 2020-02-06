package com.example.mmapplication.Chatbot;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

public class STTandTTS extends Activity {


    static Context con = null;
    String packageName;
    Boolean check = true;
    String url;
    String result_text = "대기중", answer_text = null;
    TTS tts;

    String User_id;

    public STTandTTS(Context context,String PackageName,String url,String id){
        con = context;
        packageName = PackageName;
        User_id = id;
        this.url = url;
        Log.e("contexts1",con.toString());
        tts = new TTS(context);
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


    public void inputVoice(final String language, final ChatAdapter chatAdapter){

        try {
            Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName);
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);
            final SpeechRecognizer mRecognizer = SpeechRecognizer.createSpeechRecognizer(con);
            mRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params){
                    if(language.equals("ko-KR")){
                        check = true;
                    }
                    else{
                        check = false;
                    }

                }

                @Override
                public void onBeginningOfSpeech() {}
                @Override
                public void onRmsChanged(float rmsdB) {}
                @Override
                public void onBufferReceived(byte[] buffer) {}
                @Override
                public void onEndOfSpeech() {}
                @Override
                public void onError(int error) {
                    mRecognizer.destroy();
                }

                @Override
                public void onResults(Bundle results) {

                    ArrayList<String> result = (ArrayList<String>) results.get(SpeechRecognizer.RESULTS_RECOGNITION);
                    try {
                        result_text = result.get(0);
                        chatAdapter.add(result.get(0),1);
                        chatAdapter.notifyDataSetChanged();
                        //new Chat_text(con).Update_user(result_text);

                        String lan = language.substring(0,5);


                        answer_text = new OkHttpAsync().execute(url, make_json(result.get(0),lan,User_id).toString()).get();
                        if(answer_text.charAt(0) == '_'){
                            chatAdapter.add(answer_text,2);
                            chatAdapter.notifyDataSetChanged();
                        }
                        else{
                            Log.e("acacac ->","2");
                            int en = 0;
                            int ko = 0;
                            int etc = 0;

                            char[] string = answer_text.toCharArray();

                            for (int j=0; j<string.length; j++) {
                                if (string[j]>='A' && string[j]<='z') {
                                    en++;
                                }
                                else if (string[j]>='\uAC00' && string[j]<='\uD7A3') {
                                    ko++;
                                }
                            }
                            if(ko>=en){
                                tts.doSpeak(answer_text,chatAdapter,true);
                            }
                            else{
                                tts.doSpeak(answer_text,chatAdapter,false);
                            }
                            Log.e("한글", Integer.toString(ko));
                            Log.e("영어", Integer.toString(en));

                        }

                        mRecognizer.destroy();

                    }catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onPartialResults(Bundle partialResults) {
                }

                @Override
                public void onEvent(int eventType, Bundle params) {
                }
            });

            mRecognizer.startListening(i);

        }catch (Exception e){
            Log.e("test : ",e.toString());
        }
    }
}
