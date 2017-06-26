package com.architecture.models.api;

import android.content.Context;

import com.architecture.common.data.Account;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 20141022 on 2016/11/14.
 * 登录请求
 */
public class LoginOperater extends BaseOperater {

    private String password = "";
    private String name = "";
    public LoginOperater(Context context) {
        super(context);
    }
    
    public void setParams(String username, String pass) {
        params.put("username", username);
        params.put("password", pass);
        this.name = username;
        this.password = pass;
    }

    @Override
    protected String getUrlAction() {
        return "/login";
    }

    @Override
    protected void onParser(JSONObject response) {
        String userName = response.optString("userName");
        String rsclass = response.optString("rsclass");
        String major = response.optString("major");
        String token = response.optString("token");
        Account.get().login(token, userName, rsclass, major, password, name);
    }

    @Override
    protected void onParser(JSONArray response) {

    }

//    @Override
//    protected String getDebugJson() {
//        JSONObject object = new JSONObject();
//        try {
//            object.put("result", true);
//            object.put("rsclass", "rsclass");
//            object.put("major", "major");
//            object.put("token", "token");
//            object.put("userName", "userName");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return object.toString();
//    }
}
