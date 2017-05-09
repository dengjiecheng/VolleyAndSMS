package com.example.volleytest.command.CommandMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by djc on 2017/4/24.
 */

public class Command {
    public static final String ID = "command_ID";
    public static final String TYPE = "command_type";
    public static final String NAME = "command_name";
    public static final String THREADTYPE = "thread_type";
    public static final String ADDITION_INFO = "AdditionInfo";
    private String id;
    private int type;
    private String name;
    private int threadType;
    private String additionInfo;

    public Command() {
    }

    public Command(String id, int type, String name, int threadType, String additionInfo) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.threadType = threadType;
        this.additionInfo = additionInfo;
    }

    public Command(int type, String name, int threadType) {
        this.type = type;
        this.name = name;
        this.threadType = threadType;
    }

    public Command(int type, String name, int threadType, String additionInfo) {
        this.type = type;
        this.name = name;
        this.threadType = threadType;
        this.additionInfo = additionInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public int getThreadType() {
        return threadType;
    }

    public void setThreadType(int threadType) {
        this.threadType = threadType;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdditionInfo() {
        return additionInfo;
    }

    public void setAdditionInfo(String additionInfo) {
        this.additionInfo = additionInfo;
    }

    public static Command parseToCommand(String name) {
        try {
            JSONObject js = new JSONObject(name);
            String id = js.optString( ID, "");
            int type = js.optInt( TYPE, 0);
            String text = js.optString( NAME, "");
            int threadType = js.optInt( THREADTYPE, 0);
            String additionInfo = js.optString( ADDITION_INFO, "");
            return new Command(id, type, text, threadType, additionInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String builtCommandJson(Command command) {
        if (command != null) {
            return builtCommandJson(command.getId(), command.getType(), command.getName(), command.getThreadType(), command.getAdditionInfo());
        } else {
            return null;
        }
    }

    public static String builtCommandJson(String id, int type, String name, int threadType, String additionInfo) {
        try {
            JSONObject js = new JSONObject();
            js.put(ID, id);
            js.put(TYPE, type);
            js.put(NAME, name);
            js.put(THREADTYPE, threadType);
            js.put(ADDITION_INFO, additionInfo);
            return js.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String builtDownLoadAdditionJson(String mSavePath, String filename, String downLoadUrl) {
        try {
            JSONObject js = new JSONObject();
            js.put("SavePath", mSavePath);
            js.put("FileName", filename);
            js.put("DownLoadUrl", downLoadUrl);
            return js.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
