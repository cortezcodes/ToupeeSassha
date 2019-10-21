package com.example.android.toupeesassha;

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
 * Helper methods related to requesting and receiving news articles from the Guardian
 */
public final class QueryUtility {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtility.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a QueryUtility object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtility (and an object instance of QueryUtility is
     * not needed).
     */
    private QueryUtility(){}

    /**
     * Query the the Guardian dataset and return a list of NewsArticle objects.
     * @param requestUrl String that represent the URL request
     * @return List of NewsArticles
     */
    public static List<NewsArticle> fetchNewsArticleData(String requestUrl){
        URL url = createUrl(requestUrl);

        //Perfom HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e){
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        //Extract relevant fields from the JSON response and create a list of NewsArticles
        List<NewsArticle> articles = extractFeatureFromJson(jsonResponse);

        return articles;
    }

    /**
     * Return new URL object from the given string
     * @param stringUrl String representation of a URL
     * @return URL object
     */
    private static URL createUrl(String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        } catch (MalformedURLException e){
            Log.e(LOG_TAG, "Problem building the URL", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as response.
     * @param url URL object to create a network connection
     * @return String representation of a JSON response
     * @throws IOException
     */
    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";

        //If the URL is null then return early
        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(1000 /*milliseconds*/);
            urlConnection.setConnectTimeout(15000 /* ms */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            /*
            If the request was successful (response code 200) then read the input stream and
            parse the response.
             */
            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream!= null){
                /*
                Closing the input stream could throw an IOException, which is why
                the makeHttpRequest(URL url) method signature specifies that an
                IOException could be thrown.
                 */
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the InputStream into a String which contains the whole JSON
     * response
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of NewsArticle objects that have been built up from parsing the
     * given JSON response.
     * @param newsJSON String representation of a JSON response
     * @return List of newsArticles
     */
    private static List<NewsArticle> extractFeatureFromJson(String newsJSON){
        //If the JSON string is empty or null, then return early/
        if(TextUtils.isEmpty(newsJSON)){
            return null;
        }

        //Create an empty ArrayList that we can start adding NewsArticles to
        List<NewsArticle> articles = new ArrayList<>();

        /*
        Try to parse the JSON response string. If there's a problem with the way the JSON is
        formatted, a JSONException exception object will be thrown. Catch the exception so the
        app doesn't crash, and print the error message to the logs
         */
        try{
            //Create a JSONObject from the JSON response String
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            //Create A JSON Object from the JSON "Response" key which
            //contains the array of articles
            JSONObject responsJsonObj = baseJsonResponse.getJSONObject("response");

            /*
            Extract the JSONArray associated with the key called "results", which represents a list
            of features (or articles).
             */
            JSONArray articleArray = responsJsonObj.getJSONArray("results");

            //For each article in the articleArray, create a NewsArticle object
            for(int i = 0; i < articleArray.length(); i++){
                //Get a single article at position i within the list of articles
                JSONObject currentArticle = articleArray.getJSONObject(i);

                //Get the author from the tags array
                JSONArray tagsArray = currentArticle.getJSONArray("tags");
                String author = null;
                //loop through the author tags to get each author and add them to the string
                for(int k = 0; k< tagsArray.length(); k++){
                    JSONObject currentAuthor = tagsArray.getJSONObject(k);
                    if(author != null){
                        author = author + ", " + currentAuthor.getString("webTitle");
                    } else{
                       author = currentAuthor.getString("webTitle");
                    }

                }

                //Extract the value for each Title, Section, URL, and PublishDate keys
                String articleTitle = currentArticle.getString("webTitle");
                String section = currentArticle.getString("sectionName");
                String url = currentArticle.getString("webUrl");
                String publishDate = currentArticle.getString("webPublicationDate");


                //Create a new NewsArticle object with the data retrieved from the JSON response
                articles.add(new NewsArticle(articleTitle, url, publishDate, section, author));
            }
        } catch (JSONException e){
            /*
            If an error is thrown when executing any of the above statements in the "try" block,
            catch the exception here, so the app doesn't crash. print a log message with the
            from the exception.
             */
            Log.e("QueryUtility", "Problem parsing the JSON results", e);
        }
        return articles;
    }
}

