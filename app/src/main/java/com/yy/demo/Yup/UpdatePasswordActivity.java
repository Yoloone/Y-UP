package com.yy.demo.Yup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class UpdatePasswordActivity extends AppCompatActivity {

    private ImageView back_img;
    private EditText edt_old_password,edt_new_password,edt_again_password;
    private String old_password,new_password,again_password;
    private Button update_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        //获取组件
        edt_old_password = (EditText)findViewById(R.id.edt_old_password);
        edt_new_password = (EditText)findViewById(R.id.edt_new_password);
        edt_again_password = (EditText)findViewById(R.id.edt_again_password);
        update_btn = (Button) findViewById(R.id.update_btn);
        back_img = (ImageView) findViewById(R.id.back_img);

        //监听事件返回设置界面
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //监听事件处理修改密码
        update_btn.setOnClickListener(new OnClickListenerImpl());
    }

    private class OnClickListenerImpl implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            old_password = edt_old_password.getText().toString();
            new_password = edt_new_password.getText().toString();
            again_password = edt_again_password.getText().toString();

            //内容不能为空
            if(!old_password.isEmpty()&&!new_password.isEmpty()&&!again_password.isEmpty()){
                //首先判断两个新密码是否相同
                if(new_password.equals(again_password))
                    updatePassword();
                else
                    Toast.makeText(getApplicationContext(),
                            "两次输入的密码请保持相同",Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(getApplicationContext(),
                        "请检查是否存在未填项目",Toast.LENGTH_SHORT).show();
        }
    }

    //修改密码
    private void updatePassword(){
        BmobUser.updateCurrentUserPassword(old_password, new_password, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Toast.makeText(getApplicationContext(),
                            "密码修改成功，可以用新密码进行登录啦",Toast.LENGTH_SHORT).show();
                    //退出登录
                    BmobUser.logOut();
                    //重新登录
                    startActivity(new Intent(UpdatePasswordActivity.this,LoginActivity.class));
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),
                            "密码修改失败"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
}
