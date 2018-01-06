package com.example.android.newspaper;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rahul saini on 13-Dec-17.
 */

public class MyAdapter extends ArrayAdapter<newsData> {

    public MyAdapter(@NonNull Context context, List<newsData> resource) {
        super(context, 0, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View ListItemview = convertView;
        if (ListItemview == null) {
            ListItemview = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        newsData data = getItem(position);
        TextView heading = (TextView) ListItemview.findViewById(R.id.heading);
        TextView time = (TextView) ListItemview.findViewById(R.id.time);
        ImageView image = (ImageView) ListItemview.findViewById(R.id.news_image);

        heading.setText(data.getTitle());
        time.setText(data.getTime());
        image.setImageDrawable(data.getUrlToImage());

        return ListItemview;
    }
}
