package com.yy.demo.Yup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yy.demo.Yup.Entity.MyUser;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobUser;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ImageView welcomeImg = (ImageView) findViewById(R.id.welcome_img);
        Glide.with(this).load(R.drawable.bg).into(welcomeImg);
        Bmob.initialize(this, "56130d20217e1e62f0bf68d79c838a1d");
        // 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation().save();
        // 启动推送服务
        BmobPush.startWork(this);
       // handler.sendEmptyMessageAtTime(1,10000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(BmobUser.getCurrentUser(MyUser.class) != null){
                    Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                    startActivity(intent);
                    //finish();
                }else{
                    Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
                    startActivity(intent);
                   // finish();
                }
            }
        }).start();

    }
    public void enter(View view){
        if(BmobUser.getCurrentUser(MyUser.class) != null){
            Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
            startActivity(intent);
           // finish();
        }else{
            Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
            startActivity(intent);
           // finish();
        }
    }
}
