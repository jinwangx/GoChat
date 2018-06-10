package com.jw.gochat.fragment;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;

import com.jw.gochat.R;
import com.jw.gochat.databinding.FragmentMeBinding;
import com.jw.gochatbase.BaseFragment;

/**
 * Created by Administrator on 2017/3/26.
 */


public class MeFra extends BaseFragment {

    private FragmentMeBinding mBinding;

    @Override
    public View bindView() {
        mBinding = DataBindingUtil.inflate(getActivity().getLayoutInflater(), R.layout.fragment_me, null, false);
        return mBinding.getRoot();
    }

}
