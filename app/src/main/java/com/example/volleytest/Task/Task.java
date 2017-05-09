package com.example.volleytest.Task;

import com.example.volleytest.Utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by djc on 2017/4/24.
 */

public class Task {
    public static final String ID = "Task_ID";
    public static final String TYPE = "Task_type";
    public static final String NAME = "Task_name";
    public static final String COMMAND_EXEC_ID = "CommandExecID";
    public static final String ADDITION_INFO = "AdditionInfo";

    private String id;
    private int type;
    private String name;
    private String commandExecID;
    private String additionInfo;

    public Task() {
    }

    public Task(String id, int type, String TaskStr, String additionInfo) {
        this.id = id;
        this.type = type;
        this.name = TaskStr;
        this.additionInfo = additionInfo;
    }

    public Task(int type, String TaskStr) {
        this.type = type;
        this.name = TaskStr;
    }

    public Task(int type, String TaskStr, String additionInfo) {
        this.type = type;
        this.name = TaskStr;
        this.additionInfo = additionInfo;
    }

    public Task(String id, int type, String name, String commandExecID, String additionInfo) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.commandExecID = commandExecID;
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

    public void setType(int type) {
        this.type = type;
    }

    public String getCommandExecID() {
        return commandExecID;
    }

    public void setCommandExecID(String commandExecID) {
        this.commandExecID = commandExecID;
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

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", commandExecID='" + commandExecID + '\'' +
                ", additionInfo='" + additionInfo + '\'' +
                '}';
    }

    public static Task ParseToTask(String TaskStr) {
        if (StringUtils.isEmpty(TaskStr)) {
            return null;
        }
        try {
            JSONObject js = new JSONObject(TaskStr);
            String id = js.optString(ID, "");
            int type = js.optInt(TYPE, 0);
            String name = js.optString( NAME, "");
            String additionInfo = js.optString( ADDITION_INFO, "");
            String commandExecID = js.optString( COMMAND_EXEC_ID, "");
            return new Task(id, type, name, commandExecID, additionInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String builtTaskJson(Task task) {
        if (task != null) {
            return builtTaskJson(task.id, task.type, task.name, task.commandExecID, task.additionInfo);
        } else {
            return null;
        }
    }

    public static String builtTaskJson(String id, int type, String name, String commandExecID, String additionInfo) {
        try {
            JSONObject js = new JSONObject();
            js.put(ID, id);
            js.put(TYPE, type);
            js.put(NAME, name);
            js.put(COMMAND_EXEC_ID, commandExecID);
            js.put(ADDITION_INFO, additionInfo);
            return js.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
