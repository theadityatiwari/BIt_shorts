package com.example.bitshorts;

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

public class QueryUtils {

    //tag for log messages
    public static final String LOG_TAG = QueryUtils.class.getName();


    //constructor of QueryUtils class
    public QueryUtils() {

    }

    private static URL createUrl(String requestUrl){
        URL url = null;
        try {
            url  = new URL(requestUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    private static String makeHttpRequest(URL requesUrl) throws IOException{
        String jsonResponse = "";
        if(requesUrl==null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            //making http request
            urlConnection = (HttpURLConnection) requesUrl.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            //Connection establish
            urlConnection.connect();
            Log.i(LOG_TAG,"Connection establish");
            Log.i(LOG_TAG, String.valueOf(urlConnection.getResponseCode()));

            //Getting input Stream
            //Checking weather response code is 200 or not

            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG,"Problem in making httpRequest",e);
        }finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }if(inputStream != null){
                inputStream.close();
            }
        }

        return jsonResponse;

    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line!=null){
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }

    private static List<NewsCard> extractNewsData(String jsonResponse){
        //Return early if jsonResponse is empty
        if(TextUtils.isEmpty(jsonResponse)){
            return null;
        }

        // Create an empty ArrayList that we can start adding NewsCards to listView
        List<NewsCard> newsCards = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(jsonResponse);
            Log.i(LOG_TAG,String.valueOf(jsonResponse));
            JSONObject response = root.getJSONObject("response");
            Log.i(LOG_TAG,String.valueOf(response));
            JSONArray articles = response.getJSONArray("results");

            for (int i=0;i <articles.length();i++){
                JSONObject currentArticle = articles.getJSONObject(i);

                String title = currentArticle.getString("webTitle");
                String author = "N/A";
                String coverImgUrl = "https://www.fool.com.au/wp-content/uploads/2021/04/dogecoin-16_9.jpg";
                String date = currentArticle.getString("webPublicationDate");
                String source = currentArticle.getString("sectionName");
                NewsCard newsCard = new NewsCard(coverImgUrl,title,author,date,source);
                Log.println(Log.INFO, LOG_TAG, String.valueOf(newsCard));
                newsCards.add(newsCard);
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the Books JSON results", e);
        }
        //return the list of articles
        return newsCards;
    }

    // above all are method declaration this method include all method calls
    public static List<NewsCard> fetchNewsData(String requestUrl){

        final int SLEEP_TIME_MILLIS = 500;

        // This action with sleeping is required for displaying circle progress bar
        try {
            Thread.sleep(SLEEP_TIME_MILLIS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<NewsCard> newsCards = extractNewsData(jsonResponse);

        // return the list of news articles
        return newsCards;
    }
}
