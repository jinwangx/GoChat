package com.jw.gochat;

import okhttp3.Interceptor;

/**
 * Created by liyuan on 17/4/18.
 */

public class StethoStub {
    private static IStetho sInstance;

    public static void init(IStetho stetho) {
        sInstance = stetho;
    }

    public static Interceptor getInterceptor() {
        if (sInstance == null) {
            return HttpUtil.INSTANCE.getEMPTY_INTERCEPTOR();
        }
        return sInstance.getInterceptor();
    }

    public interface IStetho {
        Interceptor getInterceptor();
    }
}
