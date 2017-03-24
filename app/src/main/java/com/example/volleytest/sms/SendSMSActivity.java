package com.example.volleytest.sms;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.volleytest.R;

/**
 * Created by djc on 2017/3/24.
 */

public class SendSMSActivity extends Activity {
    final String TAG = "SendSMSActivity";
    private EditText num;
    private EditText content;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendsms);
        num = (EditText) findViewById(R.id.Number);
        content = (EditText) findViewById(R.id.Content);
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
}
