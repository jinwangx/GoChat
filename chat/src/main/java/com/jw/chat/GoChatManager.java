package com.jw.chat;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.jw.chat.callback.GoChatCallBack;
import com.jw.chat.callback.GoChatObjectCallBack;
import com.jw.chat.core.AuthRequest;
import com.jw.chat.core.ChatRequest;
import com.jw.chat.core.PacketConnector;
import com.jw.chat.future.HttpFuture;
import com.jw.chat.msg.ChatMessage;
import com.jw.library.utils.NetUtils;


import org.apache.mina.core.session.IoSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 创建时间：2017/3/31
 * 更新时间 2017/10/30 0:35
 * 版本：
 * 作者：Mr.jin
 * 描述：负责客户端的http通信以及tcp通信
 *       监听tcp通道请求的发送消息以及接收消息的状态
 */

public class GoChatManager {
    private static GoChatManager instance;
    private Context context;
    private Map<String, String> headers = new HashMap<String, String>();
    private String authSequence;
    private PacketConnector connector;
    private List<PacketConnector.ConnectListener> connectListeners = new LinkedList<PacketConnector.ConnectListener>();
    private OnPushListener pushListener;
    private Map<String, ChatRequest> requests = new LinkedHashMap<String, ChatRequest>();
    private Thread mainThread;
    private Handler handler = new Handler();
    private static OkHttpClient CLIENT;


    public static GoChatManager getInstance(OkHttpClient client) {
        if (instance == null) {
            synchronized (GoChatManager.class) {
                if (instance == null) {
                    instance = new GoChatManager();
                }
            }
        }
        CLIENT = client;
        return instance;
    }

    private GoChatManager() {
        context = GoChat.getContext();
        mainThread = Thread.currentThread();
    }

    /**
     * 初始化连接用户的安全信息,初始化请求消息头
     * @param account 账号
     * @param token 账号token
     */
    public void initAccount(String account, String token) {
        headers.put("account", account);
        headers.put("token", token);
    }

    /**
     * 同步请求并回调
     * @param CLIENT 传入一个OKHttp对象
     * @param request 封装好的请求
     * @param callBack 传入监听
     * @return
     */
    public HttpFuture doHttpRequest(OkHttpClient CLIENT, Request request, final GoChatObjectCallBack callBack){
        Call call = CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callBack != null) {
                    callBack.onError(GoChatError.Companion.getERROR_CLIENT_NET(), "网络连接失败");
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    JsonParser parser = new JsonParser();
                    JsonObject root = parser.parse(response.body().string())
                            .getAsJsonObject();
                    if (root == null) {
                        if (callBack != null) {
                            callBack.onError(GoChatError.Companion.getERROR_SERVER(), "响应成功，" +
                                    "但没返回的数据");
                        }
                    } else {
                        if (callBack != null) {
                            JsonPrimitive flagObj = root
                                    .getAsJsonPrimitive("flag");
                            boolean flag = flagObj.getAsBoolean();
                            if (flag) {
                                JsonObject dataObj = root
                                        .getAsJsonObject("data");

                                if (dataObj == null) {
                                    callBack.onSuccess(null);
                                } else {
                                    Object data = new Gson().fromJson(dataObj,
                                            callBack.getClazz());
                                    callBack.onSuccess(data);
                                }

                            } else {
                                // 如果返回错误
                                // 获得错误code
                                JsonPrimitive errorCodeObj = root
                                        .getAsJsonPrimitive("errorCode");
                                // 获得错误string
                                JsonPrimitive errorStringObj = root
                                        .getAsJsonPrimitive("errorString");

                                int errorCode = errorCodeObj.getAsInt();
                                String errorString = errorStringObj
                                        .getAsString();

                                callBack.onError(errorCode, errorString);
                            }
                        }
                    }
                } else {
                    if (callBack != null) {
                        callBack.onError(GoChatError.Companion.getERROR_SERVER(), "服务器异常");
                    }
                }
            }
        });
        return new HttpFuture(call);
    }

    /**
     * 登录
     *
     * @param account
     * @param password
     * @param callBack
     * @return
     */
    @SuppressWarnings("rawtypes")
    public void login(String account, String password,
                      final GoChatObjectCallBack callBack) {
        RequestBody requestBody=new FormBody.Builder().add("account", account).
                add("password", password).build();
        Request request = new Request.Builder().url(GoChatURL.Companion.getURL_HTTP_LOGIN()).post(requestBody).build();
        doHttpRequest(CLIENT,request,callBack);
    }

    /**
     * 注册
     *
     * @param account
     * @param password
     * @param callBack
     * @return
     */
    @SuppressWarnings("rawtypes")
    public HttpFuture register(String account, String password,
                               final GoChatObjectCallBack callBack) {
        RequestBody requestBody=new FormBody.Builder().add("account", account).
                add("password", password).build();
        Request request=new Request.Builder().url(GoChatURL.Companion.getURL_HTTP_REGISTER()).post(requestBody)
                .build();
        return doHttpRequest(CLIENT,request,callBack);
    }

    /**
     * 搜索用户
     * @param search 搜索的账号
     * @param callBack
     * @return 可操作的future
     */
    @SuppressWarnings("rawtypes")
    public HttpFuture searchContact(String search,
                                    final GoChatObjectCallBack callBack) {
        RequestBody requestBody=new FormBody.Builder().add("search", search).build();
        Request.Builder url = new Request.Builder().url(GoChatURL.Companion.getURL_HTTP_SEARCH_CONTACT());
        for (Map.Entry<String, String> me : headers.entrySet()) {
            url.addHeader(me.getKey(), me.getValue());
        }
        Request request = url.post(requestBody).build();
        return doHttpRequest(CLIENT,request,callBack);
    }


    /**
     * socket 连接认证
     * @param account 账号
     * @param token token
     */
    public void auth(final String account, final String token) {
        headers.put("account", account);
        headers.put("token", token);

        ThreadManager.getInstance().createLongPool(3,3,2L).execute(new Runnable() {
            @Override
            public void run() {
                if (connector == null) {
                    connector = new PacketConnector(GoChatURL.Companion.getBASE_QQ_HOST(),
                            GoChatURL.Companion.getBASE_QQ_PORT(), 3);
                }
                conncectChatServer();
                AuthRequest request = new AuthRequest(account, token);
                authSequence = request.getSequence();
                connector.addRequest(request);
            }
        });
    }

    /**
     * 发送消息
     * @param message
     * @param callBack
     */
    public void sendMessage(final ChatMessage message,
                            final GoChatCallBack callBack) {
        if(!NetUtils.isNetConnected(context))
            callBack.onError(GoChatError.Companion.getERROR_CLIENT_NET(),"消息发送失败，没联网");
        ThreadManager.getInstance().createLongPool(3,3,2L).execute(new Runnable() {
            @Override
            public void run() {
                if (connector == null) {
                    connector = new PacketConnector(GoChatURL.Companion.getBASE_QQ_HOST(),
                            GoChatURL.Companion.getBASE_QQ_PORT(), 3);
                }
                conncectChatServer();
                // 加入到请求map中 为以后的response做处理
                ChatRequest request = new ChatRequest(callBack, message);
                requests.put(request.getSequence(), request);
                connector.addRequest(request);
            }
        });
    }

    private void conncectChatServer() {
        if (connector != null) {
            connector.connect();
            // 设置输入输出监听
            connector.setIOListener(ioListener);
            // 设置连接监听
            connector.setConnectListener(connectListener);
        }
    }

    public void closeSocket() {
        if (connector != null && connector.isConnected()) {
            connector.disconnect();
            removeConnectionListener(connectListener);
            connector = null;
        }
    }

    /**
     * 添加连接监听
     * @param listener
     */
    public void addConnectionListener(PacketConnector.ConnectListener listener) {
        if (!connectListeners.contains(listener)) {
            connectListeners.add(listener);
        }
    }

    /**
     * 移除连接监听
     * @param listener
     */
    public void removeConnectionListener(PacketConnector.ConnectListener listener) {
        if (connectListeners.contains(listener)) {
            connectListeners.remove(listener);
        }
    }

    /**
     * Connector成功收到消息时
     * @param listener
     */
    public void setPushListener(OnPushListener listener) {
        this.pushListener = listener;
    }

    /**
     * Connector接收消息监听
     */
    private PacketConnector.IOListener ioListener= new PacketConnector.IOListener() {

        /**
         * 消息发送失败
         * @param request
         * @param e
         */
        @Override
        public void onOutputFailed(ChatRequest request, Exception e) {
            // 消息发送失败，通知回调，让显示层做处理
            GoChatCallBack callBack = request.getCallBack();
            if (callBack != null) {
                callBack.onError(GoChatError.Companion.getERROR_CLIENT_NET(), "客户端网络未连接");
            }
            Log.v("GoChat_onOutputFailed",request.getTransport()+"发送失败");
        }

        /**
         * 成功接收到消息
         * @param session
         * @param message
         */
        @Override
        public void onInputComed(IoSession session, Object message) {
            if (message instanceof String) {
                String json = ((String) message).trim();

                JsonParser parser = new JsonParser();
                JsonObject root = parser.parse(json).getAsJsonObject();

                // 获得方向:是请求还是response
                JsonPrimitive typeJson = root.getAsJsonPrimitive("type");
                String type = typeJson.getAsString();

                // 获得序列号
                JsonPrimitive sequenceJson = root
                        .getAsJsonPrimitive("sequence");
                String sequence = sequenceJson.getAsString();

                // 服务器推送消息
                if ("request".equalsIgnoreCase(type)) {
                    JsonPrimitive actionJson = root
                            .getAsJsonPrimitive("action");
                    String action = actionJson.getAsString();
                    if (pushListener != null) {
                        //开始推送收到的消息给客户端
                        boolean pushed = pushListener.onPush(action,
                                (Map<String, Object>) new Gson().fromJson(root,
                                        new TypeToken<Map<String, Object>>() {
                                        }.getType()));
                        Log.v("GoChat_onInputComed",action+"接收到服务器推送的消息");
                        //客户端给服务器的响应
                        if (pushed) {
                            session.write("{type:'response',sequence:'"
                                    + sequence + "',flag:" + true + "}");
                        } else {
                            session.write("{type:'response',sequence:'"
                                    + sequence + "',flag:" + false
                                    + ",errorCode:1,errorString:'客户端未处理成功!'}");
                        }
                    }
                }
                //客户端发送消息后，服务器返回的响应
                else if ("response".equalsIgnoreCase(type)) {
                    // 请求返回response
                    JsonPrimitive flagJson = root.getAsJsonPrimitive("flag");
                    boolean flag = flagJson.getAsBoolean();
                    // 消息发送结果只有 成功或者 失败,不需要返回对象
                    if (flag) {
                        if (sequence.equals(authSequence)) {
                            Log.v("GoChat_onInputComed","认证成功"+"接收到服务器的响应");
                            //return;
                        }
                        // 消息成功发送
                        ChatRequest request = requests.remove(sequence);
                        if (request != null) {
                            final GoChatCallBack callBack = request
                                    .getCallBack();
                            if (callBack != null) {
                                // 在主线程中调用
                                if (Thread.currentThread() != mainThread) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            callBack.onSuccess();
                                        }
                                    });
                                } else {
                                    callBack.onSuccess();
                                }
                            }
                        }
                    } else {
                        if (sequence.equals(authSequence)) {
                            Log.v("GoChat_onInputComed","认证失败"+"接收到服务器的响应");
                            //return;
                        }

                        // 认证失败
                        ListIterator<PacketConnector.ConnectListener> iterator = connectListeners
                                .listIterator();
                        while (iterator.hasNext()) {
                            final PacketConnector.ConnectListener listener = iterator.next();
                            // 在主线程中调用
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onAuthFailed();
                                }
                            });

                        }
                    }
                }
            }
        }
    };

    /**
     * Connector连接监听
     */
    private PacketConnector.ConnectListener connectListener = new PacketConnector.ConnectListener() {

        @Override
        public void onReConnecting() {
            ListIterator<PacketConnector.ConnectListener> iterator = connectListeners
                    .listIterator();
            while (iterator.hasNext()) {
                PacketConnector.ConnectListener listener = iterator.next();
                listener.onReConnecting();
            }
        }

        @Override
        public void onDisconnected() {
            ListIterator<PacketConnector.ConnectListener> iterator = connectListeners
                    .listIterator();
            while (iterator.hasNext()) {
                PacketConnector.ConnectListener listener = iterator.next();
                listener.onDisconnected();
            }

            authSequence = null;
            connector = null;
        }

        @Override
        public void onConnecting() {
            ListIterator<PacketConnector.ConnectListener> iterator = connectListeners
                    .listIterator();
            while (iterator.hasNext()) {
                PacketConnector.ConnectListener listener = iterator.next();
                listener.onReConnecting();
            }
        }

        @Override
        public void onConnected() {
            ListIterator<PacketConnector.ConnectListener> iterator = connectListeners
                    .listIterator();
            while (iterator.hasNext()) {
                PacketConnector.ConnectListener listener = iterator.next();
                listener.onConnected();
            }
        }

        @Override
        public void onAuthFailed() {
            ListIterator<PacketConnector.ConnectListener> iterator = connectListeners
                    .listIterator();
            while (iterator.hasNext()) {
                PacketConnector.ConnectListener listener = iterator.next();
                listener.onAuthFailed();
            }
        }
    };

    public interface OnPushListener {
        boolean onPush(String type, Map<String, Object> data);
    }
}