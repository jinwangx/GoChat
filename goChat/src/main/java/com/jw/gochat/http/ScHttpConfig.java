package com.jw.gochat.http;

/**
 * Created by jinwangx on 16/11/17.
 */

public class ScHttpConfig {
    String baseurl;
    boolean debug;
    String userAgent;

    private ScHttpConfig() {
    }

    public ScHttpConfig setBaseUrl(String baseurl) {
        this.baseurl = baseurl;
        return this;
    }

    public ScHttpConfig setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public ScHttpConfig setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public String getBaseurl() {
        return baseurl;
    }

    public boolean isDebug() {
        return debug;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public static ScHttpConfig create() {
        return new ScHttpConfig();
    }
}
