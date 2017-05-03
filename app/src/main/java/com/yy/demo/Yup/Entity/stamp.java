package com.yy.demo.Yup.Entity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2017/3/28.
 */

public class stamp extends BmobObject {
    private String stamp_name;
    private BmobFile stamp_picture;
    private Integer num;
    private String introduction;
    private String history;

    public String getIntroduction() {
        return introduction;
    }

    public String getHistory() {
        return history;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public stamp(String stamp_name, BmobFile stamp_picture) {
        this.stamp_name = stamp_name;
        this.stamp_picture = stamp_picture;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getStamp_name() {
        return stamp_name;
    }

    public BmobFile getStamp_picture() {
        return stamp_picture;
    }

    public void setStamp_name(String stamp_name) {
        this.stamp_name = stamp_name;
    }

    public void setStamp_picture(BmobFile stamp_picture) {
        this.stamp_picture = stamp_picture;
    }
}
