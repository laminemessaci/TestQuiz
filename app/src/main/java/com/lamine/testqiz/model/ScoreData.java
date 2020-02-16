package com.lamine.testqiz.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ScoreData {

    //Constants for shared preferences
    private static final String SHARED_PREFERENCES = "SHARED_PREFERENCES";
    private static final String SCORE_DATA = "MOOD_DATA";

    public static ArrayList<ItemRowScore> mItemRowScores = new ArrayList<>();

    //Default constructor
    public ScoreData() {
    }

    /*
     * @saveData method
     * @activity param context of the activity
     *
     * This method allow to save array in shared preferences using Gson library
     * */
    public static void saveData(Context activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mItemRowScores);
        editor.putString(SCORE_DATA, json);
        editor.apply();
    }

    /*
     * @loadData method
     * @activity param context of the activity
     *
     * This method allow to load array from shared preferences using Gson library
     * */
    public static void loadData(Context activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SCORE_DATA, null);
        Type type = new TypeToken<ArrayList<ItemRowScore>>() {
        }.getType();
        mItemRowScores = gson.fromJson(json, type);

        if (mItemRowScores == null) {
            mItemRowScores = new ArrayList<> ();
        }
    }

    /*
     * @clear method
     * @activity param context of the activity
     *
     * This method allow to clear data from shared preferences (useful when you need to reset the view)
     * */
    public static void clearData(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
