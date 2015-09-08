package com.example.xyzreader.remote;

import java.net.MalformedURLException;
import java.net.URL;

public class Config {
// ------------------------------ FIELDS ------------------------------

    public static final URL BASE_URL;

// -------------------------- STATIC METHODS --------------------------

    static {
        URL url = null;
        try {
            url = new URL("https://dl.dropboxusercontent.com/u/231329/xyzreader_data/data.json");
        } catch (MalformedURLException ignored) {
            // TODO: throw a real error
        }
        BASE_URL = url;
    }
}
