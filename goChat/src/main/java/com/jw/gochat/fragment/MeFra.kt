package com.jw.gochat.fragment

import android.databinding.DataBindingUtil
import android.view.View
import com.jw.gochat.R
import com.jw.gochat.databinding.FragmentMeBinding
import com.jw.gochatbase.BaseFragment

/**
 * Created by Administrator on 2017/3/26.
 */


class MeFra : BaseFragment() {

    private var mBinding: FragmentMeBinding? = null

    public override fun bindView(): View {
        mBinding = DataBindingUtil.inflate(activity!!.layoutInflater, R.layout.fragment_me, null, false)
        return mBinding!!.root
    }
}