package com.example.bitshorts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewsCardAdapter extends ArrayAdapter<NewsCard> {

    //Constructor of NewsCardAdapter
    public NewsCardAdapter(Context context, List<NewsCard> newsCards){
        super(context,0 , newsCards);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView = convertView;
        if (listView == null){
            listView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        NewsCard currentNewsCard = getItem(position);

        assert currentNewsCard != null;

        TextView titleTextView = (TextView)listView.findViewById(R.id.title);
        TextView authorTextView = (TextView)listView.findViewById(R.id.author);
        TextView dateTextView = (TextView)listView.findViewById(R.id.date);
        TextView sourceTextView = (TextView)listView.findViewById(R.id.source);


        titleTextView.setText(currentNewsCard.getTitle());
        authorTextView.setText(currentNewsCard.getAuthor());
        dateTextView.setText(currentNewsCard.getDate());
        sourceTextView.setText(currentNewsCard.getSource());


        return listView;
    }


}
