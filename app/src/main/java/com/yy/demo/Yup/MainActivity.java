package com.yy.demo.Yup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yy.demo.Yup.Entity.MyUser;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.id.home;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private CircleImageView circleImageView;
    private  FloatingActionButton fab;
    private TextView toolbarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this, "56130d20217e1e62f0bf68d79c838a1d");


        //获取toolbar
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //获取drawerlayout
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        //获取navigationview
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        //获取FloatingActionButton
        fab = (FloatingActionButton)findViewById(R.id.float_btn);
        //获取toolbarTitle
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        //获取actionbar,显示并设置导航图标
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setHomeAsUpIndicator(R.drawable.nav);
        }
        //初始化页面
        replaceFragment(new HabitFrame());
        navigationView.setCheckedItem(R.id.nav_habit);
        toolbarTitle.setText("习惯养成");
        //设置navigationview
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_habit:
                        fab.setVisibility(View.VISIBLE);
                        replaceFragment(new HabitFrame());
                        navigationView.setCheckedItem(R.id.nav_habit);
                        toolbarTitle.setText("习惯养成");
                        break;
                    case R.id.nav_stamp:
                        fab.setVisibility(View.GONE);
                        replaceFragment(new StampFrame());
                        navigationView.setCheckedItem(R.id.nav_stamp);
                        toolbarTitle.setText("邮票收集");
                        break;
                    case R.id.nav_statistic:
                        fab.setVisibility(View.GONE);
                        replaceFragment(new StatisticFrame());
                        navigationView.setCheckedItem(R.id.nav_statistic);
                        toolbarTitle.setText("图表统计");
                        break;
                    case R.id.nav_settings:
                        fab.setVisibility(View.GONE);
                        replaceFragment(new SettingsFrame());
                        navigationView.setCheckedItem(R.id.nav_settings);
                        toolbarTitle.setText("设置");
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        //*悬浮按钮
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddHabitActivity.class);
                startActivity(intent);
              /*  //Snackbar
                Snackbar.make(view,"data deleted",Snackbar.LENGTH_LONG).setAction("undo",new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this,"data restored",Toast.LENGTH_LONG).show();
                    }
                }).show();*/
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout,fragment);
        fragmentTransaction.commit();

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

            //导航键
            case home://注意加android
                mDrawerLayout.openDrawer(GravityCompat.START);
                //获取circleImageView，并加载头像
                if(circleImageView == null)
                    circleImageView = (CircleImageView) findViewById(R.id.circle_img);
                TextView tvMail = (TextView) findViewById(R.id.mail);
                MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
                tvMail.setText(myUser.getEmail());
                if(myUser.getPhoto() == null)
                   Glide.with(this).load(R.drawable.person).into(circleImageView);
                else
                    Glide.with(this).load(myUser.getPhoto().getFileUrl()).into(circleImageView);
/*//                这里跳转到登录页面
                circleImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });*/
                break;

        }
        return true;
    }
    @Override
    public void onBackPressed() {
        return ;
    }
}
