package com.example.volleytest.sms;

/**
 * Created by djc on 2017/3/24.
 */

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;


public class SMSBroadcastReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        SmsMessage msg = null;
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdusObj = (Object[]) bundle.get("pdus");
            for (Object p : pdusObj) {
                msg = SmsMessage.createFromPdu((byte[]) p);

                String msgTxt = msg.getMessageBody();//得到消息的内容

                Date date = new Date(msg.getTimestampMillis());//时间
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String receiveTime = format.format(date);

                String senderNumber = msg.getOriginatingAddress();

                Toast.makeText(context, msgTxt, Toast.LENGTH_LONG).show();
                Toast.makeText(context, "发送人：" + senderNumber + "  短信内容：" + msgTxt + "接受时间：" + receiveTime, Toast.LENGTH_LONG).show();
                Intent startIntent = new Intent(context, HandleSmsService.class);
                Bundle itemBundle = new Bundle();
                itemBundle.putString("senderNumber", senderNumber);
                itemBundle.putString("msgTxt", msgTxt);
                itemBundle.putString("receiveTime", receiveTime);
                startIntent.putExtras(itemBundle);
                context.startService(startIntent);
                return;
            }
            return;
        }
    }
}