package com.example.anhdinh.newsapp;

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
    // Tag for the log message:
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    // Fetch data to combine url creation, http request
    public static List<Article> fetchArticleData(String requestUrl) {
        // Create a URL
        URL url = createUrl(requestUrl);

        // Make http request
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Article> articles = extractFeatureFromJson(jsonResponse);

        return articles;
    }

    // Create method to convert String URL to Object URL
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with create URL: " + e);
        }
        return url;
    }

    // Method to request Http to return a String response
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If Url is null, return early
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(1000 /* milliseconds*/);
            urlConnection.setConnectTimeout(15000 /*milliseconds*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the article JSON results. ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    // Method to read from inputStream:
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

    // Method to extract JSON Response from server
    private static List<Article> extractFeatureFromJson(String stringJsonResponse) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(stringJsonResponse)) {
            return null;
        }

        // Create a list to contain features
        List<Article> articles = new ArrayList<>();

        try {
            // Get the whole JSON Response by putting stringJsonResponse
            JSONObject baseJsonResponse = new JSONObject(stringJsonResponse);

            JSONObject objectOfResponse = baseJsonResponse.getJSONObject("response");

            // Get the array with "result" feature
            JSONArray articleArray = objectOfResponse.getJSONArray("results");

            // use for loop to loop through array and store values into List
            for (int i = 0; i < articleArray.length(); i++) {
                // Locate the element of the array
                JSONObject currentArticle = articleArray.getJSONObject(i);
                String articleTitle = currentArticle.getString("webTitle");
                String articleSection = currentArticle.getString("sectionId");
                String articleDatePublished = currentArticle.getString("webPublicationDate");
                String articleUrl = currentArticle.getString("webUrl");
                String articleAuthorName;

                // Get the array that has author name
                JSONArray tagsArray = currentArticle.getJSONArray("tags");
                if (tagsArray.length() == 0) { // there's no tags for author's name
                    articles.add(new Article(articleTitle, articleSection, articleDatePublished, articleUrl));
                } else if (tagsArray.length() != 0) { // there's author's name
                    for (int u = 0; u < tagsArray.length(); u++) {
                        JSONObject objectOfTagsArray = tagsArray.getJSONObject(u);
                        articleAuthorName = objectOfTagsArray.getString("webTitle");
                        Log.v("QueryUtils", "extractFeatureFromJson: " + articleAuthorName);
                        articles.add(new Article(articleTitle, articleSection, articleDatePublished, articleAuthorName, articleUrl));
                    }
                }

            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error with Parsing stringJson", e);
        }
        return articles;
    }

}
