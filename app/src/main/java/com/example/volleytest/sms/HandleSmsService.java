package com.example.volleytest.sms;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by djc on 2017/3/24.
 */

public class HandleSmsService extends IntentService {
    final String TAG = "HandleSmsService";

    public HandleSmsService() {
        super("HandleSmsService");
    }

    @Override
    public void onCreate() {
        // TODO 自动生成的方法存根
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "begin onHandleIntent() in " + this);
        String senderNumber = intent.getExtras().getString("senderNumber");
        String msgTxt = intent.getExtras().getString("msgTxt");
        String receiveTime = intent.getExtras().getString("receiveTime");
        //这里可以上传数据到服务器，可以是非常耗时的任务。（非主线程）
        Log.i(TAG, "发送人：" + senderNumber + "  短信内容：" + msgTxt + "接受时间：" + receiveTime);
        //Volley
//        try {
//            Thread.sleep(10 * 60 * 1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

}
