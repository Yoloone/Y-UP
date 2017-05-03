package com.yy.demo.Yup.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yy.demo.Yup.Entity.MyUser;
import com.yy.demo.Yup.Entity.stamp;
import com.yy.demo.Yup.Entity.stamp_item;
import com.yy.demo.Yup.MyApplication;
import com.yy.demo.Yup.R;
import com.yy.demo.Yup.StampActivity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/3/28.
 */

public class StampAdapter extends RecyclerView.Adapter<StampAdapter.ViewHolder>{
    private Activity mContext;
    private List<stamp> mStampList;
    private List<stamp_item> stamp_items;
    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView stampImg;
        TextView stampName;
        TextView stampCount;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView;
            stampImg = (ImageView)itemView.findViewById(R.id.stamp_img);
            stampName = (TextView)itemView.findViewById(R.id.stamp_name);
            stampCount = (TextView) itemView.findViewById(R.id.stamp_count);
        }
    }
    public StampAdapter(List<stamp> stampList){
        mStampList = stampList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null)
            mContext = (Activity) parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.stamp_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        //设置点击事件
        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                stamp stamp = mStampList.get(position);
                MyApplication myApplication = (MyApplication) mContext.getApplication();
                myApplication.setCurrentStamp(stamp);
                Intent intent = new Intent(mContext, StampActivity.class);
                mContext.startActivity(intent);
    }
});
        return holder;
        }

@Override
public void onBindViewHolder(final ViewHolder holder, int position) {
        stamp stamp1 = mStampList.get(position);
        holder.stampName.setText(stamp1.getStamp_name());
        String url = stamp1.getStamp_picture().getFileUrl();
        Glide.with(mContext).load(url).into(holder.stampImg);
//
        BmobQuery<stamp_item> query = new BmobQuery<>();
        query.addWhereEqualTo("stamp", stamp1);
        query.findObjects(new FindListener<stamp_item>() {
            @Override
            public void done(List<stamp_item> list, BmobException e) {
                if(e == null){
                    int count = 0;
                    int num = BmobUser.getCurrentUser(MyUser.class).getStamp_num();
                    for(stamp_item stampItem: list){
                        if(stampItem.getNum() <= num )
                            count++;
                    }
                    holder.stampCount.setText(count + "/" + list.size());
                }else{
                    Log.i("bmob","查找邮票子项失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStampList.size();
    }



}
