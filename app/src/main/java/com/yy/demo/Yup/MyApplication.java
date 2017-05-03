package com.yy.demo.Yup;

import android.app.Application;

import com.yy.demo.Yup.Entity.Habit;
import com.yy.demo.Yup.Entity.MyUser;
import com.yy.demo.Yup.Entity.stamp;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19.
 */

public class MyApplication extends Application {
    private MyUser myUser;
    private List<Habit> habitList;
    private stamp currentStamp;

    public stamp getCurrentStamp() {
        return currentStamp;
    }

    public void setCurrentStamp(stamp currentStamp) {
        this.currentStamp = currentStamp;
    }

    public MyUser getMyUser() {
        return myUser;
    }

    public void setMyUser(MyUser myUser) {
        this.myUser = myUser;
    }

    public List<Habit> getHabitList() {
        return habitList;
    }

    public void setHabitList(List<Habit> habitList) {
        this.habitList = habitList;
    }
}
