package com.example.android.newspaper;

import android.graphics.drawable.Drawable;

/**
 * Created by rahul saini on 13-Dec-17.
 */

public class newsData {
    private String title;
    private String description;
    private String url;
    private Drawable urlToImage;
    private String time;

    public newsData(String title, String description, String url, Drawable urlToImage, String time) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public Drawable getUrlToImage() {
        return urlToImage;
    }

    public String getTime() {
        return timeFormat(time);
    }

    private static String timeFormat(String time) {
        String r = time.split("T")[0] + "  " + time.split("T")[1].split("\\+")[0];
        return r;
    }
}
