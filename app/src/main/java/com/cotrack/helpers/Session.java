package com.cotrack.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.preference.PreferenceManager;

public class Session {

    private SharedPreferences prefs;

    public Session(Activity activity) {
        prefs = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    public void setUserName(String userName) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userName", userName);
    }

    public String getusername() {
        String userName = prefs.getString("userName","");
        System.out.println("User name session: " + userName);
        return userName;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void hideKeyboardFromFragment(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void setUserType(String userType) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userType", userType);
    }

    public String getUserType() {
        String userType = prefs.getString("userType","");
        System.out.println("User type session: " + userType);
        return userType;
    }
}
