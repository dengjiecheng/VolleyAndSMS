package com.example.volleytest.command.MyAsyncTask;

import android.os.Handler;

import com.example.volleytest.Utils.SafeAsyncTask;
import com.example.volleytest.Utils.StringUtils;
import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.Executor;

/**
 * Created by djc on 2017/5/4.
 */

public class ADBShellCommandAsyncTask extends SafeAsyncTask<Integer, String> {

    private static final String TAG = "ADBShellCommandAsyncTask";
    String commandStr;

    public ADBShellCommandAsyncTask(String commandStr) {
        this.commandStr = commandStr;
    }

    public ADBShellCommandAsyncTask(Handler handler, String commandStr) {
        super(handler);
        this.commandStr = commandStr;
    }

    public ADBShellCommandAsyncTask(Executor executor, String commandStr) {
        super(executor);
        this.commandStr = commandStr;
    }

    public ADBShellCommandAsyncTask(Handler handler, Executor executor, String commandStr) {
        super(handler, executor);
        this.commandStr = commandStr;
    }

    @Override
    public String call() throws Exception {
        return exec(commandStr);
    }

    private String exec(String command) throws Exception {
        Logger.i("要执行的命令：" + command);
        if (StringUtils.isEmpty(command)) {
            throw new IllegalArgumentException("ADB 命令内容为空");
        }
        String[] cmdStrings = new String[]{"sh", "-c", command};
//			Runtime.getRuntime().exec("su");
        Process process = Runtime.getRuntime().exec(cmdStrings);
        process.waitFor();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
        int read;
        char[] buffer = new char[4096];
        StringBuffer output = new StringBuffer();
        while ((read = reader.read(buffer)) > 0) {
            output.append(buffer, 0, read);
        }
        reader.close();
        process.waitFor();
        return output.toString();
    }
}
