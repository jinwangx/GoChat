package com.jw.chat

import com.jw.library.utils.PrefUtils


/**
 * 创建时间：2017/3/26
 * 更新时间 2017/10/30 0:35
 * 版本：
 * 作者：Mr.jin
 * 描述：
 */

interface GoChatURL {
    companion object {
        val BASE_QQ_HOST = PrefUtils.getString(GoChat.getContext(),"BASE_QQ_HOST",
                "10.65.234.227")
        val BASE_HTTP = "http://$BASE_QQ_HOST:8080/ChatServer"
        val BASE_QQ_PORT = 9090
        val URL_HTTP_REGISTER = "$BASE_HTTP/register"
        val URL_HTTP_LOGIN = "$BASE_HTTP/login"
        val URL_HTTP_SEARCH_CONTACT = "$BASE_HTTP/user/search"
    }
}