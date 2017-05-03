package com.yy.demo.Yup.Entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/3/15.
 */

public class person extends BmobObject {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
