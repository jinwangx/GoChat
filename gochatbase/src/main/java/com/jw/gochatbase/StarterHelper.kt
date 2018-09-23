package com.sencent.library.base.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

/**
 * 由 jinwangx 创建于 2017/9/22.
 */
object StarterHelper {
    private val mIntentMap: HashMap<Int, Intent> = HashMap()

    fun start(context: Context, intent: Intent, requestCode: Int, bundle: Bundle?) {
/*        val activity = context.activity() ?: return
        start(activity, intent, requestCode, bundle)*/
    }

    fun start(context: Context, intent: Intent, requestCode: Int) {
        start(context, intent, requestCode, null)
    }

    fun start(context: Context, intent: Intent) {
        start(context, intent, -1, null)
    }

    fun start(activity: Activity, intent: Intent, requestCode: Int, bundle: Bundle?) {
        val key = intent.hashCode()
        if (mIntentMap.containsKey(key)) {
            return
        }
        mIntentMap.put(key, intent)
        ActivityCompat.startActivityForResult(activity, intent, requestCode, bundle)
/*        CommonHandler.postDelayed(Runnable {
            mIntentMap.remove(key)
        }, 300L)*/
    }

    fun start(activity: Activity, intent: Intent, requestCode: Int) {
        start(activity, intent, requestCode, null)
    }

    fun start(activity: Activity, intent: Intent) {
        start(activity, intent, -1, null)
    }

    fun start(fragment: Fragment, intent: Intent, requestCode: Int, bundle: Bundle?) {
        val key = intent.hashCode()
        if (mIntentMap.containsKey(key)) {
            return
        }
        mIntentMap.put(key, intent)
        fragment.startActivityForResult(intent, requestCode, bundle)
/*        CommonHandler.postDelayed(Runnable {
            mIntentMap.remove(key)
        }, 300L)*/
    }

    fun start(fragment: Fragment, intent: Intent, requestCode: Int) {
        start(fragment, intent, requestCode, null)
    }

    fun start(fragment: Fragment, intent: Intent) {
        start(fragment, intent, -1, null)
    }

    fun smartShow(starter: Any, bundle: Bundle?=null, tag:String, target: DialogFragment) {
        when (starter){
            is Fragment-> {
                target.arguments = bundle
                target.show(starter.childFragmentManager, tag)
            }
            is FragmentActivity-> {
                target.arguments = bundle
                target.show(starter.supportFragmentManager, tag)
            }
            is Context -> {
/*                val activity = starter.activity() as? FragmentActivity ?:return
                target.arguments = bundle
                target.show(activity.supportFragmentManager, tag)*/
            }
        }
    }

    fun smartStart(starter: Any, intent: Intent, requestCode: Int = -1, bundle: Bundle? = null) {
        when (starter) {
            is Fragment -> start(starter, intent, requestCode, bundle)
            is Activity -> start(starter, intent, requestCode, bundle)
            is Context -> start(starter, intent, requestCode, bundle)
        }
    }

    fun <T : Activity> createIntent(starter: Any, clazz: Class<T>): Intent? {
        when (starter) {
            is Fragment -> {
                val context = starter.context ?: return null
                return Intent(context, clazz)
            }
            is Activity -> {
                return Intent(starter, clazz)
            }
            is Context -> {
                return Intent(starter, clazz)
            }
            else -> return null
        }
    }

    fun <T : Activity> smartStart(starter: Any, clazz: Class<T>, requestCode: Int = -1, bundle: Bundle? = null) {
        when (starter) {
            is Fragment -> {
                val context = starter.context ?: return
                start(starter, Intent(context, clazz), requestCode, bundle)
            }
            is Activity -> {
                start(starter, Intent(starter, clazz), requestCode, bundle)
            }
            is Context -> {
                start(starter, Intent(starter, clazz), requestCode, bundle)
            }
        }
    }
}