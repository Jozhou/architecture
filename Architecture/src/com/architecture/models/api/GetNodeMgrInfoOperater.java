package com.architecture.models.api;

import android.content.Context;

import com.architecture.common.data.Account;
import com.architecture.common.data.Common;
import com.architecture.models.entry.ProjectInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 20141022 on 2016/11/14.
 * 二维码搜索
 */
public class GetNodeMgrInfoOperater extends BaseOperater {

    private ProjectInfo projectInfo;
    public GetNodeMgrInfoOperater(Context context) {
        super(context);
    }

    public void setParams(String nodeId) {
        params.put("token", Account.get().token);
        params.put("nodeId", nodeId);
    }

    @Override
    protected String getUrlAction() {
        return "/nodemgrInfo";
    }

    @Override
    protected void onParser(JSONObject response) {
        projectInfo = new ProjectInfo();
        JSONObject jsonObject = response.optJSONObject("nodeInfo");
        projectInfo.id = jsonObject.optString("id");
        projectInfo.isNewRecord = jsonObject.optBoolean("isNewRecord");
        projectInfo.createDate = jsonObject.optString("createDate");
        projectInfo.updateDate = jsonObject.optString("updateDate");
        projectInfo.nodeCode = jsonObject.optString("nodeCode");
        projectInfo.nodeNames = jsonObject.optString("nodeNames");
        projectInfo.projectName = jsonObject.optString("projectName");
        projectInfo.buildingInfo = jsonObject.optString("buildingInfo", "");
        projectInfo.equipInfo = jsonObject.optString("equipInfo", "");
        projectInfo.materialInfo = jsonObject.optString("materialInfo", "");
        projectInfo.status = jsonObject.optString("status");
        projectInfo.stateFlag = jsonObject.optInt("stateFlag");
        projectInfo.graphicInfo = jsonObject.optString("graphicInfo", "").replaceAll(Common.regEx_space, "").replaceAll(Common.regEx_html, "");
        projectInfo.attention = jsonObject.optString("attention", "").replaceAll(Common.regEx_space, "").replaceAll(Common.regEx_html, "");
        projectInfo.essentials = jsonObject.optString("essentials", "").replaceAll(Common.regEx_space, "").replaceAll(Common.regEx_html,"");
    }

    @Override
    protected void onParser(JSONArray response) {

    }

//    @Override
//    protected String getDebugJson() {
//        JSONObject object = new JSONObject();
//        try {
//            object.put("result", true);
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("id", "id");
//            jsonObject.put("isNewRecord", true);
//            jsonObject.put("createDate", "createDate");
//            jsonObject.put("updateDate", "updateDate");
//            jsonObject.put("nodeCode", "nodeCode");
//            jsonObject.put("nodeNames", "nodeNames");
//            jsonObject.put("projectName", "projectName");
//            jsonObject.put("buildingInfo", "buildingInfographicInfosadfasdfsdafdsfsdfsdgsdfsdfsdfsdfsdfsdgdsfgfdgsdfgsdfsdfsdfsfdsafsdg就死定了水电费见识到了分级数量大幅拉升善良的会计法看来大叔发觉了时打开啥都放暑假的浪费阿斯兰的付款建设路口的房间");
//            jsonObject.put("equipInfo", "equipInfo");
//            jsonObject.put("materialInfo", "materialInfographicInfosadfasdfsdafdsfsdfsdgsdfsdfsdfsdfsdfsdgdsfgfdgsdfgsdfsdfsdfsfdsafsdg就死定了水电费见识到了分级数量大幅拉升善良的会计法看来大叔发觉了时打开啥都放暑假的浪费阿斯兰的付款建设路口的房间");
//            jsonObject.put("graphicInfo", "graphicInfo");
//            jsonObject.put("status", "status");
//            jsonObject.put("stateFlag", 1);
//            object.put("nodeInfo", jsonObject);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return object.toString();
//    }
    
    public ProjectInfo getProjectInfo() {
        return this.projectInfo;
    }
}
