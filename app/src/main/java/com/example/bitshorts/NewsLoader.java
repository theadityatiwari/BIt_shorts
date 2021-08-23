package com.example.bitshorts;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<NewsCard>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = NewsLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructs a new {@link NewsLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;

        Log.i(LOG_TAG, ": Loaded!");
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.i("On start loading", ": Force loaded!");
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<NewsCard> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of books.
        List<NewsCard> newsCards = QueryUtils.fetchNewsData(mUrl);
        Log.i(LOG_TAG, ": Loaded in background!");
        return newsCards;

    }
}
