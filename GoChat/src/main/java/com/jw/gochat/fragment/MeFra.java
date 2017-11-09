package com.jw.gochat.fragment;

import android.view.View;

import com.jw.gochat.R;
import com.jw.gochat.base.BaseFragment;

/**
 * Created by Administrator on 2017/3/26.
 */


public class MeFra extends BaseFragment {

    @Override
    public View bindView() {
        View view=View.inflate(getActivity(),R.layout.fragment_me,null);
        return view;
    }

}
