package com.yy.demo.Yup;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yy.demo.Yup.Adapter.HabitAdapter;
import com.yy.demo.Yup.Entity.Habit;
import com.yy.demo.Yup.Entity.MyUser;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/4/9.
 */

public class HabitFrame extends Fragment {
    private Activity context;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private MyApplication myApplication;
    private HabitAdapter habitAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.habits_frame,container,false);
        context = this.getActivity();
      //*下拉刷新
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.habit_swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                refreshStamps();
            }
        });
        myApplication = (MyApplication)context.getApplication();
//        Log.i("myapp",myApplication.getMyUser().getObjectId());
//        Log.i("cache-user", BmobUser.getCurrentUser(MyUser.class).getObjectId());
        listView = (ListView) view.findViewById(R.id.habit_list);
        initData();
        return view;
    }

    private void initData() {
        BmobQuery<Habit> query = new BmobQuery<>();
        query.addWhereEqualTo("user", BmobUser.getCurrentUser(MyUser.class));
        query.findObjects(new FindListener<Habit>() {
            @Override
            public void done(List<Habit> list, BmobException e) {
                if(e == null){
                    //*设置适配器
                   myApplication.setHabitList(list);
                    habitAdapter = new HabitAdapter(context,R.layout.habit_item,list);
                    listView.setAdapter(habitAdapter);
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
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
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
}
