package com.yy.demo.Yup.Entity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2017/4/25.
 */

public class stamp_item extends BmobObject{
    private Integer num;
    private String stamp_item_name;
    private stamp stamp;
    private BmobFile stamp_item_pic;

    public BmobFile getStamp_item_pic() {
        return stamp_item_pic;
    }

    public void setStamp_item_pic(BmobFile stamp_item_pic) {
        this.stamp_item_pic = stamp_item_pic;
    }

    public Integer getNum() {
        return num;
    }

    public String getStamp_item_name() {
        return stamp_item_name;
    }

    public stamp getStamp() {
        return stamp;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public void setStamp_item_name(String stamp_item_name) {
        this.stamp_item_name = stamp_item_name;
    }

    public void setStamp(stamp stamp) {
        this.stamp = stamp;
    }
}
