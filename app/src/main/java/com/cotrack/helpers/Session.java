package com.cotrack.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.inputmethod.InputMethodManager;

import androidx.preference.PreferenceManager;

public class Session {

    private SharedPreferences prefs;

    public Session(Activity activity) {
        prefs = activity.getPreferences(Context.MODE_PRIVATE);
    }

    public void setusename(String usename) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("usename", usename);
    }

    public String getusename() {
        String usename = prefs.getString("usename","");
        return usename;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}
