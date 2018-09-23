package com.jw.gochatbase.base.application

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.support.multidex.MultiDex
import java.lang.ref.WeakReference
import java.util.*

/**
 * 由 jinwangx 创建于 2017/9/14.
 */
open class BaseApplication : Application() {
    private val mActivities = LinkedList<WeakReference<Activity>>()
    private var mActiveActivityCount = 0

    override fun onCreate() {
        super.onCreate()

        mInstanceRef = WeakReference(this)

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityResumed(activity: Activity?) {
            }

            override fun onActivityStarted(activity: Activity?) {
                activity ?: return
                mActiveActivityCount++
            }

            override fun onActivityDestroyed(activity: Activity?) {
                activity ?: return
                synchronized(mActivities) {
                    val iterator = mActivities.iterator()
                    while (iterator.hasNext()) {
                        val existingActivity = iterator.next().get()
                        if (existingActivity == null || existingActivity == activity) {
                            iterator.remove()
                            continue
                        }
                    }
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity?, savedInstanceState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity?) {
                activity ?: return
                mActiveActivityCount--
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                activity ?: return
                mActivities.add(WeakReference(activity))
            }
        })
    }

    fun getActiveActivityCount() = mActiveActivityCount

    fun finishAll() {
        synchronized(mActivities) {
            mActivities.forEach { it.get()?.finish() }
        }
    }

    fun exit() {
        finishAll()
        System.exit(0)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {
        private var mInstanceRef: WeakReference<BaseApplication>? = null

        fun getInstance() = mInstanceRef?.get()

        fun getContext() = mInstanceRef?.get()?.applicationContext

        fun isForeground() = getInstance()?.getActiveActivityCount() ?: 0 > 0

        fun finishAll() = getInstance()?.finishAll()

        fun exit() = getInstance()?.exit()
    }
}