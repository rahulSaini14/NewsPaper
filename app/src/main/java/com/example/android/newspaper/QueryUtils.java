package com.example.android.newspaper;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link newsData} objects that has been built up from
     * parsing a JSON response.
     */
    private static URL changetourl(String stringjasonresponse) {
        URL url = null;
        try {
            url = new URL(stringjasonresponse);

        } catch (MalformedURLException m) {
            Log.e("QueryUtils", "changetourl: ", m);
            return null;
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("QueryUtils", "Error in makehttprequest function getresponsecode " + urlConnection.getResponseCode() + jsonResponse);
            }
        } catch (IOException e) {
            Log.e("QueryUtils", "Error in makehttprequest function", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<newsData> extractjsonresponse(String jsonresponse) {

        if (TextUtils.isEmpty(jsonresponse)) {
            return null;
        }
        List<newsData> news = new ArrayList<>();
        try {
            JSONObject baseJsonobject = new JSONObject(jsonresponse);
            JSONArray newsarray = baseJsonobject.getJSONArray("articles");
            for (int i = 0; i < newsarray.length(); i++) {
                JSONObject newsarrayJSONObject = newsarray.getJSONObject(i);
                String title = newsarrayJSONObject.getString("title");
                String description = newsarrayJSONObject.getString("description");
                String url = newsarrayJSONObject.getString("url");
                String imageUrl = newsarrayJSONObject.getString("urlToImage");
                String time = newsarrayJSONObject.getString("publishedAt");
                //for image
                Drawable drawable = toImage(imageUrl);
                newsData newscontent = new newsData(title, description, url, drawable, time);
                news.add(newscontent);
            }
        } catch (JSONException e) {
            Log.e("MainActivity", "Problem parsing the news JSON results", e);
        }

        return news;
    }

    public static List<newsData> fetchnewsdata(String urljson) {
        URL url = changetourl(urljson);
        String urljsonstring = null;
        try {
            urljsonstring = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("QueryUtils", "Problem making the http request", e);
        }
        List<newsData> returnstring = extractjsonresponse(urljsonstring);
        return returnstring;
    }


    private static Drawable toImage(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
}
