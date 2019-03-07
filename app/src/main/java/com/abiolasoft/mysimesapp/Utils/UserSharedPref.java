package com.abiolasoft.mysimesapp.Utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.abiolasoft.mysimesapp.Interfaces.SharePref;
import com.abiolasoft.mysimesapp.Models.UserDetails;
import com.google.gson.Gson;

import static com.facebook.FacebookSdk.getApplicationContext;

public class UserSharedPref implements SharePref<UserDetails> {
    private SharedPreferences mPrefs;

    public UserSharedPref() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    public void setObj(String key, UserDetails obj) {
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        prefsEditor.putString(key, json);
        prefsEditor.commit();
    }

    public UserDetails getObj(String key) {
        Gson gson = new Gson();
        String json = mPrefs.getString(key, null);
        UserDetails obj = gson.fromJson(json, UserDetails.class);
        return obj;
    }
}
