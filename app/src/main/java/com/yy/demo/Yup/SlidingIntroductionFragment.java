package com.yy.demo.Yup;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/4/9.
 */

public class SlidingIntroductionFragment extends Fragment {
    private Activity context;
    private TextView txt_stamp_intro;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sliding_introduction_fragment,container,false);
        context = this.getActivity();
        txt_stamp_intro = (TextView) view.findViewById(R.id.txt_stamp_intro);
        MyApplication myApplication = (MyApplication) context.getApplication();
        txt_stamp_intro.setText(myApplication.getCurrentStamp().getIntroduction());
        return view;
    }
}
