package com.yy.demo.Yup.Entity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2017/4/18.
 */

public class MyUser extends BmobUser{
    private Integer stamp_num;
    private BmobFile photo;

    public Integer getStamp_num() {
        return stamp_num;
    }

    public BmobFile getPhoto() {
        return photo;
    }

    public void setStamp_num(Integer stamp_num) {
        this.stamp_num = stamp_num;
    }

    public void setPhoto(BmobFile photo) {
        this.photo = photo;
    }
}
