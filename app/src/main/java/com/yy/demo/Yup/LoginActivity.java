package com.yy.demo.Yup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yy.demo.Yup.Entity.MyUser;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class LoginActivity extends AppCompatActivity {
    public EditText editEmail, editPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bmob.initialize(this, "56130d20217e1e62f0bf68d79c838a1d");

        editEmail = (EditText) findViewById(R.id.edit_text_email);
        editPassword = (EditText) findViewById(R.id.edit_text_password);

    }
    public void login(View view){
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        final MyApplication myApplication = (MyApplication) this.getApplication();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"请输入邮箱地址！",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"请输入密码！",Toast.LENGTH_SHORT).show();
        }else{
            BmobUser.loginByAccount(email,password, new LogInListener<MyUser>() {
                @Override
                public void done(MyUser myUser, BmobException e) {
                    if(myUser != null){
                       // if(myUser.getEmailVerified()){
                           // Toast.makeText(LoginActivity.this,"登录成功！"+myUser.getUsername(),Toast.LENGTH_SHORT).show();
                            myApplication.setMyUser(myUser);
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                       /* }else{
                            Toast.makeText(LoginActivity.this,"邮箱验证未通过，请先验证邮箱！"+myUser.getUsername(),Toast.LENGTH_SHORT).show();
                        }*/

                    }else{
                        Toast.makeText(LoginActivity.this,"账号或密码错误！",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void goToRegister(View view){
         Intent intent = new Intent(this,RegisterActivity.class);
         startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
