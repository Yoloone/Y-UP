package com.yy.demo.Yup;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.wenchao.cardstack.CardStack;
import com.yy.demo.Yup.Adapter.CardStampAdapter;
import com.yy.demo.Yup.Entity.MyUser;
import com.yy.demo.Yup.Entity.stamp;
import com.yy.demo.Yup.Entity.stamp_item;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ShowStampActivity extends AppCompatActivity {
    private CardStack cardStampStack;
    private TextView tvStampCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_stamp);
//
        tvStampCount = (TextView) findViewById(R.id.card_stamp_count);
        cardStampStack = (CardStack) findViewById(R.id.stamp_stack);
        cardStampStack.setContentResource(R.layout.card_stamp_item);
        cardStampStack.setStackMargin(20);
        //        设置toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.show_stamp_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
//
        MyApplication myApplication = (MyApplication) getApplication();
        stamp stamp = myApplication.getCurrentStamp();
        final List<stamp_item> stampList = new ArrayList<>();
//
        BmobQuery<stamp_item> query = new BmobQuery<>();
        query.addWhereEqualTo("stamp", stamp);
        query.order("num");
        query.findObjects(new FindListener<stamp_item>() {
            @Override
            public void done(List<stamp_item> list, BmobException e) {
                if(e == null){
                    int count = 0;
                    int num = BmobUser.getCurrentUser(MyUser.class).getStamp_num();
                    for(stamp_item stampItem: list){
                        if(stampItem.getNum() <= num ){
                            count++;
                            stampList.add(stampItem);
                        }
                    }
//
                    tvStampCount.setText(count + "/" + list.size());
                    CardStampAdapter cardStampAdapter = new CardStampAdapter(ShowStampActivity.this,R.layout.card_stamp_item,stampList);
                    cardStampStack.setAdapter(cardStampAdapter);
                }else{
                    Log.i("bmob","查找邮票子项失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });


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
