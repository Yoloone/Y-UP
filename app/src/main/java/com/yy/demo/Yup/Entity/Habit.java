package com.yy.demo.Yup.Entity;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/4/19.
 */

public class Habit extends BmobObject implements Serializable{
    private String habit_name;
    private Integer lasted_days;
    private Integer isChecked;
    private MyUser user;
    private String habit_note;
    private String begin_day;

    public String getBegin_day() {
        return begin_day;
    }

    public void setBegin_day(String begin_day) {
        this.begin_day = begin_day;
    }

    public String getHabit_name() {
        return habit_name;
    }

    public Integer getLasted_days() {
        return lasted_days;
    }

    public Integer getChecked() {
        return isChecked;
    }

    public void setHabit_name(String habit_name) {
        this.habit_name = habit_name;
    }

    public void setLasted_days(Integer lasted_days) {
        this.lasted_days = lasted_days;
    }

    public void setChecked(Integer checked) {
        isChecked = checked;
    }

    public MyUser getUser() {
        return user;
    }

    public String getHabit_note() {
        return habit_note;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public void setHabit_note(String habit_note) {
        this.habit_note = habit_note;
    }
}
