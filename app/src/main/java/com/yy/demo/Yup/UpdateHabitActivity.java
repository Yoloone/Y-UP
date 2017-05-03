package com.yy.demo.Yup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yy.demo.Yup.Entity.Habit;
import com.yy.demo.Yup.Entity.day_record;
import com.yy.demo.Yup.Entity.month_record;
import com.yy.demo.Yup.Entity.week_record;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class UpdateHabitActivity extends AppCompatActivity {
    private TextView tvCreateDay, tvLastedDay;
    private EditText editHabitName, editHabitNote;
    private Habit habit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_habit);
        tvLastedDay = (TextView) findViewById(R.id.tv_show_lastedDays);
        tvCreateDay = (TextView) findViewById(R.id.tv_show_day);
        editHabitName = (EditText) findViewById(R.id.edit_show_name);
        editHabitNote = (EditText) findViewById(R.id.edit_show_note);
        habit = (Habit) getIntent().getSerializableExtra("habit");
        tvLastedDay.setText("打卡天数："+habit.getLasted_days());
        tvCreateDay.setText("创建时间："+habit.getBegin_day());
        editHabitName.setText(habit.getHabit_name());
        editHabitNote.setText(habit.getHabit_note());

        //        设置toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.update_habit_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    public void save(View view){
        String habitName = editHabitName.getText().toString();
        String habitNote = editHabitNote.getText().toString();
        if(!TextUtils.isEmpty(habitName)){

            habit.setHabit_name(habitName);
            habit.setHabit_note(habitNote);
            habit.update(habit.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e == null){
                        Toast.makeText(UpdateHabitActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                    }else{
                        Log.i("bmob","习惯修改失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });
          }else {
            Toast.makeText(UpdateHabitActivity.this,"习惯名称非空",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_habit_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.delete:
                showDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确定删除？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteDayRecord();
                deleteWeekRecord();
                deleteMonthRecord();
                deleteHabit();
            }
        });
        builder.setNegativeButton("取消",null);
        AlertDialog dialog  = builder.create();
        dialog.show();
    }

    private void deleteHabit() {
        habit.delete(habit.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    Toast.makeText(UpdateHabitActivity.this,"删除成功",Toast.LENGTH_LONG).show();
                    Log.i("bmob","删除习惯成功");
                }else{
                    Log.i("bmob","删除习惯失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
    private void deleteDayRecord() {
        BmobQuery<day_record> query = new BmobQuery<>();
        query.addWhereEqualTo("habit", habit);
        query.findObjects(new FindListener<day_record>() {
            @Override
            public void done(List<day_record> list, BmobException e) {
                if(e == null){
//                   更新当日打卡信息
                    day_record dayRecord = list.get(0);

                    dayRecord.delete(dayRecord.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                Log.i("bmob","删除日记录成功");
                            }else{
                                Log.i("bmob","删除日记录失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });

                }else{
                    Log.i("bmob","删除日记录失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
    private void deleteWeekRecord() {
        BmobQuery<week_record> query = new BmobQuery<>();
        query.addWhereEqualTo("habit", habit);
        query.findObjects(new FindListener<week_record>() {
            @Override
            public void done(List<week_record> list, BmobException e) {
                if (e == null) {
//                   更新当周打卡信息
                    week_record weekRecord = list.get(0);

                    weekRecord.delete(weekRecord.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Log.i("bmob", "删除周记录成功");
                            } else {
                                Log.i("bmob", "删除周记录失败：" + e.getMessage() + "," + e.getErrorCode());
                            }
                        }
                    });

                } else {
                    Log.i("bmob", "删除周记录失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }
    private void deleteMonthRecord() {
        BmobQuery<month_record> query = new BmobQuery<>();
        query.addWhereEqualTo("habit", habit);
        query.findObjects(new FindListener<month_record>() {
            @Override
            public void done(List<month_record> list, BmobException e) {
                if(e == null){
//                   更新当周打卡信息
                    month_record monthRecord = list.get(0);
                    monthRecord.delete(monthRecord.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                Log.i("bmob","删除月记录成功");
                            }else{
                                Log.i("bmob","删除月记录失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });

                }else{
                    Log.i("bmob","删除月记录失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
}
