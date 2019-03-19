package com.abiolasoft.mysimesapp.Utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.abiolasoft.mysimesapp.Interfaces.SharePref;
import com.abiolasoft.mysimesapp.Models.ImeClass;
import com.google.gson.Gson;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ImeClassSharedPref implements SharePref<ImeClass> {
    private SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

    @Override
    public void setObj(final String KEY, ImeClass obj) {
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        prefsEditor.putString(KEY, json);
        prefsEditor.commit();
    }

    @Override
    public ImeClass getObj(final String KEY) {
        Gson gson = new Gson();
        String json = mPrefs.getString(KEY, null);
        ImeClass obj = gson.fromJson(json, ImeClass.class);
        return obj;
    }
}
