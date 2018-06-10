package com.jw.chat;


import com.jw.library.utils.CacheUtils;

/**
 * 创建时间：2017/3/26
 * 更新时间 2017/10/30 0:35
 * 版本：
 * 作者：Mr.jin
 * 描述：
 */

public interface GoChatURL {
    String BASE_QQ_HOST= CacheUtils.getCacheString("BASE_QQ_HOST",
            "10.65.234.227", GoChat.getContext());
    String BASE_HTTP="http://"+BASE_QQ_HOST+":8080/ChatServer";
    int BASE_QQ_PORT = 9090;
    String URL_HTTP_REGISTER=BASE_HTTP+"/register";
    String URL_HTTP_LOGIN=BASE_HTTP+"/login";
    String URL_HTTP_SEARCH_CONTACT = BASE_HTTP+ "/user/search";
}
