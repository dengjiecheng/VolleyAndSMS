package com.example.volleytest.command.exector;

import com.example.volleytest.command.CommandMessage.Command;
import com.example.volleytest.command.MyAsyncTask.ADBShellCommandAsyncTask;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

/**
 * Created by djc on 2017/4/24.
 */

public class ADBCommandExec extends CommandExec<Void> {
    private static final String TAG = "ADBCommandExec";
    ADBShellCommandAsyncTask task;

    public ADBCommandExec(Command command) {
        super(command);
        String commandStr = mCommand.getAdditionInfo();
        task = new ADBShellCommandAsyncTask(commandStr) {
            @Override
            protected void onPreExecute() throws Exception {
                super.onPreExecute();
                if (callback != null) {
                    callback.onPreExecute(mCommand, "");
                }
            }

            @Override
            protected void onProgressUpdate(Integer... values) throws Exception {
                super.onProgressUpdate(values);
                Logger.i("onProgressUpdate=" + values[0] + "现在：" + values[1] + "总：" + values[2]);
                if (callback != null) {
                    callback.onProgressUpdate(mCommand, values);
                }
            }

            @Override
            protected void onSuccess(String result) throws Exception {
                super.onSuccess(result);
                Logger.i("执行结果：" + result);
                JSONObject js = new JSONObject();
                js.put("MsgCode", 1);
                js.put("MsgInfo", result);
                if (callback != null) {
                    callback.onSuccess(mCommand, js.toString());
                }
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);
                Logger.i("ABD Shell执行失败" + e.toString());
                if (callback != null) {
                    callback.onException(mCommand, e);
                }
            }
        };
    }

    @Override
    public Void startExecCommand() {
        if (task != null) {
            Command command = getCommand();
            if (command != null && command.getThreadType() == 1) {
                try {
                    task.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                task.execute();
            }
        }
        return null;
    }

    @Override
    public boolean cancelExecCommand() {
        if (task != null) {
            task.cancel(true);
        }
        return true;
    }

    @Override
    public boolean senCommandMessag(String jsonMsg) {
        return false;
    }

    @Override
    public boolean isFinish() {
        return task.isFinish();
    }
}
