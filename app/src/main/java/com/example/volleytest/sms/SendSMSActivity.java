package com.example.volleytest.sms;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.volleytest.R;
import com.example.volleytest.Utils.TelephonyManagerTool;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by djc on 2017/3/24.
 */

public class SendSMSActivity extends Activity {
    final String TAG = "SendSMSActivity";
    private EditText num;
    private EditText content;
    public  static int batteryLevel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendsms);
        num = (EditText) findViewById(R.id.Number);
        content = (EditText) findViewById(R.id.Content);
        initBatteryReceive();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                setdata();
//            }
//        },2000);
    }

    public void send(View view) {
        String strNo = num.getText().toString();
        String strContent = content.getText().toString();

        SmsManager smsManager = SmsManager.getDefault();
        //如果字数超过5,需拆分成多条短信发送
        if (strContent != null) {
            if (strContent.length() > 5) {
                ArrayList<String> msgs = smsManager.divideMessage(strContent);
                for (String msg : msgs) {
                    smsManager.sendTextMessage(strNo, null, msg, null, null);
                }
            } else {
                smsManager.sendTextMessage(strNo, null, strContent, null, null);
            }
        }
        num.setText("");
        content.setText("");

        Toast.makeText(SendSMSActivity.this, "短信发送完成", Toast.LENGTH_LONG).show();
    }

    void initBatteryReceive(){
        //注册广播接受者java代码
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        //创建广播接受者对象
        BatteryReceiver batteryReceiver = new BatteryReceiver();
        //注册receiver
        registerReceiver(batteryReceiver, intentFilter);
    }
    void setdata(){
        String senderNumber ="13662755913";
        String msgTxt = "你妹";
        String receiveTime = "2013-11-12";
        //这里可以上传数据到服务器，可以是非常耗时的任务。（非主线程）
        Log.i(TAG, "发送人：" + senderNumber + "  短信内容：" + msgTxt + "接受时间：" + receiveTime);
        String phone_number= TelephonyManagerTool.getPhoneNumber(this);
        JSONObject js=new JSONObject();
        try {
            js.put("addresser_number",senderNumber);
            js.put("phone_number",phone_number);
            js.put("electricity",batteryLevel);
            js.put("message_content",msgTxt);
            js.put("send_time",receiveTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String JsonData=js.toString();
        Log.i(TAG, "发送的内容：" +JsonData);
        //Volley
        RequestQueue mQueue;

        mQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                //第一个参数，请求的网址
                "http://192.168.0.163:8888/smsTransmit/sendMessage",
                //第二个参数
                js,
                //响应正确时的处理
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", "结果："+response.toString());
//                        String mJSON = response.toString();
                    }
                },
                //响应错误时的处理
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                    }
                });
        //把这个请求加到Volley队列即可
        mQueue.add(jsonObjectRequest);
    }
    /**
     * 广播接受者
     */
    class BatteryReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            //判断它是否是为电量变化的Broadcast Action
            if(Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())){
                //获取当前电量
                int level = intent.getIntExtra("level", 0);
                //电量的总刻度
                int scale = intent.getIntExtra("scale", 100);
                //把它转成百分比
//                tv.setText("电池电量为"+((level*100)/scale)+"%");
                batteryLevel=level;
                Log.i(TAG,"当前电量："+level);
            }
        }

    }
}
