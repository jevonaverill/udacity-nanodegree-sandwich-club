package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String LOG_TAG = JsonUtils.class.getSimpleName();
    private static final String NAME = "name";
    private static final String MAIN_NAME = "mainName";
    private static final String ALSO_KNOWN_AS = "alsoKnownAs";
    private static final String PLACE_OF_ORIGIN = "placeOfOrigin";
    private static final String DESCRIPTION = "description";
    private static final String IMAGE = "image";
    private static final String INGREDIENTS = "ingredients";

    public static Sandwich parseSandwichJson(String json) {

        JSONObject jsonObject;
        JSONObject jsonObjectName;
        String mainName = "";
        List<String> alsoKnownAs = new ArrayList<>();
        String placeOfOrigin = "";
        String description = "";
        String image = "";
        List<String> ingredients = new ArrayList<>();

        try {
            jsonObject = new JSONObject(json);
            jsonObjectName = jsonObject.getJSONObject(JsonUtils.NAME);
            mainName = jsonObjectName.getString(JsonUtils.MAIN_NAME);
            alsoKnownAs = getJSONArrayList(jsonObjectName.getJSONArray(JsonUtils.ALSO_KNOWN_AS));
            placeOfOrigin = jsonObject.getString(JsonUtils.PLACE_OF_ORIGIN);
            description = jsonObject.getString(JsonUtils.DESCRIPTION);
            image = jsonObject.getString(JsonUtils.IMAGE);
            ingredients = getJSONArrayList(jsonObject.getJSONArray(JsonUtils.INGREDIENTS));
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error with parse sandwich JSON: {}", e);
        }

        return new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description, image, ingredients);
    }

    private static List<String> getJSONArrayList(JSONArray jsonArray) {
        List<String> stringList = new ArrayList<>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    stringList.add(jsonArray.getString(i));
                } catch (JSONException e) {
                    Log.e(JsonUtils.LOG_TAG, "Error with Array List: {}", e);
                }
            }
        }
        return stringList;
    }
}
