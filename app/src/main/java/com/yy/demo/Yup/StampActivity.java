package com.yy.demo.Yup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yy.demo.Yup.Adapter.ViewPagerAdapter;
import com.yy.demo.Yup.Entity.stamp;

public class StampActivity extends AppCompatActivity {

    private MyApplication myApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stamp);
//
        myApplication = (MyApplication) getApplication();
        stamp stamp  = myApplication.getCurrentStamp();
        String stampName = stamp.getStamp_name();
        String stampImageId = stamp.getStamp_picture().getFileUrl();

//        初始化相关布局，组件
        Toolbar toolbar = (Toolbar) findViewById(R.id.stamp_toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView stampImage = (ImageView) findViewById(R.id.stamp_information_img);
        //        设置toolbar
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
//        加载可折叠状态栏的标题和图片
        collapsingToolbarLayout.setTitle(stampName);
        Glide.with(this).load(stampImageId).into(stampImage);
//        获取TabLayout和ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        ViewPager viewPager  = (ViewPager) findViewById(R.id.sliding_viewPager);
//        设置viewPager
        setupViewPager(viewPager);
//        添加tab标签
        tabLayout.addTab(tabLayout.newTab().setText("简介"));
        tabLayout.addTab(tabLayout.newTab().setText("背景"));
//        绑定tabLayout和viewPager
        tabLayout.setupWithViewPager(viewPager);
//        悬浮按钮
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_show_stamps);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StampActivity.this, ShowStampActivity.class);
                startActivity(intent);
            }
        });
    }

//设置ViewPager
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter slidingAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        slidingAdapter.addFragment(new SlidingIntroductionFragment(),"简介");
        slidingAdapter.addFragment(new SlidingBackgroundFragment(),"背景");
        viewPager.setAdapter(slidingAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
