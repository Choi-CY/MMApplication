package com.example.mmapplication.Chatbot;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class TTS{
    private static TextToSpeech mTTS;
    TTS(Context context){
        mTTS = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                //onInitListener.onInit(status); -- it raises up an error;
                if (status == TextToSpeech.SUCCESS) {
                    mTTS.setLanguage(Locale.KOREA);
                }

            }
        });
    }
    public static void onDestroy() {
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
    }

    public void doSpeak(final String zWord, final ChatAdapter chatAdapter, boolean check) {

        chatAdapter.add(zWord,0);
        chatAdapter.notifyDataSetChanged();
        Log.e("acacac ->","3");
        if(check){
            mTTS.setLanguage(Locale.KOREA);
            mTTS.speak(zWord, TextToSpeech.QUEUE_FLUSH, null);
        }else{
            mTTS.setLanguage(Locale.ENGLISH);
            mTTS.speak(zWord, TextToSpeech.QUEUE_FLUSH, null);
        }

    }

}
