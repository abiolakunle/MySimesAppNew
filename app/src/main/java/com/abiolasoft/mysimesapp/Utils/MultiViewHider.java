package com.abiolasoft.mysimesapp.Utils;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MultiViewHider {

    private List<View> views;
    private boolean currentState = false;

    public MultiViewHider() {
        views = new ArrayList<View>();
    }

    public boolean getState() {
        return currentState;
    }

    public void add(View viewToHide) {
        views.add(viewToHide);
    }

    ;

    public void hideViews() {
        for (View v : views) {
            v.setVisibility(View.INVISIBLE);
        }
        currentState = true;
    }

    public void unHideViews() {
        for (View v : views) {
            v.setVisibility(View.VISIBLE);
        }
        currentState = false;
    }
}
