package com.yy.demo.Yup;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yy.demo.Yup.Entity.MyBmobInstallation;
import com.yy.demo.Yup.Entity.MyUser;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static cn.bmob.v3.Bmob.getApplicationContext;


/**
 * Created by Administrator on 2017/4/9.
 */

public class SettingsFrame extends Fragment {
    private Activity context;
    private CircleImageView img_userphoto;
    private ImageView set_msg;
    private LinearLayout lay_sets_password,lay_sets_photo,lay_sets_exit;
    private TextView txt_mail;
    private Uri imageUri;
    private static boolean msgOpen = false;//消息推送的开关

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_frame,container,false);
        context = this.getActivity();

        //获取控件
        txt_mail = (TextView)view.findViewById(R.id.txt_mail);
        img_userphoto = (CircleImageView) view.findViewById(R.id.img_userphoto);
        lay_sets_password = (LinearLayout)view.findViewById(R.id.lay_sets_password);
        lay_sets_photo = (LinearLayout)view.findViewById(R.id.lay_sets_photo);
        lay_sets_exit = (LinearLayout)view.findViewById(R.id.lay_sets_exit);
        set_msg = (ImageView)view.findViewById(R.id.set_msg);
//
        BmobQuery<MyBmobInstallation> query = new BmobQuery<MyBmobInstallation>();
        query.addWhereEqualTo("installationId", BmobInstallation.getInstallationId(context));

        query.findObjects(new FindListener<MyBmobInstallation>() {

            @Override
            public void done(List<MyBmobInstallation> list, BmobException e) {
                if(e == null && list.size() > 0){
                    msgOpen = (list.get(0).getStatus() == 1)? true:false;
                    if(msgOpen)
                        Glide.with(getApplicationContext()).load(R.drawable.open).into(set_msg);
                    else
                        Glide.with(getApplicationContext()).load(R.drawable.off).into(set_msg);
                }else{
                    Log.i("bmob","设备信息更新失败");
                }
            }
        });


        // 获取本地用户对象
        MyUser user = BmobUser.getCurrentUser(MyUser.class);
        if(null == user){
            // 未登录，跳转到登录Or注册界面
            Toast.makeText(getApplicationContext(),
                    "进入登录Or注册界面",Toast.LENGTH_LONG).show();
            startActivity(new Intent(context,LoginActivity.class));
            getActivity().finish();
        }else{
            // 已登录，正在进入应用
            txt_mail.setText(user.getUsername());

            BmobFile avatarFile = user.getPhoto();
            if(avatarFile == null)
                Glide.with(this).load(R.drawable.person).into(img_userphoto);
            else{
            String avatarUrl = avatarFile.getFileUrl();
            Log.d("photo_url:",avatarUrl);
            // 根据头像地址(avatarUrl)下载头像并显示在应用界面中...
            Glide.with(this).load(avatarUrl)
                    .error(R.drawable.pk).into(img_userphoto);}
        }

        //绑定监听事件
        lay_sets_password.setOnClickListener(new OnClickListenerImpl());
        lay_sets_photo.setOnClickListener(new OnClickListenerImpl());
        set_msg.setOnClickListener(new OnClickListenerImpl());
        lay_sets_exit.setOnClickListener(new OnClickListenerImpl());

        img_userphoto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                updatePhoto();//此处实现修改头像方法
                return false;
            }
        });

        return view;
    }

    private class OnClickListenerImpl implements View.OnClickListener{
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.lay_sets_password:
                    Intent update_intent = new Intent(getActivity(), UpdatePasswordActivity.class);
                    startActivity(update_intent);
                    break;
                case R.id.lay_sets_photo:
                    updatePhoto();
                    break;
                case R.id.set_msg:
//                    Toast.makeText(getApplicationContext(),
//                            "设置消息推送",Toast.LENGTH_LONG).show();
                    msgOpen = !msgOpen;
                    if(msgOpen) {
                        Glide.with(getApplicationContext()).load(R.drawable.open).into(set_msg);
                        updateInstallation(1);
                        Toast.makeText(getApplicationContext(),
                            "打开消息推送",Toast.LENGTH_SHORT).show();
                    }else{
                        Glide.with(getApplicationContext()).load(R.drawable.off).into(set_msg);
                        updateInstallation(0);}
                    break;
                case R.id.lay_sets_exit:
                    BmobUser.logOut();
                    //重新登录
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    //防止用户点击返回键，重新进入程序
                    getActivity().finish();
                    break;
                case R.id.img_userphoto:
                    //此处可以实现将图片放大展示,不实现
                    break;
                default:
                    break;
            }
        }
    }

    //进入图库获取头像
    private void updatePhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra("crop", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 1);
    }

    //获取图片的真实地址
    private String getImagePath(Uri uri, String seletion) {
        String path = null;
        Cursor cursor = context.getContentResolver().query(uri, null, seletion, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    //返回图片的uri
                    imageUri = data.getData();
                    //设置图片内容
                    img_userphoto.setImageURI(imageUri);
                    String path = getImagePath(imageUri, null);
                    //将头像上传到数据库
                    uploadPhoto(path);
                }
                break;
            default:
                break;
        }
    }

    //上传到服务器
    private BmobFile uploadPhoto (String path){
        // 创建一个BmobFile对象作为用户头像属性
        final BmobFile avatarFile = new BmobFile(new File(path));
        avatarFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Toast.makeText(getApplicationContext(),
                            "头像上传成功",Toast.LENGTH_SHORT).show();
                    updateUser(avatarFile);
                }else{
                    Toast.makeText(getApplicationContext(),
                            "头像上传失败"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        return avatarFile;
    }

    //更新系统User表的头像
    private void updateUser(BmobFile avatarFile){
        MyUser user = BmobUser.getCurrentUser(MyUser.class);
        if(null == user){
            // 未登录，跳转到登录Or注册界面
            Toast.makeText(getApplicationContext(),
                    "进入登录Or注册界面",Toast.LENGTH_LONG).show();
        }else {
            user.setPhoto(avatarFile);
            user.update(user.getObjectId(),new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Toast.makeText(getApplicationContext(),
                                "头像更新成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "头像更新失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
//
    public void updateInstallation(final int i){
        BmobQuery<MyBmobInstallation> query = new BmobQuery<MyBmobInstallation>();
        query.addWhereEqualTo("installationId", BmobInstallation.getInstallationId(context));

        query.findObjects(new FindListener<MyBmobInstallation>() {

            @Override
            public void done(List<MyBmobInstallation> list, BmobException e) {
                if(e == null && list.size() > 0){
                    MyBmobInstallation mbi = list.get(0);
                    mbi.setStatus(i);
                    mbi.update(mbi.getObjectId(),new UpdateListener() {

                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                Log.i("bmob","设备信息更新成功");
                            }else{
                                Log.i("bmob","设备信息更新失败");
                            }
                        }
                    });
                }else{
                    Log.i("bmob","设备信息更新失败");
                }
            }
        });
    }

}
