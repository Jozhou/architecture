package com.architecture.models.api;

import android.content.Context;

import com.architecture.common.data.Account;
import com.architecture.models.entry.CadImage;
import com.architecture.models.entry.Knowledge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 20141022 on 2016/11/18.
 * 获取知识库列表
 */
public class GetKnowledgeOperater extends BaseOperater {

    private List<Knowledge> knowledges = new ArrayList<Knowledge>();
    public GetKnowledgeOperater(Context context) {
        super(context);
    }
    
    public void setParams (String nodeId) {
        params.put("nodeId", nodeId);
        params.put("token", Account.get().token);
    }

    @Override
    protected String getUrlAction() {
        return "/knowledgeList";
    }

    @Override
    protected void onParser(JSONObject response) {
        JSONArray array = response.optJSONArray("knowledgeList");
        Knowledge item = null;
        JSONObject jsonObject = null;
        try {
            for (int i = 0; i < array.length(); i++) {
                jsonObject = array.getJSONObject(i);
                item = new Knowledge();
                item.id = jsonObject.optString("id");
                item.isNewRecord = jsonObject.optBoolean("isNewRecord");
                item.createDate = jsonObject.optString("createDate");
                item.updateDate = jsonObject.optString("updateDate");
                item.nodeId = jsonObject.optString("nodeId");
                item.name = jsonObject.optString("name");
                item.content = jsonObject.optString("content");
                item.stateFlag = jsonObject.optInt("stateFlag");
                knowledges.add(item);
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
//            jsonObject.put("nodeId", "nodeId");
//            jsonObject.put("name", "imageName");
//            jsonObject.put("content", "attachimafgeName");
//            jsonObject.put("stateFlag", 1);
//            array.put(jsonObject);
//
//            jsonObject = new JSONObject();
//            jsonObject.put("id", "id1");
//            jsonObject.put("isNewRecord", true);
//            jsonObject.put("createDate", "createDate1");
//            jsonObject.put("updateDate", "updateDate1");
//            jsonObject.put("nodeId", "nodeId1");
//            jsonObject.put("name", "imageName");
//            jsonObject.put("content", "attachimafgeNamsdfsadfsadfsadlfksdlfjsadl;fjsdlfjsdfl;sadfjsladfjlasdjflvlsdf是莲富大厦快来东风e");
//            jsonObject.put("stateFlag", 1);
//            array.put(jsonObject);
//
//            jsonObject = new JSONObject();
//            jsonObject.put("id", "id2");
//            jsonObject.put("isNewRecord", true);
//            jsonObject.put("createDate", "createDate2");
//            jsonObject.put("updateDate", "updateDate2");
//            jsonObject.put("nodeId", "nodeId2");
//            jsonObject.put("name", "imageName");
//            jsonObject.put("content", "attachimafgeName");
//            jsonObject.put("stateFlag", 1);
//            array.put(jsonObject);
//            object.put("knowledgeList", array);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return object.toString();
//    }
    
    public List<Knowledge> getKnowledges() {
        return this.knowledges;
    }
}
