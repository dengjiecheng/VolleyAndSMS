package com.example.volleytest.command;

import android.content.Context;

import com.example.volleytest.command.CommandMessage.Command;
import com.example.volleytest.command.exector.ADBCommandExec;
import com.example.volleytest.command.exector.CommandExec;
import com.example.volleytest.command.exector.DownLoadCommandExec;
import com.example.volleytest.command.exector.OkHttpCommandExec;

/**
 * Created by djc on 2017/5/2.
 */

public class CommandExecCreate {
    public static CommandExec getCommandExec(Context context,String  commandJsonInfo){
        CommandExec result=null;
        Command command= Command. parseToCommand(commandJsonInfo);
        if (command.getType()==CommandType.ADBCommand){
            result= new ADBCommandExec(command);
        }else if (command.getType()==CommandType.OKHTTPCommand){
            result= new OkHttpCommandExec(context.getCacheDir(),command);
        }else if (command.getType()==CommandType.DownLoadCommand){
            result= new DownLoadCommandExec(command);
        }
        return result;
    }
}
