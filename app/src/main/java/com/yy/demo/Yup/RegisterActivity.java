package com.yy.demo.Yup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yy.demo.Yup.Entity.MyUser;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity {
    public EditText editEmail, editPassword, editConfirmPsw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Bmob.initialize(this, "56130d20217e1e62f0bf68d79c838a1d");
        editEmail = (EditText) findViewById(R.id.edit_text_email_reg);
        editPassword = (EditText) findViewById(R.id.edit_text_password_reg);
        editConfirmPsw = (EditText) findViewById(R.id.edit_text_password_confirm);
//        设置toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }
    public void register(View view){
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        String comfirmPsw = editConfirmPsw.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"邮箱不能为空！",Toast.LENGTH_SHORT).show();
        }else if(password.length() < 6){
            Toast.makeText(this,"密码不能少于6位！",Toast.LENGTH_SHORT).show();
        }else if(!password.equals(comfirmPsw)){
            Toast.makeText(this,"密码输入错误！",Toast.LENGTH_SHORT).show();
        }else{
//            BmobUser bu = new BmobUser();
            MyUser bu = new MyUser();
            bu.setUsername(email);
            bu.setPassword(password);
            bu.setEmail(email);
            bu.setStamp_num(0);
            bu.signUp(new SaveListener<MyUser>() {
                @Override
                public void done(MyUser myUser, BmobException e) {
                    if(e == null){
                        Toast.makeText(RegisterActivity.this,"注册成功！"+ myUser.getUsername(),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(RegisterActivity.this,"邮箱格式错误或邮箱已被注册",Toast.LENGTH_SHORT).show();
                        Log.e("register", "done: ",e );
                    }
                }
            });

        }

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
