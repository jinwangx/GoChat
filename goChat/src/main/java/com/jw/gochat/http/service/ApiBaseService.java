package com.jw.gochat.http.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 Created by jinwangx on 16/11/14.
 */

public interface ApiBaseService {
    @FormUrlEncoded
    @POST()
    Observable<String> obPost(@Url String url, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST()
    Observable<JSONArray> obPostArray(@Url String url, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST()
    Observable<JSONObject> obPostJsonObject(@Url String url, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST()
    Call<JSONObject> post(@Url String url, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST()
    Call<JSONObject> postJsonObject(@Url String url, @FieldMap Map<String, String> params);
}
