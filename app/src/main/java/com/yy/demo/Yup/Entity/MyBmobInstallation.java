package com.yy.demo.Yup.Entity;


import cn.bmob.v3.BmobInstallation;

/**
 * Created by Administrator on 2017/5/3.
 */

public class MyBmobInstallation extends BmobInstallation {

    /**
     * 用户id-这样可以将设备与用户之间进行绑定
     */
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
