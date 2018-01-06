package com.example.android.newspaper;

import android.content.Intent;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import static com.example.android.newspaper.MainActivity.N_descp;
import static com.example.android.newspaper.MainActivity.N_heading;
import static com.example.android.newspaper.MainActivity.N_img;
import static com.example.android.newspaper.MainActivity.N_time;

public class NewsContent extends AppCompatActivity {

    private TextView heading;
    private TextView description;
    private TextView time;
    private ImageView img;
    private String url;
    private ImageButton listen;
    private ImageButton web;
    private TextToSpeech textToSpeech;
    private static final float PITCH_VOICE = (float) 1.2;
    private static final float speech_rate = (float) 0.8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);

        //        check for TextToSpeech functionality
        Intent checkTTS = new Intent();
        checkTTS.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTS,0);

        heading = (TextView) findViewById(R.id.N_heading);
        description = (TextView) findViewById(R.id.N_description);
        time = (TextView) findViewById(R.id.N_time);
        img = (ImageView) findViewById(R.id.N_image);
        web = (ImageButton) findViewById(R.id.N_website);
        listen = (ImageButton) findViewById(R.id.N_listen);

        heading.setText(N_heading);
        description.setText(N_descp);
        time.setText(N_time);
        img.setImageDrawable(N_img);

        //        initialise TextToSpeech
        textToSpeech = new TextToSpeech(NewsContent.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.setPitch(PITCH_VOICE);
                    textToSpeech.setSpeechRate(speech_rate);
                    textToSpeech.setLanguage(Locale.US);
                } else if (status == TextToSpeech.ERROR) {
                    Toast.makeText(NewsContent.this, "Sorry! Text to Speech failed...", Toast.LENGTH_LONG).show();
                }
            }
        });

        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri urin = Uri.parse(url);
                Intent webintent = new Intent(Intent.ACTION_VIEW, urin);
                startActivity(webintent);
            }
        });

        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakWord(N_descp);
            }
        });

    }

    public void speakWord(String s){
        textToSpeech.stop();
        textToSpeech.speak(s, TextToSpeech.QUEUE_ADD, null);
    }

}
