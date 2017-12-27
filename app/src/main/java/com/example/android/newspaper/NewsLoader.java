package com.example.android.newspaper;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by rahul saini on 23-Dec-17.
 */

public class NewsLoader extends AsyncTaskLoader<List<newsData>> {

    private static final String LOG_TAG = NewsLoader.class.getName();
    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }


    @Override
    protected void onStartLoading() {
        onForceLoad();
    }

    @Override
    public List<newsData> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<newsData> list = QueryUtils.fetchnewsdata(mUrl);
        return list;
    }
}
