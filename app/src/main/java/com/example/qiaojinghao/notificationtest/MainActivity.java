package com.example.qiaojinghao.notificationtest;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qiaojinghao.notificationtest.utils.NotificationUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button sendChatBtn;
    Button sendSubscribeBtn;

    NotificationCompat.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initData();
        initAction();
    }

    private void initUI(){
        sendChatBtn = findViewById(R.id.send_chat_btn);
        sendSubscribeBtn = findViewById(R.id.send_subscribe_btn);
    }

    private void initAction(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId = NotificationUtils.NOTIF_CHANNEL_CHAT;
            String channelName = "聊天消息";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationUtils.createNotificationChannel(this, channelId, channelName, importance);

            channelId = NotificationUtils.NOTIF_CHANNEL_SUBSCRIBE;
            channelName = "订阅消息";
            importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationUtils.createNotificationChannel(this, channelId, channelName, importance);
        }

        sendChatBtn.setOnClickListener(this);
        sendSubscribeBtn.setOnClickListener(this);
    }

    private void initData(){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_chat_btn:
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                    NotificationChannel channel = manager.getNotificationChannel(NotificationUtils.NOTIF_CHANNEL_CHAT);
                    if(channel.getImportance() < NotificationManager.IMPORTANCE_HIGH){
                        Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                        intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
                        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                        startActivity(intent);
                        Toast.makeText(this, "请将通知打开，并设置重要程度为紧急", Toast.LENGTH_LONG).show();
                    }
                }
                builder = new NotificationCompat.Builder(this, NotificationUtils.NOTIF_CHANNEL_CHAT);
                builder.setContentText("今天中午吃什么？")
                        .setContentTitle("收到一条聊天信息")
                        // smallIcon 如果使用mipmap中的文件，则会导致手机崩溃，必须重启
                        .setSmallIcon(R.drawable.android)
                        .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.android));
                NotificationUtils.sendNotification(this, 1, builder);
                break;
            case R.id.send_subscribe_btn:
                builder = new NotificationCompat.Builder(this, NotificationUtils.NOTIF_CHANNEL_SUBSCRIBE);
                builder.setContentTitle("收到一条订阅信息")
                        .setContentText("地铁沿线30万商铺抢购中！")
                        .setSmallIcon(R.drawable.android)
                        .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.android));
                NotificationUtils.sendNotification(this, 2, builder);
                break;
        }
    }
}
