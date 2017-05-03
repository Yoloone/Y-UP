package com.yy.demo.Yup;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.push.PushConstants;

/**
 * Created by Administrator on 2017/4/27.
 */
public class MyPushMessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
//            Log.d("bmob", "客户端收到推送内容："+intent.getStringExtra("msg"));
            String jsonStr = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
            String content = null;
            try {
                // 处理JSON
                JSONObject jsonObject = new JSONObject(jsonStr);
                content = jsonObject.getString("alert");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            Notification notify = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle("来自YUP")
                    .setContentText(content)
                    .build();
            manager.notify(1, notify);
        }
    }
}