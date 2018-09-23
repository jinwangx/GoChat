package com.sencent.library.base.helper

import android.content.Context
import android.support.v4.app.Fragment
import com.sencent.library.base.activity.IActivityData
import com.sencent.library.base.activity.IActivityOtherFragmentData

/**
 * 由 jinwangx 创建于 2017/12/8.
 */
class FragmentFrameworkHelper {
    private var mActivityData: IActivityData? = null
    private var mActivityOtherFragmentData: IActivityOtherFragmentData? = null

    fun onAttach(context: Context) {
        mActivityData = context as? IActivityData
        mActivityOtherFragmentData = context as? IActivityOtherFragmentData
    }

    fun onDetach() {
        mActivityData = null
        mActivityOtherFragmentData = null
    }

    fun getTag(fragment: Fragment): String = ""

    fun sendDataToActivity(sender: Fragment, name: String, data: Any?) {
        mActivityData?.onReceiveDataFromFragment(getTag(sender), name, data)
    }

    fun sendDataToOtherFragment(sender: Fragment, receiverTag: String, name: String, data: Any?) {
        mActivityOtherFragmentData?.onReceiveOtherFragmentData(getTag(sender), receiverTag, name, data)
    }

    fun queryActivityData(query: Fragment, name: String) = mActivityData?.onQueryDataFromFragment(getTag(query), name)
}