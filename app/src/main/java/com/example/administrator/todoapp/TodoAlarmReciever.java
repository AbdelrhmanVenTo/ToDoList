package com.example.administrator.todoapp;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import javax.crypto.Cipher;

public class TodoAlarmReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent alarmIntent) {
        String title=alarmIntent.getStringExtra("title");
        String content=alarmIntent.getStringExtra("content");
        showNotification(title,content,context);
    }

   public void showNotification(String title,String content,Context context){
       NotificationCompat.Builder builder
               =new NotificationCompat.Builder(context,MyApplication.CHANNEL_ID);
       builder.setContentTitle(title)
               .setContentText(content)
               .setSmallIcon(R.mipmap.ic_launcher);

       NotificationManager notificationManager
               = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
       notificationManager.notify(((int) System.currentTimeMillis()),builder.build());
    }
}
