package com.theflexproject.thunder.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {

    Context context;
    SharedPreferences sharedPreferences;
    public SettingsManager(Context context) {
        sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        this.context = context;
    }

    public void saveRefresh(Boolean REFRESH_PERIODICALLY_BUTTON) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("REFRESH_SETTING", REFRESH_PERIODICALLY_BUTTON);
        editor.commit();
    }
    public void saveCast(Boolean CAST_BUTTON) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("CAST_SETTING", CAST_BUTTON);
        editor.commit();
    }
    public void saveExternal(Boolean EXTERNAL_PLAYER_BUTTON) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("EXTERNAL_SETTING", EXTERNAL_PLAYER_BUTTON);
        editor.commit();

    }

}