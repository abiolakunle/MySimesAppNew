package com.abiolasoft.mysimesapp.Utils;

import java.util.HashMap;
import java.util.Map;

public enum ImeClasses {
    ND1_FT("ND1 FULLTIME"),
    ND2_FT("ND2 FULLTIME"),
    HND1_FT("HND1 FULLTIME"),
    HND2_FT("HND2 FULLTIME"),
    ND1_PT("ND1 PARTTIME"),
    ND2_PT("ND2 PARTTIME"),
    ND3_PT("ND3 PARTTIME"),
    HND1_PT("HND1 PARTTIME"),
    HND2_PT("HND2 PARTTIME"),
    HND3_PT("HND3 PARTTIME");

    private static final Map<String, ImeClasses> lookup = new HashMap<String, ImeClasses>();

    static {
        for (ImeClasses c : ImeClasses.values()) {
            lookup.put(c.getTheValues(), c);
        }
    }

    private final String theValues;

    private ImeClasses(String aValue) {
        theValues = aValue;
    }

    public static ImeClasses get(String theClass) {
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
