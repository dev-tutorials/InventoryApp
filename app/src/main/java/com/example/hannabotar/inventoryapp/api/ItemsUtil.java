package com.example.hannabotar.inventoryapp.api;

import android.text.TextUtils;
import android.util.Log;

import com.example.hannabotar.inventoryapp.model.InventoryItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hanna on 7/17/2018.
 */

public class ItemsUtil {

    private static final String LOG_TAG = ItemsUtil.class.getSimpleName();

    public static List<InventoryItem> fetchItems(String urlString) {
        URL url = createUrl(urlString);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        List<InventoryItem> itemList = extractItems(jsonResponse);

        return itemList;
    }

    public static String postItems(String urlString, List<InventoryItem> itemList) {
        URL url = createUrl(urlString);

        return makePostHttpRequest(url, itemList);
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the inventory JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String makePostHttpRequest(URL url, List<InventoryItem> itemList) {
        try {
            // Create the urlConnection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            urlConnection.setRequestProperty("Content-Type", "application/json");

            urlConnection.setRequestMethod("POST");

            // Send the post body
            if (itemList != null) {
                String jsonToPost = getJsonFromList(itemList);
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(jsonToPost);
                writer.flush();
            }

            int statusCode = urlConnection.getResponseCode();

            if (statusCode == 200) {

                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

                String response = readFromStream(inputStream);
                return response;
            } else {
                return "Could not post data";
            }
        } catch (Exception e) {
            return "Could not post data";
        }
    }

    private static String getJsonFromList(List<InventoryItem> itemList) {

        String jsonString = null;
        try {
            JSONArray jsonArray = new JSONArray();
            for (InventoryItem inventoryItem : itemList) {
                JSONObject jsonObj = new JSONObject();

                jsonObj.put("name", inventoryItem.getName());
                jsonObj.put("serial_no", inventoryItem.getSerialNo());
                jsonObj.put("condition", inventoryItem.getCondition());
                jsonObj.put("description", inventoryItem.getDescription());
                jsonArray.put(jsonObj);
            }
            jsonString = jsonArray.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonString;
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

    private static List<InventoryItem> extractItems(String jsonResponse) {

        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        List<InventoryItem> itemList = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray results = root.getJSONArray("items");

//            JSONArray results = new JSONArray(jsonResponse);
            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                String itemName = result.getString("name");
                String serialNo = result.getString("serial_no");
                Integer condition = result.getInt("condition");
                String description = result.optString("description");

                itemList.add(new InventoryItem(itemName, serialNo, condition, description));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the inventory JSON results", e);
        }

        return itemList;
    }


}
