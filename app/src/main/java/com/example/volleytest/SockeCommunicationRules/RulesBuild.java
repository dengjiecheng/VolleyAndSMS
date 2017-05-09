package com.example.volleytest.SockeCommunicationRules;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by djc on 2017/5/5.
 */

public class RulesBuild {
    public static final String RESPONSE_TYPE ="responseType";
    public static final String RESPONSE_TYPE_INFO ="responseTypeInfo";
    public static final String RESPONSE_STATE_TYPE ="responseStateType";
    public static final String RESPONSE_STATE_INFO ="responseStateInfo";

    public static final int RESPONSE_TYPE_TASKINFO =1;
    public static final int RESPONSE_TYPE_COMMANDINFO =2;

    public static final int RESPONSE_STATE_TYPE_ONPREECECUTE =1;
    public static final int RESPONSE_STATE_TYPE_ONPROGRSEEUPDATE =2;
    public static final int RESPONSE_STATE_TYPE_ONSUCCESS =3;
    public static final int RESPONSE_STATE_TYPE_ONEXCEPTION=4;

    public static String response(int type,String typeInfo,int StateType,String stateInfo){
        try {
            JSONObject js = new JSONObject();
            js.put(RESPONSE_TYPE, type);
            js.put(RESPONSE_TYPE_INFO, typeInfo);
            js.put(RESPONSE_STATE_TYPE, StateType);
            js.put(RESPONSE_STATE_INFO, stateInfo);
            return js.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String bulitProgress(int totalSize,int currentSize){
        try {
            JSONObject js = new JSONObject();
            js.put("totalSize", totalSize);
            js.put("currentSize", currentSize);
            return js.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
