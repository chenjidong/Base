package com.kuky.baselib_java.baseUtils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author Kuky
 */

public class SharePreferenceUtils {
    private static final String SHARED_PREFERENCES_NAME = "persistence";

    /**
     * @param context
     * @param key
     * @param value
     */
    public static void saveString(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value).apply();
    }

    /**
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    /**
     * @param context
     * @param key
     * @param value
     */
    public static void saveInt(Context context, String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value).apply();
    }

    /**
     * @param context
     * @param key
     * @return
     */
    public static int getInt(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(key, 0);
    }

    /**
     * @param context
     * @param key
     * @param value
     */
    public static void saveLong(Context context, String key, long value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value).apply();
    }

    /**
     * @param context
     * @param key
     * @return
     */
    public static long getLong(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return preferences.getLong(key, 0);
    }

    /**
     * @param context
     * @param key
     * @param value
     */
    public static void saveFloat(Context context, String key, float value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(key, value).apply();
    }

    /**
     * @param context
     * @param key
     * @return
     */
    public static float getFloat(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return preferences.getFloat(key, 0.0f);
    }

    /**
     * @param context
     * @param key
     * @param value
     */
    public static void saveBoolean(Context context, String key, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value).apply();
    }

    /**
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key, boolean defaultState) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, defaultState);
    }

    /**
     * @param context
     * @param key
     * @param stringSet
     */
    public static void saveStringSet(Context context, String key, Set<String> stringSet) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(key, stringSet).apply();
    }

    /**
     * @param context
     * @param key
     * @return
     */
    public static Set<String> getStringSet(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return preferences.getStringSet(key, new TreeSet<String>());
    }
}
