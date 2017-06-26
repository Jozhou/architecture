package com.architecture.models.api;

import android.content.Context;

import com.architecture.common.data.Account;
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
public class GetNodeMgrListOperater extends BaseOperater {

    private List<ProjectInfo> projectInfoList = new ArrayList<ProjectInfo>();
    public GetNodeMgrListOperater(Context context) {
        super(context);
    }
    
    
    public void setParams(String nodeCode, String nodeNames, String projectName) {
        params.put("token", Account.get().token);
        params.put("nodeCode", nodeCode);
        params.put("nodeNames", nodeNames);
        params.put("projectName", projectName);
    }

    @Override
    protected String getUrlAction() {
        return "/nodemgrList";
    }

    @Override
    protected void onParser(JSONObject response) {
        JSONArray array = response.optJSONArray("data");
        ProjectInfo item = null;
        JSONObject jsonObject = null;
        try {
            for (int i = 0; i < array.length(); i++) {
                jsonObject = array.getJSONObject(i);
                item = new ProjectInfo();
                item.id = jsonObject.optString("id");
                item.isNewRecord = jsonObject.optBoolean("isNewRecord");
                item.createDate = jsonObject.optString("createDate");
                item.updateDate = jsonObject.optString("updateDate");
                item.nodeCode = jsonObject.optString("nodeCode");
                item.nodeNames = jsonObject.optString("nodeNames");
                item.projectName = jsonObject.optString("projectName");
                item.buildingInfo = jsonObject.optString("buildingInfo");
                item.equipInfo = jsonObject.optString("equipInfo");
                item.materialInfo = jsonObject.optString("materialInfo");
                item.status = jsonObject.optString("status");
                item.stateFlag = jsonObject.optInt("stateFlag");
                item.graphicInfo = jsonObject.optString("graphicInfo");
                item.imageUrl = jsonObject.optString("imageUrl");
                projectInfoList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onParser(JSONArray response) {

    }

//    @Override
//    protected String getDebugJson() {
//        JSONObject object = new JSONObject();
//        try {
//            object.put("result", true);
//            JSONArray array = new JSONArray();
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
//            array.put(jsonObject);
//            object.put("data", array);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return object.toString();
//    }
    
    public List<ProjectInfo> getProjectInfo() {
        return projectInfoList;
    }
}
