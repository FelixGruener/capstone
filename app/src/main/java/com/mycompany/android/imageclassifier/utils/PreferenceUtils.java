package com.mycompany.android.imageclassifier.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

/**
 * Created by delaroy on 3/26/18.
 */

public class PreferenceUtils {

    private static final String CLASSIFICATION_ID = "classification_id";
    private static final String CLASSIFICATION_NAME = "classification_name";
    public static final int NO_ID = -1;
    public static final String NO_NAME = "";

    public PreferenceUtils(){

    }
    public static <T> boolean save(T object, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(object);
        prefsEditor.putString(object.getClass().getSimpleName(), json);
        prefsEditor.apply();
        return true;
    }

    public static <T> T read(Class<T> oClass, Context context) {
        Gson gson = new Gson();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(oClass.getSimpleName(), "");
        if (json == null) {
            return null;
        }
        return gson.fromJson(json, oClass);
    }

    public static boolean saveToken(String token, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.TOKEN, token);
        prefsEditor.apply();
        return true;
    }

    public static String getToken(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.TOKEN, "");
    }

    public static final int getSelectedClassificationId(@NonNull Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(CLASSIFICATION_ID, NO_ID);
    }

    public static void setSelectedClassificationId(@NonNull Context context, int classificationId) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(CLASSIFICATION_ID, classificationId);
        editor.apply();
    }

    public static final String getSelectedClassificationName(@NonNull Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(CLASSIFICATION_NAME, NO_NAME);
    }

    public static void setSelectedClassificationName(@NonNull Context context, String recipeName) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CLASSIFICATION_NAME, recipeName);
        editor.apply();
    }
}
