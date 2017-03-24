package com.example.volleytest.sms;

/**
 * Created by djc on 2017/3/24.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            // 后边的XXX.class就是要启动的服务
//			 Intent service = new Intent(context, VoiceService.class);
//			 context.startService(service);
            Log.v(TAG, "开机自动服务自动启动.....");
            // 启动应用，参数为需要自动启动的应用的包名
//            Intent intent1 = context.getPackageManager()
//                    .getLaunchIntentForPackage(
//                            "cn.joinlink.intelligenhomevoice");
//            context.startActivity(intent1);
        }
    }
}
