package com.example.volleytest.sms;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.volleytest.Utils.TelephonyManagerTool;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by djc on 2017/3/24.
 */

public class HandleSmsService extends IntentService {
    final String TAG = "HandleSmsService";
   public  int batteryLevel=-1;
    public HandleSmsService() {
        super("HandleSmsService");
    }
    BatteryReceiver batteryReceiver;
    @Override
    public void onCreate() {
        // TODO 自动生成的方法存根
        super.onCreate();
        initBatteryReceive();
    }
    public void initBatteryReceive(){
        //注册广播接受者java代码
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        //创建广播接受者对象
        batteryReceiver = new BatteryReceiver();
        //注册receiver
        registerReceiver(batteryReceiver, intentFilter);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "begin onHandleIntent() in " + this);
        if(batteryLevel<=0){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String senderNumber = intent.getExtras().getString("senderNumber");
        String msgTxt = intent.getExtras().getString("msgTxt");
        String receiveTime = intent.getExtras().getString("receiveTime");
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
                        Log.d("TAG", response.toString());
//                        String mJSON = response.toString();
                        Toast.makeText(HandleSmsService.this,response.toString(),Toast.LENGTH_LONG).show();
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
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
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(batteryReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
