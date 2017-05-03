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

public class SlidingBackgroundFragment extends Fragment {
    private Activity context;
    private TextView txt_stamp_history;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sliding_background_fragment,container,false);
        context = this.getActivity();
        txt_stamp_history = (TextView) view.findViewById(R.id.txt_stamp_history);
        //Log.d("getIntroduction,getArguments().getString("history"));
        MyApplication myApplication = (MyApplication) context.getApplication();

        txt_stamp_history.setText(myApplication.getCurrentStamp().getHistory());
        return view;
    }
}
