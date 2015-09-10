package com.example.xyzreader.util;

import com.google.gson.JsonElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonUtil {
// ------------------------------ FIELDS ------------------------------

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

// -------------------------- STATIC METHODS --------------------------

    public static Date toDate(JsonElement element, String key) {
        String date = toString(element, key);
        try {
            return DATE_FORMATTER.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String toString(JsonElement element, String key) {
        JsonElement e = element.getAsJsonObject().get(key);
        return e == null ? "" : e.getAsString().trim();
    }
}
