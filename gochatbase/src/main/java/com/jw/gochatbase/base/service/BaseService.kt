package com.jw.gochatbase.base.service

import android.app.Service

abstract class BaseService : Service() {

    override fun onCreate() {
        super.onCreate()
        //((ChatApplication)getApplication()).addService(this);
    }

    override fun onDestroy() {
        super.onDestroy()
        //((ChatApplication)getApplication()).removeService(this);
    }
}