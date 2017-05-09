package com.example.volleytest.Task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import com.example.volleytest.command.CommandExecCreate;
import com.example.volleytest.command.exector.CommandExec;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by djc on 2017/4/26.
 */

public class TaskHandler {

    private class TaskAndCallback {
        private Task task;
        private TaskHandlerCallback callback;

        public Task getTask() {
            return task;
        }

        public void setTask(Task task) {
            this.task = task;
        }

        public TaskHandlerCallback getCallback() {
            return callback;
        }

        public void setCallback(TaskHandlerCallback callback) {
            this.callback = callback;
        }

        public TaskAndCallback(Task task, TaskHandlerCallback callback) {
            this.task = task;
            this.callback = callback;
        }
    }

    CommandTaskHandler handler;
    Context context;

    public TaskHandler(Context context) {
        this.context = context;
        handler = new CommandTaskHandler();
    }

    public void clearAllUnDealCommand() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    public boolean addTask(Task task, TaskHandlerCallback callback) {
        if (task == null) {
            return false;
        }
        TaskAndCallback taskAndCallback = new TaskAndCallback(task, callback);
        Message msg = handler.obtainMessage();
        msg.what = 2;
        msg.obj = taskAndCallback;
        handler.handleMessage(msg);
        return true;
    }

    public void removeExecotr(CommandExec commandExec) {
        Message msg = handler.obtainMessage();
        msg.what = 1;
        msg.obj = commandExec;
        handler.handleMessage(msg);
    }

    private class CommandTaskHandler extends Handler {
        List<CommandExec> taskExectorList = new ArrayList<CommandExec>();

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            removeFinishExec();
            if (msg.what == 1) {
                CommandExec command = (CommandExec) msg.obj;
                removeExecotr(command);
            } else if (msg.what == 2) {
                TaskAndCallback taskAndCallback = (TaskAndCallback) msg.obj;
                if (taskAndCallback != null) {
                    Task command = taskAndCallback.task;
                    TaskHandlerCallback callback = taskAndCallback.callback;
                    if (command != null) {
                        execCommand(command, callback);
                    }
                }
            }
        }

        private void removeExecotr(CommandExec commandExec) {
            taskExectorList.remove(commandExec);
        }

        public void execCommand(Task task, TaskHandlerCallback callback) {
            if (task == null) {
                return;
            }
            int type = task.getType();
            if (type == TaskType.CREAT_COMMAND_EXEC) {
                creatCommond(task, callback);
            } else if (type == TaskType.CANCEL_COMMAND_EXEC) {
                cancelCommand(task, callback);
            } else if (type == TaskType.SENT_MSG_TO_COMMAND_EXEC) {
                sendTaskMsg(task, callback);
            } else if (type == TaskType.START_COMMAND_EXEC) {
                startCommand(task, callback);
            }
        }

        private boolean startCommand(Task task, TaskHandlerCallback callback) {
            if (task == null) {
                return false;
            }
            String commandExecID = task.getCommandExecID();
            CommandExec commandExec = getCommandExecByID(commandExecID);
            if (commandExec != null) {
                if (callback != null) {
                    callback.onPreExecute(task, commandExec);
                }
                commandExec.startExecCommand();
                if (callback != null) {
                    callback.onSuccess(task, commandExec);
                }
                return true;
            } else {
                if (callback != null) {
                    callback.onException(task, null, new IllegalArgumentException("参数错误"));
                }
                return false;
            }
        }

        private boolean sendTaskMsg(Task task, TaskHandlerCallback callback) {
            if (task == null) {
                return false;
            }
            String commandExecID = task.getCommandExecID();
            CommandExec commandExec = getCommandExecByID(commandExecID);
            if (commandExec != null) {
                if (callback != null) {
                    callback.onPreExecute(task, commandExec);
                }
                String addinfo = task.getAdditionInfo();
                boolean result = commandExec.senCommandMessag(addinfo);
                if (callback != null) {
                    callback.onSuccess(task, commandExec);
                }
                return result;
            } else {
                if (callback != null) {
                    callback.onException(task, null, new IllegalArgumentException("参数错误"));
                }
                return false;
            }
        }

        private boolean cancelCommand(Task task, TaskHandlerCallback callback) {
            if (task == null) {
                return false;
            }
            String commandExecID = task.getCommandExecID();
            CommandExec commandExec = getCommandExecByID(commandExecID);
            if (commandExec != null) {
                if (callback != null) {
                    callback.onPreExecute(task, commandExec);
                }
                removeExecotr(commandExec);
                boolean result = commandExec.cancelExecCommand();
                if (callback != null) {
                    callback.onSuccess(task, commandExec);
                }
                return result;
            } else {
                if (callback != null) {
                    callback.onException(task, null, new IllegalArgumentException("参数错误"));
                }
                return false;
            }
        }

        private boolean creatCommond(Task task, TaskHandlerCallback callback) {
            if (task == null) {
                return false;
            }
            if (callback != null) {
                callback.onPreExecute(task, null);
            }
            String addinfo = task.getAdditionInfo();
            String execID = task.getCommandExecID();
            CommandExec commandExec = CommandExecCreate.getCommandExec(context, addinfo);
            if (commandExec != null) {
                String id = "" + SystemClock.currentThreadTimeMillis();
                commandExec.setID(id);
                taskExectorList.add(commandExec);
                if (callback != null) {
                    callback.onSuccess(task, commandExec);
                }
                if ("StartNow".equalsIgnoreCase(execID)) {
                    String taskStr = Task.builtTaskJson("id", TaskType.START_COMMAND_EXEC, "name", id, "");
                    Logger.json(taskStr);
                    Task command = Task.ParseToTask(taskStr);
                    startCommand(command, null);
                }
                return true;
            } else {
                if (callback != null) {
                    callback.onException(task, null, new IllegalArgumentException("参数错误"));
                }
            }
            return false;
        }

        private void removeFinishExec() {
            if (taskExectorList.size() > 0) {
                Iterator<CommandExec> it = taskExectorList.iterator();
                CommandExec commandExec;
                while (it.hasNext()) {
                    commandExec = it.next();
                    if (commandExec.isFinish()) {
                        it.remove();
                    }
                }
            }
        }

        private CommandExec getCommandExecByID(String commandExecID) {
            for (int i = 0; i < taskExectorList.size(); i++) {
                CommandExec commandExec = taskExectorList.get(i);
                if (commandExec.getID().equalsIgnoreCase(commandExecID)) {
                    return commandExec;
                }
            }
            return null;
        }
    }
}
