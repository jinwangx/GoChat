package com.sencent.mm

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jw.gochatbase.base.fragment.BaseFragment

/**
 * 由 jinwangx 创建于 2018/3/5.
 */
abstract class GoChatBindingFragment<BINDING : ViewDataBinding> : BaseFragment() {
    protected var binding: BINDING? = null
        private set

    abstract fun getLayoutId(): Int

    override fun doInflate(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<BINDING>(inflater, getLayoutId(), container, false)
        this.binding = binding
        return binding.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}