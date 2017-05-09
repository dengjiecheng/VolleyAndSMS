package com.example.volleytest.Task;

import com.example.volleytest.command.exector.CommandExec;

/**
 * Created by djc on 2017/5/3.
 */

public interface TaskHandlerCallback {
     void onPreExecute(Task task, CommandExec commandExec);

     void onSuccess(Task task, CommandExec commandExec);

     void onException(Task task, CommandExec commandExec,Exception e );

     void onProgressUpdate(Task task, CommandExec commandExec, Integer... values);
}
