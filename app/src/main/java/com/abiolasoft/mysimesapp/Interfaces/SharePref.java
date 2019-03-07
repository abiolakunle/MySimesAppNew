package com.abiolasoft.mysimesapp.Interfaces;

public interface SharePref<T> {
    void setObj(String KEY, T obj);

    T getObj(String KEY);
}
