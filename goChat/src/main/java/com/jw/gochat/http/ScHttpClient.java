package com.jw.gochat.http;

import android.annotation.SuppressLint;
import android.support.v4.util.Preconditions;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jw.gochat.http.service.ApiBaseService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by jinwangx on 16/11/18.
 */
public class ScHttpClient {
    private final ScHttpConfig config;
    private final String baseUrl;
    private final Retrofit retrofit;
    private final OkHttpClient okHttpClient;
    private final ApiBaseService baseService;
    private final Map<Class<?>, Object> serviceMap = new ConcurrentHashMap<>();

    private static ScHttpClient sInstance;

    /**
     * 初始化，只调用一次
     */
    public static void init(ScHttpConfig config) {
        if (sInstance != null) {
            return;
        }
        sInstance = new ScHttpClient(config);
    }

    public static OkHttpClient getOkHttpClient(){
        return sInstance.okHttpClient;
    }

    /**
     * 获取service
     */
    @SuppressLint("RestrictedApi")
    public static <T> T getService(final Class<T> serviceCls) {
        Preconditions.checkNotNull(sInstance);
        return sInstance.getServiceInner(serviceCls);
    }

    /**
     * 同步请求
     */
    @SuppressLint("RestrictedApi")
    public static JSONObject postSync(String url, Map<String, String> params) throws IOException {
        Preconditions.checkNotNull(sInstance);
        return sInstance.postSyncInner(url, params);
    }

    /**
     * 返回Jsonobject
     */
    @SuppressLint("RestrictedApi")
    public static Observable<JSONObject> obPost(String url, Map<String, String> params) {
        Preconditions.checkNotNull(sInstance);
        return sInstance.obPostWithJson(url, params);
    }

    /**
     * 返回string
     */
    @SuppressLint("RestrictedApi")
    public static Observable<JSONArray> obPostArray(String url, Map<String, String> params) {
        Preconditions.checkNotNull(sInstance);
        return sInstance.obPostWithArray(url, params);
    }

    @SuppressLint("RestrictedApi")
    public static ApiBaseService getBaseService() {
        Preconditions.checkNotNull(sInstance);
        return sInstance.baseService;
    }

    /**
     * 异步请求
     */
    @SuppressLint("RestrictedApi")
    public static void post(String url, Map<String, String> params, final HttpCallBack cb) {
        Preconditions.checkNotNull(sInstance);
        sInstance.postInner(url, params, cb);
    }

/*    public static boolean downloadFileSync(String uri, File output, AtomicBoolean cancel) {
        Preconditions.checkNotNull(sInstance);
        return sInstance.downloadFileSyncInner(uri, output, cancel);
    }*/


    private ScHttpClient(ScHttpConfig config) {
        this.config = config;
        baseUrl = config.getBaseurl();
        okHttpClient = createOkHttpClient();
        retrofit = createRetrofit();
        baseService = retrofit.create(ApiBaseService.class);
    }

    private <T> T getServiceInner(final Class<T> serviceCls) {
        if (serviceCls == null) {
            throw new RuntimeException("getService service is null!");
        }
        T service = (T) serviceMap.get(serviceCls);
        if (service == null) {
            service = retrofit.create(serviceCls);
            serviceMap.put(serviceCls, service);
        }
        return service;
    }

    /**
     * 同步请求, 返回 JSONObject
     */
    private JSONObject postSyncInner(String url, Map<String, String> params) throws IOException {
        Call<JSONObject> call = baseService.postJsonObject(url, params);
        Response<JSONObject> response = call.execute();
        return response.body();
    }

    /**
     * 返回jsonobject
     */
    private Observable<JSONObject> obPostWithJson(String url, Map<String, String> params) {
        return baseService.obPostJsonObject(url, params);
    }

    /**
     * 返回jsonobject
     */
    private Observable<JSONArray> obPostWithArray(String url, Map<String, String> params) {
        return baseService.obPostArray(url, params);
    }

    /**
     * 异步请求, 回调函数在UI线程
     */
    private void postInner(String url, Map<String, String> params, final HttpCallBack cb) {
        Call<JSONObject> call = baseService.postJsonObject(url, params);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                cb.onSuccess(response.body());
                cb.onFinish();
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                cb.onFailed(t);
                cb.onFinish();
            }
        });
    }


    private Retrofit createRetrofit() {
        //Build retrofit
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
//                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(new JsonObjectFacotry())
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }


    private OkHttpClient createOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(17, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                /*               .addInterceptor(new ParamChecker())
                               .addInterceptor(getLogInterceptor(debug))*/
                //                .addInterceptor(new GzipRequestInterceptor())
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }

    /**
     * This interceptor compresses the HTTP request body. Many webservers can't handle this!
     */
    static class GzipRequestInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            if (originalRequest.body() == null || originalRequest.header("Content-Encoding") != null) {
                return chain.proceed(originalRequest);
            }

            RequestBody gzip = gzip(originalRequest.body());
            Request compressedRequest = originalRequest.newBuilder()
                    .header("Content-Encoding", "gzip")
                    //                    .method(originalRequest.method(), gzip)
                    .build();
            //L.e(compressedRequest.toString());
            return chain.proceed(compressedRequest);
        }

        private RequestBody gzip(final RequestBody body) {
            return new RequestBody() {
                @Override
                public MediaType contentType() {
                    return body.contentType();
                }

                @Override
                public long contentLength() {
                    return -1; // We don't know the compressed length in advance!
                }

                @Override
                public void writeTo(BufferedSink bufferedSink) throws IOException {
                    BufferedSink gzipSink = Okio.buffer(new GzipSink(bufferedSink));
                    body.writeTo(gzipSink);
                    gzipSink.close();
                }
            };
        }
    }

    /*   *//**
     参数检查
     *//*
    private class ParamChecker implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            builder = builder.addHeader("User-Agent", config.getUserAgent());
            Map<String, String> headers = InfoUtils.getClientInfoHeader(GlobleContext.getContext());
            for (String key : headers.keySet()) {
                builder = builder.addHeader(key, headers.get(key));
            }
            return chain.proceed(builder.build());
        }
    }

    public static Interceptor getLogInterceptor(boolean debug) {
        return debug ? new LogInterceptor() : HttpUtil.INSTANCE.getEMPTY_INTERCEPTOR();
    }

    *//**
     Log 打印
     *//*
    private static class LogInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            //Print log before request;
            Request request = chain.request();

            String bodyStr = "";
            RequestBody requestBody = request.body();
            if (requestBody != null && requestBody.contentLength() > 0) {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                bodyStr = buffer.readString(Charset.defaultCharset());
            }
            HttpUrl url = request.url();
            SencentLog.INSTANCE.i("httpclient", "post:url=" + url + ((url.querySize() > 0) ? "&" : "?") + bodyStr);
            return chain.proceed(request);
        }
    }*/




    private static class JsonObjectFacotry extends Converter.Factory {
        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            //进行条件判断，如果传进来的Type不是class，则匹配失败
            if (!(type instanceof Class<?>)) {
                return null;
            }
            //进行条件判断，如果传进来的Type不是JSONObject的实现类，则也匹配失败
            Class<?> c = (Class<?>) type;
            if (JSONObject.class.isAssignableFrom(c)) {
                return (Converter<ResponseBody, JSONObject>) value -> {
                    try {
                        JSONObject data = new JSONObject(value.string());
                        return data;
                    } catch (JSONException e) {
                        throw new NullPointerException();
                    }
                };
            } else if (JSONArray.class.isAssignableFrom(c)) {
                return (Converter<ResponseBody, JSONArray>) value -> {
                    try {
                        JSONArray data = new JSONArray(value.string());
                        return data;
                    } catch (JSONException e) {
                        throw new NullPointerException();
                    }
                };
            } else {
                return null;
            }
        }
    }


/*    *
     Download file sync

    private boolean downloadFileSyncInner(String uri, File output, AtomicBoolean cancel) {
        Request request = new Request.Builder().url(uri).build();
        InputStream is = null;
        FileOutputStream fos = null;
        byte[] buffer = new byte[1024];
        int lenght = 0;
        try {
            okhttp3.Response response = okHttpClient.newCall(request).execute();
            is = response.body().byteStream();
            fos = new FileOutputStream(output);
            //
            while ((lenght = is.read(buffer)) > 0) {
                if (cancel != null && cancel.get()) {
                    return false;
                }
                fos.write(buffer, 0, lenght);
            }
            fos.flush();
            return true;
        } catch (IOException e) {
            SencentLog.INSTANCE.e(e);
            return false;
        } catch (Exception e) {
            SencentLog.INSTANCE.e(e);
            return false;
        } finally {
            Closeables.close(is);
            Closeables.close(fos);
        }
    }*/
}
