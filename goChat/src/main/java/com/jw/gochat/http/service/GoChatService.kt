package com.jw.gochat.http.service

import io.reactivex.Observable
import org.json.JSONObject
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * 创建时间:2018/9/18 on 15:51
 * 创建人:jinwangx
 * 描述:
 */
interface GoChatService {
    @FormUrlEncoded
    @POST("register")
    fun register(@Field("account") account: String, @Field("password") password: String): Observable<JSONObject>

    @FormUrlEncoded
    @POST("login")
    fun login(@Field("account") account: String, @Field("password") password: String): Observable<JSONObject>

    @FormUrlEncoded
    @POST("user/search")
    fun searchAccount(@Header("account") account: String, @Header("token") token: String, @Field("search") search: String): Observable<JSONObject>

}