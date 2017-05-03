package com.yy.demo.Yup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yy.demo.Yup.Entity.Habit;
import com.yy.demo.Yup.Entity.MyUser;
import com.yy.demo.Yup.Entity.day_record;
import com.yy.demo.Yup.Entity.month_record;
import com.yy.demo.Yup.Entity.week_record;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class AddHabitActivity extends AppCompatActivity {
    private TextView tvCreateDay;
    private String dateNowStr;
    private EditText editHabitName, editHabitNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);
        Bmob.initialize(this, "56130d20217e1e62f0bf68d79c838a1d");
        tvCreateDay = (TextView) findViewById(R.id.tv_create_day);
        editHabitName = (EditText) findViewById(R.id.edit_habit_name);
        editHabitNote = (EditText) findViewById(R.id.edit_habit_note);

        //        设置toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_habit_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Date d = new Date();
        System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dateNowStr = sdf.format(d);
        tvCreateDay.setText("创建日期：" + dateNowStr);
    }
//    添加习惯
    public void add(View view){
        String habitName = editHabitName.getText().toString();
        String habitNote = editHabitNote.getText().toString();
        if(!TextUtils.isEmpty(habitName)){
            final Habit habit = new Habit();
            habit.setHabit_name(habitName);
            habit.setHabit_note(habitNote);
            habit.setUser(BmobUser.getCurrentUser(MyUser.class));
            habit.setLasted_days(0);
            habit.setChecked(0);
            habit.setBegin_day(dateNowStr);
            habit.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if(e == null){
                        Toast.makeText(AddHabitActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                        addDayRecord(habit);
                        addMonthRecord(habit);
                        addWeekRecord(habit);
                    }else{
                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });}else {
            Toast.makeText(AddHabitActivity.this,"习惯名称非空",Toast.LENGTH_SHORT).show();
        }
    }
    public void addDayRecord(Habit habit){
        day_record dayRecord = new day_record();
        dayRecord.setHabit(habit);
        dayRecord.setOne_ago(0);dayRecord.setTwo_ago(0);dayRecord.setThree_ago(0);
        dayRecord.setFour_ago(0);dayRecord.setFive_ago(0);dayRecord.setSix_ago(0);
        dayRecord.setSeven_ago(0);
        dayRecord.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){
                    Log.i("bmob","添加日记录成功");
                }else{
                    Log.i("bmob","添加日记录失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
    public void addWeekRecord(Habit habit){
        week_record dayRecord = new week_record();
        dayRecord.setHabit(habit);
        dayRecord.setOne_ago(0);dayRecord.setTwo_ago(0);dayRecord.setThree_ago(0);
        dayRecord.setFour_ago(0);dayRecord.setFive_ago(0);dayRecord.setSix_ago(0);
        dayRecord.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){
                    Log.i("bmob","添加周记录成功");
                }else{
                    Log.i("bmob","添加周记录失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
    public void addMonthRecord(Habit habit){
        month_record dayRecord = new month_record();
        dayRecord.setHabit(habit);
        dayRecord.setOne_ago(0);dayRecord.setTwo_ago(0);dayRecord.setThree_ago(0);
        dayRecord.setFour_ago(0);dayRecord.setFive_ago(0);dayRecord.setSix_ago(0);
        dayRecord.setSeven_ago(0);dayRecord.setEight_ago(0);dayRecord.setNine_ago(0);
        dayRecord.setTen_ago(0);dayRecord.setEleven_ago(0);dayRecord.setTwelve_ago(0);
        dayRecord.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){
                    Log.i("bmob","添加月记录成功");
                }else{
                    Log.i("bmob","添加月记录失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
