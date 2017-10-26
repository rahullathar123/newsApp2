package com.example.rahul.newsapp;


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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class NUtils {

    private SimpleDateFormat  simpleDateFormat;
    Calendar calendar;
    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = NUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link NUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private NUtils() {
    }

    /**
     * Query the USGS dataset and return a list of {@link NewsData} objects.
     */
    public static List<NewsData> fetchNewsData(String requestUrl) throws JSONException {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        jsonResponse = makeHttpRequest(url);

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<NewsData> news = extractFromJson(jsonResponse);
        // Return the list of {@link Earthquake}s
        return news;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url)  {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
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
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream){
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = null;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (line != null) {
                output.append(line);
                try {
                    line = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link NewsData} objects that has been built up from
     * parsing the given JSON response.
     */
    public static List<NewsData> extractFromJson(String jsonNews) throws JSONException {
        String nameAuthor="";
        String sectionName="";
        String webTitle="";
        String webURL="";
        String date="";

        List<NewsData> newsList = new ArrayList<>();
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonNews)) {
            return null;
        }
        JSONObject jsonObject = new JSONObject(jsonNews);
        JSONObject response = jsonObject.getJSONObject("response");
        JSONArray resultResponse = response.getJSONArray("results");


        for (int i = 0; i < resultResponse.length(); i++) {
            JSONObject ob1 = resultResponse.getJSONObject(i);
            if(ob1.has("sectionName")) {
                sectionName = ob1.getString("sectionName");
            }
            if(ob1.has("webTitle")) {
                webTitle = ob1.getString("webTitle");
            }
            if(ob1.has("webUrl")) {
                webURL = ob1.getString("webUrl");
            }
            if(ob1.has("webPublicationDate")) {
                date = ob1.getString("webPublicationDate").replaceAll("T", "  #").replaceAll("Z", "");
            }
            if (ob1.has("tags")) {
                JSONArray tag = ob1.getJSONArray("tags");
                if (tag.length() > 0) {
                    JSONObject firstName = tag.getJSONObject(0);
                    nameAuthor = firstName.getString("id");
                }
            }
            NewsData news = new NewsData(sectionName, webTitle, webURL, date,nameAuthor);
            newsList.add(news);
        }

        return newsList;
    }
}

