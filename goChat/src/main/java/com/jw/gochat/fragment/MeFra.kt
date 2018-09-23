package com.jw.gochat.fragment

import android.os.Bundle
import com.jw.gochat.R
import com.jw.gochat.databinding.FragmentMeBinding
import com.sencent.mm.GoChatBindingFragment

/**
 * Created by Administrator on 2017/3/26.
 */


class MeFra : GoChatBindingFragment<FragmentMeBinding>() {

    override fun getLayoutId() = R.layout.fragment_me

    override fun doConfig(arguments: Bundle?) {
    }
}