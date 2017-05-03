package com.yy.demo.Yup.Entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/4/23.
 */

public class day_record extends BmobObject {
    private Habit habit;
    private  Integer one_ago;
    private  Integer two_ago;
    private  Integer three_ago;
    private  Integer four_ago;
    private  Integer five_ago;
    private  Integer six_ago;
    private  Integer seven_ago;

    public Habit getHabit() {
        return habit;
    }

    public Integer getOne_ago() {
        return one_ago;
    }

    public Integer getTwo_ago() {
        return two_ago;
    }

    public Integer getThree_ago() {
        return three_ago;
    }

    public Integer getFour_ago() {
        return four_ago;
    }

    public Integer getFive_ago() {
        return five_ago;
    }

    public Integer getSix_ago() {
        return six_ago;
    }

    public Integer getSeven_ago() {
        return seven_ago;
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
    }

    public void setOne_ago(Integer one_ago) {
        this.one_ago = one_ago;
    }

    public void setTwo_ago(Integer two_ago) {
        this.two_ago = two_ago;
    }

    public void setThree_ago(Integer three_ago) {
        this.three_ago = three_ago;
    }

    public void setFour_ago(Integer four_ago) {
        this.four_ago = four_ago;
    }

    public void setFive_ago(Integer five_ago) {
        this.five_ago = five_ago;
    }

    public void setSix_ago(Integer six_ago) {
        this.six_ago = six_ago;
    }

    public void setSeven_ago(Integer seven_ago) {
        this.seven_ago = seven_ago;
    }
}
