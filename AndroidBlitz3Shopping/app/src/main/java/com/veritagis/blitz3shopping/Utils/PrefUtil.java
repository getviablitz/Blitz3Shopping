package com.veritagis.blitz3shopping.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtil {
    static public String getString(Context context, String key) {
        SharedPreferences settings = Prefs.get(context);
        return settings.getString(key, "");
    }

    static public int getInt(Context context, String key, int defaultInt) {
        SharedPreferences settings = Prefs.get(context);
        return settings.getInt(key, defaultInt);
    }

    static public String getString(Context context, String key, String defaultString) {
        SharedPreferences settings = Prefs.get(context);
        return settings.getString(key, defaultString);
    }

    static public synchronized void putInt(Context context, String key,
                                           int value) {
        SharedPreferences settings = Prefs.get(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    static public synchronized void putString(Context context, String key,
                                              String value) {
        SharedPreferences settings = Prefs.get(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    static public synchronized void putBoolean(Context context, String key,
                                               boolean value) {
        SharedPreferences settings = Prefs.get(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    static public boolean getBoolean(Context context, String key, boolean defaultString) {
        SharedPreferences settings = Prefs.get(context);
        return settings.getBoolean(key, defaultString);
    }
    static public final class Prefs {
        public static SharedPreferences get(Context context) {
            return context.getSharedPreferences("_dreamf_pref", 0);
        }
    }
}