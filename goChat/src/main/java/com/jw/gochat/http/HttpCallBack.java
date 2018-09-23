package com.jw.gochat.http;

import org.json.JSONObject;

/**
 * Created by jinwangx on 16/11/17.
 */

public class HttpCallBack {
    public void onFinish(){

    }

    public void onSuccess(JSONObject response) {

    }

    public void onFailed(Throwable e) {

    }

    public static final HttpCallBack EMPTY = new HttpCallBack();
}
