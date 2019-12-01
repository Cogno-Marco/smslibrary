package com.eis.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

/**
 * Service that writes and reads configurations/preferences in Android memory
 *
 * @author Luca Crema
 * @version 2.0
 */
public class PreferencesManager {

    public static final int DEFAULT_INTEGER_RETURN = -1;
    public static final String DEFAULT_STRING_RETURN = "";
    public static final boolean DEFAULT_BOOLEAN_RETURN = false;

    protected static final int DEFAULT_UPDATE_INT_ADD = 1;

    /**
     * @param context context of an Activity or Service
     * @return default shared preferences class
     */
    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * @param sharedPreferences can be the default or a custom shared preferences class
     * @return the editor for the preferences
     */
    private static SharedPreferences.Editor getEditor(SharedPreferences sharedPreferences) {
        return sharedPreferences.edit();
    }

    /**
     * @param context context of an Activity or Service
     * @return default shared preferences editor
     */
    private static SharedPreferences.Editor getEditor(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).edit();
    }

    /**
     * @param ctx context of an Activity or Service
     * @param key key for the resource
     * @return the value of the resource if present, {@link #DEFAULT_INTEGER_RETURN} ({@value #DEFAULT_INTEGER_RETURN}) otherwise
     */
    public static int getInt(Context ctx, String key) {
        return getSharedPreferences(ctx).getInt(key, DEFAULT_INTEGER_RETURN);
    }

    /**
     * @param ctx context of an Activity or Service
     * @param key key for the resource
     * @return the value of the resource if present, {@link #DEFAULT_STRING_RETURN} ({@value #DEFAULT_STRING_RETURN}) otherwise
     */
    public static String getString(Context ctx, String key) {
        return getSharedPreferences(ctx).getString(key, DEFAULT_STRING_RETURN);
    }

    /**
     * @param ctx context of an Activity or Service
     * @param key key for the resource
     * @return the value of the resource if present, {@link #DEFAULT_BOOLEAN_RETURN} ({@value #DEFAULT_BOOLEAN_RETURN}) otherwise
     */
    public static boolean getBoolean(Context ctx, String key) {
        return getSharedPreferences(ctx).getBoolean(key, DEFAULT_BOOLEAN_RETURN);
    }

    /**
     * @param ctx   context of an Activity or Service
     * @param key   key for the resource
     * @param value value to be put or override
     * @return if the value has been set correctly
     */
    public static boolean setInt(Context ctx, String key, int value) {
        SharedPreferences.Editor editor = getEditor(ctx);
        editor.putInt(key, value);
        return editor.commit();
    }

    /**
     * @param ctx   context of an Activity or Service
     * @param key   key for the resource
     * @param value value to be put or override
     * @return if the value has been set correctly
     */
    public static boolean setString(Context ctx, String key, String value) {
        SharedPreferences.Editor editor = getEditor(ctx);
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     * @param ctx   context of an Activity or Service
     * @param key   key for the resource
     * @param value value to be put or override
     * @return if the value has been set correctly
     */
    public static boolean setBoolean(Context ctx, String key, boolean value) {
        SharedPreferences.Editor editor = getEditor(ctx);
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * Sums the value to an integer
     *
     * @param ctx   context of an Activity or Service
     * @param key   key for the resource
     * @param value value to be summed to the current value
     * @return if the value has been updated correctly
     */
    public static int updateInt(Context ctx, String key, int value) {
        int currentValue = getInt(ctx, key);
        if (currentValue == DEFAULT_INTEGER_RETURN)
            currentValue = 0;
        SharedPreferences.Editor editor = getEditor(ctx);
        editor.putInt(key, currentValue + value);
        editor.commit();
        return currentValue + value;
    }

    /**
     * Sums the current value of the preference to {@value DEFAULT_UPDATE_INT_ADD}
     *
     * @param ctx context of an Activity or Service
     * @param key key for the resource
     * @return if the value has been updated correctly
     */
    public static int updateInt(Context ctx, String key) {
        return updateInt(ctx, key, DEFAULT_UPDATE_INT_ADD);
    }

    /**
     * Sums 1 to the integer saved in memory and goes back to 1 if it exceeds the maxValue
     *
     * @param ctx context of an Activity or Service
     * @param key key for the resource
     * @return result of the shift, between 1 and maxValue included
     */
    public static int shiftInt(Context ctx, String key, int maxValue) {
        int currentValue = getInt(ctx, key);
        if (currentValue == DEFAULT_INTEGER_RETURN)
            currentValue = 0;
        int nextValue = (currentValue % maxValue) + 1;
        setInt(ctx, key, nextValue);
        return nextValue;
    }

    /**
     * Removes a value of any type from the phone preferences
     *
     * @param ctx context of an Activity or Service
     * @param key key for the resource
     * @return if the value has been removed correctly
     */
    public static boolean removeValue(Context ctx, String key) {
        SharedPreferences.Editor editor = getEditor(ctx);
        editor.remove(key);
        return editor.commit();
    }

    /**
     * Remove all saved values from the device's preferences
     * Use with caution
     *
     * @param ctx context of an Activity or Service
     * @return if all the values have been removed correctly
     */
    public static boolean removeAllValues(Context ctx) {
        SharedPreferences.Editor editor = getEditor(ctx);
        return editor.clear().commit();
    }

}
