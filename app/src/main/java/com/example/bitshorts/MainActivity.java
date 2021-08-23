package com.example.bitshorts;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<NewsCard>> {

    ListView newsListView;
    private NewsCardAdapter mAdapter;
    boolean isConnected;
    private TextView mEmptyStateTextView;
    private View circleProgressBar;
    private SearchView mSearchViewField;
    Button mSearchButton;
    private static final int NEWS_LOADER_ID = 1;
    private  String mUrlRequestNewsArticles = "";

    private static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Declaration and initialization ConnectivityManager for checking internet connection
        final ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        /*
         * At the beginning check the connection with internet and save result to (boolean) variable isConnected
         * Checking if network is available
         * If TRUE - work with LoaderManager
         * If FALSE - hide loading spinner and show emptyStateTextView
         */
        checkConnection(cm);


        // Find a reference to the {@link ListView} in the layout
        newsListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of books as input
        mAdapter = new NewsCardAdapter(this, new ArrayList<NewsCard>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(mAdapter);

        // Find a reference to the empty view
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);


        // Circle progress
        circleProgressBar = findViewById(R.id.loading_spinner);

        // Search button
        mSearchButton = (Button) findViewById(R.id.search_button);

        // Search field
        mSearchViewField = (SearchView) findViewById(R.id.search_view_field);
        mSearchViewField.onActionViewExpanded();
        mSearchViewField.setIconified(true);
        mSearchViewField.setQueryHint("Search topics");


        if (isConnected) {
            mEmptyStateTextView.setVisibility(View.GONE);
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader.
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            // Progress bar mapping
            Log.i(LOG_TAG, "INTERNET connection status: " + String.valueOf(isConnected) + ". Sorry dude, no internet - no data :(");


            circleProgressBar.setVisibility(View.GONE);
            // Set empty state text to display "No internet connection."
            mEmptyStateTextView.setText("NO INTERNET CONNECTION");
        }


        // Set an item click listener on the Search Button, which sends a request to
        // Google Books API based on value from Search View
        mSearchButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                // Check connection status
                checkConnection(cm);

                if (isConnected) {
                    // Update URL and restart loader to displaying new result of searching
                    updateQueryUrl(mSearchViewField.getQuery().toString());
                    restartLoader();

                    Log.i(LOG_TAG, "Search value: " + mSearchViewField.getQuery().toString());
                } else {
                    // Clear the adapter of previous book data
                    mAdapter.clear();
                    // Set mEmptyStateTextView visible
                    mEmptyStateTextView.setVisibility(View.VISIBLE);
                    // ...and display message: "No internet connection."
                    mEmptyStateTextView.setText(R.string.no_internet_connection);
                }

            }

        });





    }

    @Override
    public Loader<List<NewsCard>> onCreateLoader(int id, Bundle args) {
        Log.i("There is no instance", ": Created new one loader at the beginning!");
        // Create a new loader for the given URL
        updateQueryUrl(mSearchViewField.getQuery().toString());
        return new NewsLoader(this,mUrlRequestNewsArticles);

    }

    @Override
    public void onLoadFinished(Loader<List<NewsCard>> loader, List<NewsCard> newsCards) {

        // Progress bar mapping
        View circleProgressBar = findViewById(R.id.loading_spinner);
        circleProgressBar.setVisibility(View.GONE);

        // Set empty state text to display "No books found."
        mEmptyStateTextView.setText("No News found");
        Log.i(LOG_TAG, ": Books has been moved to adapter's data set. This will trigger the ListView to update!");

        // Clear the adapter of previous book data
        mAdapter.clear();

        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (newsCards != null && !newsCards.isEmpty()) {
            mAdapter.addAll(newsCards);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<NewsCard>> loader) {
        mAdapter.clear();
        Log.i(LOG_TAG, ": Loader reset, so we can clear out our existing data!");
    }

    public void restartLoader() {
        mEmptyStateTextView.setVisibility(View.GONE);
        circleProgressBar.setVisibility(View.VISIBLE);
        getLoaderManager().restartLoader(NEWS_LOADER_ID, null, MainActivity.this);
    }

    public void checkConnection(ConnectivityManager connectivityManager) {
        // Status of internet connection
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting()) {
            isConnected = true;

            Log.i(LOG_TAG, "INTERNET connection status: " + String.valueOf(isConnected) + ". It's time to play with LoaderManager :)");

        } else {
            isConnected = false;

        }
    }

    private String updateQueryUrl(String searchValue) {

        if (searchValue.contains(" ")) {
            searchValue = searchValue.replace(" ", "+");
        }



        StringBuilder sb = new StringBuilder();
        sb.append("https://content.guardianapis.com/search?page=2&q=").append(searchValue).append("&api-key=d6855595-b73e-4307-9425-809d1acb81bb");
        mUrlRequestNewsArticles = sb.toString();
        return mUrlRequestNewsArticles;
    }


}