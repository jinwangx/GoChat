package com.sencent.library.base.helper

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.sencent.library.base.fragment.IFragmentData
import com.sencent.library.base.fragment.IOtherFragmentData

/**
 * 由 jinwangx 创建于 2017/12/8.
 */
class ActivityFrameworkHelper {
    private fun findFragment(fragment: Fragment, tag: String): Fragment? {
        val subFragmentList = fragment.childFragmentManager.fragments ?: return null
        for (subFragment in subFragmentList) {
             if (subFragment.tag == tag) {
                return subFragment
             } else {
                 val result = findFragment(subFragment, tag)
                 if (result != null) {
                     return result
                 }
            }
        }
        return null
    }

    fun getManagerFragment(activity: FragmentActivity, tag: String): Fragment? {
        val fragmentList = activity.supportFragmentManager.fragments ?: return null
        for (fragment in fragmentList) {
            if (tag == fragment.tag) {
                return fragment
            }
        }
        for (fragment in fragmentList) {
            val result = findFragment(fragment, tag)
            if (result != null) {
                return result
            }
        }
        return null
    }

    fun getManagerFragmentCount(activity: FragmentActivity): Int {
        val fragmentList = activity.supportFragmentManager.fragments ?: return 0
        return fragmentList.size
    }

    fun sendDataToFragment(activity: FragmentActivity, tag: String, name: String, data: Any?) {
        val fragment = getManagerFragment(activity, tag) ?: return
        if (fragment is IFragmentData) {
            fragment.onReceiveDataFromActivity(name, data)
        }
    }

    fun onReceiveOtherFragmentData(activity: FragmentActivity, senderTag: String, receiverTag: String, name: String, data: Any?) {
        val fragment = getManagerFragment(activity, receiverTag) ?: return
        if (fragment is IOtherFragmentData) {
            fragment.onReceiveDataFromOtherFragment(senderTag, name, data)
        }
    }

    fun queryFragmentData(activity: FragmentActivity, tag: String, name: String):Any? {
        val fragment = getManagerFragment(activity, tag) ?: return null
        return if (fragment is IFragmentData) {
            fragment.onQueryDataFromActivity(name)
        } else {
            null
        }
    }
}