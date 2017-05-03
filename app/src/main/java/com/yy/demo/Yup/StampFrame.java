package com.yy.demo.Yup;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yy.demo.Yup.Adapter.StampAdapter;
import com.yy.demo.Yup.Entity.MyUser;
import com.yy.demo.Yup.Entity.stamp;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/4/9.
 */

public class StampFrame extends Fragment{
    private RecyclerView recyclerView;
    private StampAdapter stampAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvCountStamps;
    private Activity context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stamp_frame,container,false);
        context = this.getActivity();
        tvCountStamps = (TextView) view.findViewById(R.id.tv_count_stamps);
        tvCountStamps.setText("我的收集："+ BmobUser.getCurrentUser(MyUser.class).getStamp_num()+"张");
        //*加载卡片
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_stamp_view);
        GridLayoutManager layoutManager = new GridLayoutManager(context,2);
        recyclerView.setLayoutManager(layoutManager);
        initData();


        //*下拉刷新
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                refreshStamps();
            }
        });
        return view;
    }

    private void refreshStamps() {
        new Thread(new Runnable(){

            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        initData();
//                        stampAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    public void initData(){
      BmobQuery<stamp> query = new BmobQuery<>();
        query.order("num");
        query.findObjects(new FindListener<stamp>() {
            @Override
            public void done(List<stamp> list, BmobException e) {
              if(e == null){
             //*设置适配器
                  stampAdapter = new StampAdapter(list);
                  recyclerView.setAdapter(stampAdapter);
             }else{
                  Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
              }
            }
        });
    }
}
