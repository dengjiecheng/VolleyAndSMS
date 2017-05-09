package com.example.volleytest.volley;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.volleytest.Netty.NettyServer;
import com.example.volleytest.R;
import com.example.volleytest.SockeCommunicationRules.RulesBuild;
import com.example.volleytest.Task.Task;
import com.example.volleytest.Task.TaskHandler;
import com.example.volleytest.Task.TaskHandlerCallback;
import com.example.volleytest.Task.TaskType;
import com.example.volleytest.Utils.L;
import com.example.volleytest.command.CommandMessage.Command;
import com.example.volleytest.command.CommandType;
import com.example.volleytest.command.exector.CommandExec;
import com.orhanobut.logger.Logger;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private Button localRunButton;
    private EditText localPathEdit;
    private Handler handler = new Handler();
    private EditText urlEdit;
    //	private Button remoteRunButton;
    TextView outputView;
    TaskHandler mCommandHander;
    NettyServer nettyServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        String test = "pm install -r  /sdcard/ActivityMain.apk";
        String test = "adb shell pm install -r  /sdcard/zIntelligenHomeVoice1.0-release.apk";
//		String test="adb -s 20080411 install /sdcard/ActivityMain.apk";
//        String test = "/sdcard/ActivityMain.apk";
        outputView = (TextView) findViewById(R.id.outputView);
        localPathEdit = (EditText) findViewById(R.id.localPathEdit);
        localPathEdit.setText(test);
        localRunButton = (Button) findViewById(R.id.localRunButton);

        mCommandHander = new TaskHandler(MainActivity.this);
        callback = new TaskHandlerCallback() {

            @Override
            public void onPreExecute(Task task, CommandExec commandExec) {
                String taskStr = Task.builtTaskJson(task);
                String exeInfoMsg = "";
                String responseInfo = RulesBuild.response(RulesBuild.RESPONSE_TYPE_TASKINFO, taskStr, RulesBuild.RESPONSE_STATE_TYPE_ONPREECECUTE, exeInfoMsg);
                Logger.json(responseInfo);
                if (nettyServer != null) {
                    nettyServer.write(responseInfo);
                }
            }

            @Override
            public void onSuccess(Task task, final CommandExec commandExec) {
                if (task != null) {
                    if (task.getType() == TaskType.CREAT_COMMAND_EXEC && commandExec != null) {

                        CommandExec.CommandExecCallback commandExecCallback = new CommandExec.CommandExecCallback() {
                            @Override
                            public void onProgressUpdate(Command mCommand, Integer... values) {
//                                T.showShort(MainActivity.this,"");
//                                String str=""+values[0];
//                                output(str);
                                if (mCommand != null && values != null && values.length >= 3) {
                                    String commandJson = Command.builtCommandJson(mCommand);
                                    String progressStr = RulesBuild.bulitProgress(values[2], values[1]);
                                    String responseInfo = RulesBuild.response(RulesBuild.RESPONSE_TYPE_COMMANDINFO, commandJson, RulesBuild.RESPONSE_STATE_TYPE_ONPROGRSEEUPDATE, progressStr);
                                    if (i % 5000 == 0) {
                                        Logger.json(responseInfo);
                                        if (nettyServer != null) {
                                            nettyServer.write(responseInfo);
                                        }
                                    }
                                    i++;
                                }
//                                String taskStr = Task.builtTaskJson("id", TaskType.CANCEL_COMMAND_EXEC, "name",commandExec.getID() , "");
//                                Logger.json(taskStr);
//                                Task command = Task.ParseToTask(taskStr);
//                                mCommandHander.addTask(command, null);
                            }

                            @Override
                            public void onPreExecute(Command mCommand, String message) {
                                output(message);
                                if (mCommand != null) {
                                    String commandJson = Command.builtCommandJson(mCommand);
                                    String responseInfo = RulesBuild.response(RulesBuild.RESPONSE_TYPE_COMMANDINFO, commandJson, RulesBuild.RESPONSE_STATE_TYPE_ONPREECECUTE, message);
                                    Logger.json(responseInfo);
                                    if (nettyServer != null) {
                                        nettyServer.write(responseInfo);
                                    }
                                }
                            }

                            @Override
                            public void onSuccess(Command mCommand, String message) {
                                output(message);
                                if (mCommand != null) {
                                    String commandJson = Command.builtCommandJson(mCommand);
                                    String responseInfo = RulesBuild.response(RulesBuild.RESPONSE_TYPE_COMMANDINFO, commandJson, RulesBuild.RESPONSE_STATE_TYPE_ONSUCCESS, message);
                                    Logger.json(responseInfo);
                                    if (nettyServer != null) {
                                        nettyServer.write(responseInfo);
                                    }
                                }
                            }

                            @Override
                            public void onException(Command mCommand, Exception e) {
                                String message = e.toString();
                                output(message);
                                if (mCommand != null) {
                                    String commandJson = Command.builtCommandJson(mCommand);
                                    String responseInfo = RulesBuild.response(RulesBuild.RESPONSE_TYPE_COMMANDINFO, commandJson, RulesBuild.RESPONSE_STATE_TYPE_ONEXCEPTION, message);
                                    Logger.json(responseInfo);
                                    if (nettyServer != null) {
                                        nettyServer.write(responseInfo);
                                    }
                                }
                            }
                        };
                        commandExec.setCallback(commandExecCallback);
                    }
                }

                if (task != null) {
                    String taskStr = Task.builtTaskJson(task);
                    String exeInfoMsg = "";
                    try {
                        JSONObject js = new JSONObject();
                        if (commandExec != null) {
                            js.put("execID", commandExec.getID());
                        }
                        exeInfoMsg = js.toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String responseInfo = RulesBuild.response(RulesBuild.RESPONSE_TYPE_TASKINFO, taskStr, RulesBuild.RESPONSE_STATE_TYPE_ONSUCCESS, exeInfoMsg);
                    Logger.json(responseInfo);
                    if (nettyServer != null) {
                        nettyServer.write(responseInfo);
                    }
                }
            }

            @Override
            public void onException(Task task, CommandExec commandExec, Exception e) {
                if (task != null) {
                    String taskStr = Task.builtTaskJson(task);
                    String errMsg = e.toString();
                    String responseInfo = RulesBuild.response(RulesBuild.RESPONSE_TYPE_TASKINFO, taskStr, RulesBuild.RESPONSE_STATE_TYPE_ONEXCEPTION, errMsg);
                    Logger.json(responseInfo);
                    if (nettyServer != null) {
                        nettyServer.write(responseInfo);
                    }
                }
            }

            @Override
            public void onProgressUpdate(Task task, CommandExec commandExec, Integer... values) {
                if (task != null && values != null && values.length >= 3) {
                    String taskStr = Task.builtTaskJson(task);
                    String progressStr = RulesBuild.bulitProgress(values[2], values[1]);
                    String responseInfo = RulesBuild.response(RulesBuild.RESPONSE_TYPE_TASKINFO, taskStr, RulesBuild.RESPONSE_STATE_TYPE_ONPROGRSEEUPDATE, progressStr);
                    Logger.json(responseInfo);
                    if (nettyServer != null) {
                        nettyServer.write(responseInfo);
                    }
                }
            }
        };
        SimpleChannelHandler mServerHandler = new SimpleChannelHandler() {
            @Override
            public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e) throws Exception {
                super.messageReceived(ctx, e);
//                ctx.getHandler().
                if (e.getMessage() instanceof String) {
                    String message = (String) e.getMessage();
                    L.i(TAG, "Client发来:" + message);
//                    e.getChannel().write("Server已收到刚发送的:" + message);
                    Task command = Task.ParseToTask(message);
                    if (command != null) {
//                        TaskHandlerCallback callback = new TaskHandlerCallback() {
//                            @Override
//                            public void onPreExec(Task task, CommandExec commandExec) {
//
//                            }
//
//                            @Override
//                            public void onPostExec(Task task, CommandExec commandExec) {
//                                L.i(TAG, "回复内容:" + message);
//                                if (StringUtils.isNotEmpty(message)) {
//                                    e.getChannel().write(message);
//                                }
//                            }
//
//                            @Override
//                            public void onError(Task task, CommandExec commandExec, int errCode) {
//
//                            }
//                        };
                        mCommandHander.addTask(command, callback);
                    }
                }
            }

            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
                super.exceptionCaught(ctx, e);
            }

            @Override
            public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
                L.i(TAG, "有一个客户端注册上来了。。。");
                L.i(TAG, "Client:" + e.getChannel().getRemoteAddress());
                L.i(TAG, "Server:" + e.getChannel().getLocalAddress());
                L.i(TAG, "等待客户端输入。。。");
                super.channelConnected(ctx, e);
            }

            @Override
            public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
                super.channelDisconnected(ctx, e);
                L.i(TAG, "客户端关闭。。。");
            }
        };
        nettyServer = new NettyServer(mServerHandler);
        nettyServer.init();
//        test();
    }

    private void output(final String str) {
        Log.i(TAG, "output.:" + str);
        Runnable proc = new Runnable() {
            public void run() {
                outputView.setText(str);
            }
        };
        handler.post(proc);
    }

    int i = 1000;
    TaskHandlerCallback callback;
    void test() {
        String mSavePath = "/sdcard/DownLoad/";
        String filename = "voice.apk";
        String downLoadUrl = "http://www.joinlinkhome.com/zlhtnew/VoiceAppUrl.aspx";
        String addinfo = Command.builtDownLoadAdditionJson(mSavePath, filename, downLoadUrl);

        String taskAddinfo = Command.builtCommandJson("id", CommandType.DownLoadCommand, "name", 0, addinfo);

        String taskStr = Task.builtTaskJson("id", TaskType.CREAT_COMMAND_EXEC, "name", "StartNow", taskAddinfo);
        Logger.json(taskStr);
        Task command = Task.ParseToTask(taskStr);

        mCommandHander.addTask(command, callback);

//        String test = "adb shell pm install -r  /sdcard/DownLoad/voice.apk";
//         taskAddinfo = Command.builtCommandJson("id", CommandType.ADBCommand, "name", 0, test);
//         taskStr = Task.builtTaskJson("id", TaskType.CREAT_COMMAND_EXEC, "name", "StartNow", taskAddinfo);
//        L.i(TAG,taskStr);
//        Logger.json(taskStr);
//        command = Task.ParseToTask(taskStr);
//        mCommandHander.addTask(command, null);
//
//        command = Task.ParseToTask(taskStr);
//        mCommandHander.addTask(command, null);
//
//        command = Task.ParseToTask(taskStr);
//        mCommandHander.addTask(command, null);
    }
}
