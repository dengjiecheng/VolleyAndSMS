package com.example.volleytest.command.exector;

import com.example.volleytest.command.CommandMessage.Command;
import com.example.volleytest.command.MyAsyncTask.DownLoadAsyncTask;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by djc on 2017/5/2.
 * <p>
 * 下载文件
 */

public class DownLoadCommandExec extends CommandExec<Void> {

    DownLoadAsyncTask task;

    public DownLoadCommandExec(Command command) {
        super(command);
        String additionInfo = command.getAdditionInfo();
        try {
            JSONObject js = new JSONObject(additionInfo);
            if (js != null) {
                String mSavePath = js.getString("SavePath");
                String filename = js.getString("FileName");
                String downLoadUrl = js.getString("DownLoadUrl");
                Logger.i("mSavePath=" + mSavePath);
                Logger.i("filename=" + filename);
                Logger.i("downLoadUrl=" + downLoadUrl);

                task = new DownLoadAsyncTask(mSavePath, filename, downLoadUrl) {
                    @Override
                    protected void onPreExecute() throws Exception {
                        super.onPreExecute();
                        if(callback!=null){
                            callback.onPreExecute(mCommand,"");
                        }
                    }

                    @Override
                    protected void onProgressUpdate(Integer... values)throws Exception {
                        super.onProgressUpdate(values);
//                        Logger.i("onProgressUpdate=" + values[0]+"现在："+ values[1]+"总："+ values[2]);
                        if(callback!=null){
                            callback.onProgressUpdate(mCommand,values);
                        }
                    }

                    @Override
                    protected void onSuccess(Boolean result)throws Exception {
                        super.onSuccess(result);
                        JSONObject js=new JSONObject();
;                        if (result) {
                            js.put("MsgCode",1);
                            js.put("MsgInfo","下载成功");
                        } else {
                            js.put("MsgCode",0);
                            js.put("MsgInfo","下载失败");
                        }
//                        Logger.i(js.toString());
                        if(callback!=null){
                            callback.onSuccess(mCommand,js.toString());
                        }
                    }

                    @Override
                    protected void onException(Exception e) throws RuntimeException {
                        super.onException(e);
//                        Logger.i("下载失败"+e.toString());
                        if(callback!=null){
                            callback.onException(mCommand,e);
                        }
                    }
                };
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Void startExecCommand() {
        if (task != null) {
            task.execute();
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
    public boolean isFinish() {
        return task.isFinish();
    }
    @Override
    public boolean senCommandMessag(String jsonMsg) {
        return false;
    }

}
