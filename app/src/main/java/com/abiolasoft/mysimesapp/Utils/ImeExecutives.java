package com.abiolasoft.mysimesapp.Utils;

import java.util.HashMap;
import java.util.Map;

public enum ImeExecutives {
    PRES("President"),
    VICE1("1st Vice President"),
    VICE2("2nd Vice president"),
    GEN_SEC("General Secretary"),
    ASS_SEC("Assistant Secretary"),
    FIN_SEC("Financial Secretary"),
    SPORT1("Sport Director"),
    SPORT2("Sport Director 2"),
    SOCIAL("Social Director"),
    WELFARE("Welfare Director"),
    TREASURER("Treasurer"),
    PRO("Public Relations Officer");

    private static final Map<String, ImeExecutives> lookup = new HashMap<String, ImeExecutives>();

    static {
        for (ImeExecutives c : ImeExecutives.values()) {
            lookup.put(c.getTheValues(), c);
        }
    }

    private final String theValues;

    private ImeExecutives(String aValue) {
        theValues = aValue;
    }

    public static ImeExecutives get(String theClass) {
        return lookup.get(theClass);
    }

    public String getTheValues() {
        return theValues;
    }

    @Override
    public String toString() {
        return theValues;
    }
}
