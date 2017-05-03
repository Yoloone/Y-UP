package com.yy.demo.Yup.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yy.demo.Yup.Entity.stamp_item;
import com.yy.demo.Yup.R;

import java.util.List;

/**
 * Created by Administrator on 2017/4/10.
 */

public class CardStampAdapter extends ArrayAdapter<stamp_item> {
    private Context context;
    private int resource;
    public CardStampAdapter(Context context, int resource, List<stamp_item> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        stamp_item cardStamp = this.getItem(position);
        ImageView cardStampImg = (ImageView) convertView.findViewById(R.id.card_stamp_img);
        TextView cardStampName = (TextView) convertView.findViewById(R.id.card_stamp_name);
        cardStampName.setText(cardStamp.getStamp_item_name());
        Glide.with(context).load(cardStamp.getStamp_item_pic().getFileUrl()).into(cardStampImg);
        return convertView;
    }
}
