package com.example.android.newsproject;

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
    //Tag for the log messages
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    static final String RESPONSE = "response";
    static final String RESULTS = "results";
    static final String SECTION_NAME = "sectionName";
    static final String WEB_PUBLICATION_DATE = "webPublicationDate";
    static final String WEB_TITLE = "webTitle";
    static final String WEB_URL = "webUrl";
    static final String TAGS = "tags";
    private QueryUtils() {
    }

    // Query the Guardian data and return a list of News objects.
    public static List<News> fetchNewsData(String requestUrl) throws InterruptedException {

        //Obtain url
        URL url = returnUrl(requestUrl);
        //Perform HTTP request to URL and receive a JSON response
        String jsonResponse = null;
        try {
            //Try to create a HTTP request with the request URL by makeHttpRequest
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            //Print the error message into log
            Log.e(LOG_TAG, "HTTP request failed.", e);
        }
        // Extract relevant fields from the JSON response and create a list of News
        List<News> news = extractFeatureFromJson(jsonResponse);
        // Return the list of news
        return news;
    }
    private static URL returnUrl(String stringUrl) {

        URL url = null;
        try {
            // Try to create an URL from String
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            //Print the error message into log
            Log.e(LOG_TAG, "URL building problem.", e);
        }
        return url;
    }
     //Make an HTTP request to the given URL and return a String as the response.
    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";
        // If the URL is null then return early.
        if (url == null) {
            return jsonResponse;
        }
        // Initialize variables for the HTTP connection and for the InputStream
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            // Try to establish a HTTP connection with the request URL and set up the properties of the request
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            //Read the Input Stream and parse the response.
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                // If the response failed, print it to the Log
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            // If the connection was not established, print it to the log
            Log.e(LOG_TAG, "Connection was not established. Problem retrieving JSON News results.", e);
        } finally {
            // Disconnect the HTTP connection
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            // Close the Input Stream
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    // Convert the InputStream into a String which contains the whole JSON response from the server.
    private static String readFromStream(InputStream inputStream) throws IOException {

        // Create a new StringBuilder
        StringBuilder output = new StringBuilder();
        //Create an InputStreamReader from it and a BufferedReader from the InputStreamReader
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
    //Return list of {@link News} objects that has been built up from parsing the given JSON response.
    private static List<News> extractFeatureFromJson(String newsJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }
        List<News> newsList = new ArrayList<>();
        // Try to parse the JSON response string.
        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonNewsResponse = new JSONObject(newsJSON);
            // Extract the JSONObject associated with the key "response",
            JSONObject responseJsonNews = baseJsonNewsResponse.getJSONObject(RESPONSE);
            // Extract the JSONArray associated with the key "results"
            JSONArray newsArray = responseJsonNews.getJSONArray(RESULTS);
            // For each news in the JsonNewsArray create an {@link News} object
            for (int i = 0; i < newsArray.length(); i++) {
                // Get a single news article at position i within the list of news
                JSONObject currentNews = newsArray.getJSONObject(i);
                // Extract the section name for the key "sectionName"
                String newsSection = currentNews.getString(SECTION_NAME);
                // Check if newsDate exist and than extract the date for the key "webPublicationDate"
                String newsDate = "";
                if (currentNews.has(WEB_PUBLICATION_DATE)) {
                    newsDate = currentNews.getString(WEB_PUBLICATION_DATE);
                }
                // Extract the article name for the key "webTitle"
                String newsTitle = currentNews.getString(WEB_TITLE);
                // Extract the value for the key "webUrl"
                String newsUrl = currentNews.getString(WEB_URL);
                //Extract the JSONArray associated with the key "tags",
                JSONArray currentNewsAuthorArray = currentNews.getJSONArray(TAGS);
                String newsAuthor = "";
                //Check if "tags" array contains data
                int tagsLenght = currentNewsAuthorArray.length();
                if (tagsLenght == 1) {
                    // Create a JSONObject for author
                    JSONObject currentNewsAuthor = currentNewsAuthorArray.getJSONObject(0);
                    String newsAuthor1 = currentNewsAuthor.getString(WEB_TITLE);
                    newsAuthor = "written by: " + newsAuthor1;
                }
                // Create a new News object with the title, category, author, date, url ,
                // from the JSON response.
                News newNews = new News(newsTitle, newsSection, newsAuthor, newsDate, newsUrl);
                // Add the new {@link News} to the list of News.
                newsList.add(newNews);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            Log.e("QueryUtils", "JSON results parsing problem.");
        }
        // Return the list of earthquakes
        return newsList;
    }
}