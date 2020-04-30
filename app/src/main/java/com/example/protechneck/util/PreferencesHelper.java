package com.example.protechneck.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class PreferencesHelper {

    private static final String PREF_KEY = "MyPref";
    private static final String PREF_IS_SERVICE_RUNNING = "TECH_NECK_RUNNING";
    private static final String PREF_IS_RETURNING_USER = "IS_RETURNING_USER";
    private static final String PREF_APP_STRICTNESS = "PREF_APP_STRICTNESS";

    private static PreferencesHelper instance;
    private final Context context;

    private PreferencesHelper(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    public static PreferencesHelper getInstance(@NonNull Context context) {
        synchronized(PreferencesHelper.class) {
            if (instance == null) {
                instance = new PreferencesHelper(context);
            }
            return instance;
        }
    }

    public void setAppStrictnessPreference(String strictnessChosen) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PREF_APP_STRICTNESS, strictnessChosen);
        editor.apply();
    }

    public void setAppServiceRunningPreference(boolean isRunning) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(PREF_IS_SERVICE_RUNNING, isRunning);
        editor.apply();
    }

    public void setIsReturningUserPreference() {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(PREF_IS_RETURNING_USER, true);
        editor.apply();
    }

    public boolean isReturningUser() {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY, 0);
        return pref.getBoolean(PREF_IS_RETURNING_USER, false);
    }

    public boolean isServiceRunning() {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY, 0);
        return pref.getBoolean(PREF_IS_SERVICE_RUNNING, false);
    }

}
