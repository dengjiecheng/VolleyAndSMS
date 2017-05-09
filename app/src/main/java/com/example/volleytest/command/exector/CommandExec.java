package com.example.volleytest.command.exector;

import com.example.volleytest.command.CommandMessage.Command;

/**
 * Created by djc on 2017/4/24.
 */

public abstract class CommandExec<T> {
    public interface CommandExecCallback {
        void onProgressUpdate(Command mCommand,Integer... values);
        void onPreExecute(Command mCommand, String message);
        void onSuccess(Command mCommand, String message);
        void onException(Command mCommand,Exception e);
    }

    protected CommandExecCallback callback;

    public CommandExecCallback getCallback() {
        return callback;
    }

    public void setCallback(CommandExecCallback callback) {
        this.callback = callback;
    }

    private String ID;
    protected Command mCommand;

    public CommandExec(String ID, Command mCommand,CommandExecCallback callback) {
        this.callback = callback;
        this.ID = ID;
        this.mCommand = mCommand;
    }

    public CommandExec(Command mCommand, CommandExecCallback callback) {
        this.callback = callback;
        this.mCommand = mCommand;
    }

    public CommandExec(Command mCommand ) {
        this.mCommand = mCommand;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Command getCommand() {
        return mCommand;
    }

    public void setCommand(Command mCommand) {
        this.mCommand = mCommand;
    }

    public abstract T startExecCommand();

    public abstract boolean cancelExecCommand();

    public abstract boolean senCommandMessag(String jsonMsg);

    public abstract boolean isFinish();
}
