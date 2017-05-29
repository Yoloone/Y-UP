package com.yy.demo.Yup.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.yy.demo.Yup.Entity.Habit;
import com.yy.demo.Yup.Entity.MyUser;
import com.yy.demo.Yup.Entity.day_record;
import com.yy.demo.Yup.Entity.month_record;
import com.yy.demo.Yup.Entity.stamp_item;
import com.yy.demo.Yup.Entity.week_record;
import com.yy.demo.Yup.R;
import com.yy.demo.Yup.UpdateHabitActivity;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2017/4/19.
 */

public class HabitAdapter extends ArrayAdapter<Habit>{
    private int resourceId;
    public HabitAdapter(Context context, int resource, List<Habit> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Habit habit = this.getItem(position);
        final ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.CheckBtn = (Button) convertView.findViewById(R.id.btn_check);
            viewHolder.habitNameTv = (TextView) convertView.findViewById(R.id.tv_habit_name);
            viewHolder.habitDaysTv = (TextView) convertView.findViewById(R.id.tv_habit_days);
            viewHolder.touchLayout = convertView.findViewById(R.id.touch_layout);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(habit.getChecked() == 1){

            viewHolder.CheckBtn.setText("已打卡");
            viewHolder.CheckBtn.setTextColor(Color.parseColor("#398f3d"));
            viewHolder.CheckBtn.setBackgroundColor(Color.TRANSPARENT);

        }

        viewHolder.habitNameTv.setText(habit.getHabit_name());
        viewHolder.habitDaysTv.setText("打卡"+habit.getLasted_days()+"天");
//
        viewHolder.CheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(habit.getChecked() == 0)
                    showDialog(habit, viewHolder);
            }
        });
//
        viewHolder.touchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getContext(),habit.getHabit_name(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(),UpdateHabitActivity.class);
                intent.putExtra("habit",(Serializable) habit);
                getContext().startActivity(intent);
            }
        });
        return convertView;
    }
    static class ViewHolder{
        Button CheckBtn;
        TextView habitNameTv;
        TextView habitDaysTv;
        View touchLayout;
    }

    public void showDialog(final Habit habit, final ViewHolder viewHolder){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("确定打卡吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    updateHabit(habit, viewHolder);
                    updateDayRecord(habit);
                    updateWeekRecord(habit);
                    updateMonthRecord(habit);
            }
        });
        builder.setNegativeButton("取消",null);
        AlertDialog dialog  = builder.create();
        dialog.show();
    }
    public void updateHabit(final Habit habit, final ViewHolder viewHolder){
        habit.setChecked(1);
        int days = habit.getLasted_days() + 1;
        habit.setLasted_days(days);
        habit.update(habit.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.i("bmob","更新成功");
                    viewHolder.CheckBtn.setText("已打卡");
                    viewHolder.CheckBtn.setTextColor(Color.parseColor("#398f3d"));
                    viewHolder.CheckBtn.setBackgroundColor(Color.TRANSPARENT);
                    viewHolder.habitDaysTv.setText("打卡"+habit.getLasted_days()+"天");
//                    判断是否赠送邮票
                    int num = habit.getLasted_days();
                    if(num % 7 == 0){
                        showAddStampDialog();
                    }
                }else{
                    Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    private void showAddStampDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
        myUser.setStamp_num(myUser.getStamp_num()+1);
//        显示获得的邮票
        BmobQuery<stamp_item> query = new BmobQuery<>();
        query.addWhereEqualTo("num", myUser.getStamp_num());
        query.findObjects(new FindListener<stamp_item>() {
            @Override
            public void done(List<stamp_item> list, BmobException e) {
                if(e == null){
                    builder.setTitle("恭喜你获得邮票一张！\n"+list.get(0).getStamp_item_name());
                    builder.setPositiveButton("确定",null);
                    AlertDialog dialog  = builder.create();
                    dialog.show();
                }else{
                    Log.i("bmob","查找邮票子项失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
//        更新用户邮票数
        myUser.update(myUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    Log.i("bmob","用户邮票收集成功");
                }else{
                    Log.i("bmob","用户邮票收集失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });


    }

    public void updateDayRecord(Habit habit){
        BmobQuery<day_record> query = new BmobQuery<>();
        query.addWhereEqualTo("habit", habit);
        query.findObjects(new FindListener<day_record>() {
            @Override
            public void done(List<day_record> list, BmobException e) {
                if(e == null){
//                   更新当日打卡信息
                    day_record dayRecord = list.get(0);
                    dayRecord.setSeven_ago(1);
                    dayRecord.update(dayRecord.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                Log.i("bmob","更新日记录成功");
                            }else{
                                Log.i("bmob","更新日记录失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });

                }else{
                    Log.i("bmob","获取日记录失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });

    }
    public void updateWeekRecord(Habit habit) {
        BmobQuery<week_record> query = new BmobQuery<>();
        query.addWhereEqualTo("habit", habit);
        query.findObjects(new FindListener<week_record>() {
            @Override
            public void done(List<week_record> list, BmobException e) {
                if (e == null) {
//                   更新当周打卡信息
                    week_record weekRecord = list.get(0);
                    weekRecord.setSix_ago(weekRecord.getSix_ago() + 1);
                    weekRecord.update(weekRecord.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Log.i("bmob", "更新周记录成功");
                            } else {
                                Log.i("bmob", "更新周记录失败：" + e.getMessage() + "," + e.getErrorCode());
                            }
                        }
                    });

                } else {
                    Log.i("bmob", "获取周记录失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }
    public void updateMonthRecord(Habit habit){
        BmobQuery<month_record> query = new BmobQuery<>();
        query.addWhereEqualTo("habit", habit);
        query.findObjects(new FindListener<month_record>() {
            @Override
            public void done(List<month_record> list, BmobException e) {
                if(e == null){
//                   更新当周打卡信息
                    month_record monthRecord = list.get(0);
                    monthRecord.setTwelve_ago(monthRecord.getTwelve_ago()+1);
                    monthRecord.update(monthRecord.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                Log.i("bmob","更新月记录成功");
                            }else{
                                Log.i("bmob","更新月记录失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });

                }else{
                    Log.i("bmob","获取月记录失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });

    }
}
