/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.newspaper;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<newsData>> {

    private LinearLayout mEmptylayout;
    private ImageView mWel;
    private TextView mWel_t;
    private MyAdapter mAdapter;
    private static final int LOADER_ID = 1;
    private android.support.v7.app.ActionBar actionBar;
    private TextToSpeech textToSpeech;
    private static final float PITCH_VOICE = (float) 1.2;

    public static final String LOG_TAG = MainActivity.class.getName();
    private static final String urlnews = "https://newsapi.org/v2/top-headlines?sources=google-news-in&apiKey=1f91dce654074d74a2cf028eacc654b9";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBarShow(false);

        ListView listview = (ListView) findViewById(R.id.main_list);
        mEmptylayout = (LinearLayout) findViewById(R.id.welcomeScreen);
        listview.setEmptyView(mEmptylayout);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

//        Adapter operations
        mAdapter = new MyAdapter(this, new ArrayList<newsData>());
        listview.setAdapter(mAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final newsData currentnews = mAdapter.getItem(i);
                String description_of_news = currentnews.getDescription();
                speakWord(description_of_news);
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final newsData currentnews = mAdapter.getItem(i);
                Uri earthquakeuri = Uri.parse(currentnews.getUrl());
                Intent webintent = new Intent(Intent.ACTION_VIEW, earthquakeuri);
                startActivity(webintent);
                return true;
            }
        });

//        check for TextToSpeech functionality
        Intent checkTTS = new Intent();
        checkTTS.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTS,0);

//        loading data and show using loader
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(LOADER_ID, null, this);
        } else {
           actionBarShow(true);
            noConnection();
        }
    }

    @Override
    public Loader<List<newsData>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, urlnews);
    }

    @Override
    public void onLoadFinished(Loader<List<newsData>> loader, List<newsData> data) {
        actionBarShow(true);
        noData();
        mAdapter.clear();
        if (data != null && !data.isEmpty()) {
            showPop();
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<newsData>> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingIntent);
            return true;
        }
        if (id == R.id.aboutus){
            Intent aboutIntent = new Intent(this, aboutus.class);
            startActivity(aboutIntent);
            return true;
        }
        if (id == R.id.help){
            Intent email = new Intent(Intent.ACTION_SENDTO);
            email.setData(Uri.fromParts("mailto","rahulsaini1499@gmail.com",null));
            email.putExtra(Intent.EXTRA_SUBJECT,"Help me");
            email.putExtra(Intent.EXTRA_TEXT,"I want help for ");
            startActivity(email);
        }
        return super.onOptionsItemSelected(item);
    }

//    My Helping Functions
    public void showPop() {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.help_popup, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setPositiveButton("OK", null);
        alert.setView(v);
        alert.show();
    }

    public void noData(){
        mWel = (ImageView) findViewById(R.id.welcomeLogo);
        mWel_t = (TextView) findViewById(R.id.welcomeText);
        mWel.setImageResource(R.mipmap.ic_no_internet);
        mWel_t.setTextColor(Color.parseColor("#f44336"));
        mWel_t.setTextSize(15);
        mWel_t.setText(R.string.no_data);
    }

    public void noConnection(){
        mWel = (ImageView) findViewById(R.id.welcomeLogo);
        mWel_t = (TextView) findViewById(R.id.welcomeText);
        mWel.setImageResource(R.mipmap.ic_no_internet);
        mWel_t.setText(R.string.no_net);
        mWel_t.setTextColor(Color.parseColor("#f44336"));
        mWel_t.setTextSize(15);
    }

    public void actionBarShow(boolean t){
        if (t){
            actionBar = getSupportActionBar();
            actionBar.show();
        }else {
            actionBar = getSupportActionBar();
            actionBar.hide();
        }
    }

    public void speakWord(String s){
        textToSpeech = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.setPitch(PITCH_VOICE);
                    textToSpeech.setLanguage(Locale.US);
                } else if (status == TextToSpeech.ERROR) {
                    Toast.makeText(MainActivity.this, "Sorry! Text to Speech failed...", Toast.LENGTH_LONG).show();
                }
            }
        });
        textToSpeech.stop();
        textToSpeech.speak(s, TextToSpeech.QUEUE_ADD, null);
    }
}

