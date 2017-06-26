package com.architecture.models.api;

import android.content.Context;

import com.architecture.activity.ProjectInfoActivity;
import com.architecture.common.data.Account;
import com.architecture.common.data.Common;
import com.architecture.models.entry.CadImage;
import com.architecture.models.entry.ConstructionVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 20141022 on 2016/11/18.
 * 获取施工影像列表
 */
public class GetVideoListOperater extends BaseOperater {

    private List<ConstructionVideo> constructionVideos = new ArrayList<ConstructionVideo>();
    public GetVideoListOperater(Context context) {
        super(context);
    }
    private int videoType;
    
    public void setParams (String nodeId, int videoType) {
        params.put("nodeId", nodeId);
        params.put("token", Account.get().token);
        this.videoType = videoType;
    }

    @Override
    protected String getUrlAction() {
        if (videoType == ProjectInfoActivity.TYPE_CONSTRUCTION) {
            return "/sgVideoList";
        } else if (videoType == ProjectInfoActivity.TYPE_BIM) {
            return "/bimVideoList";
        }
        return "";
    }

    @Override
    protected void onParser(JSONObject response) {
        JSONArray array = response.optJSONArray("videoList");
        ConstructionVideo item = null;
        JSONObject jsonObject = null;
        try {
            for (int i = 0; i < array.length(); i++) {
                jsonObject = array.getJSONObject(i);
                item = new ConstructionVideo();
                item.id = jsonObject.optString("id");
                item.isNewRecord = jsonObject.optBoolean("isNewRecord");
                item.createDate = jsonObject.optString("createDate");
                item.updateDate = jsonObject.optString("updateDate");
                item.nodeId = jsonObject.optString("nodeId");
                item.videoName = jsonObject.optString("videoName");
                item.videoType = jsonObject.optString("videoType");
                item.attachcoverimgName = jsonObject.optString("attachcoverimgName");
                item.videofileName = jsonObject.optString("videofileName");
                item.attachvideoName = jsonObject.optString("attachvideoName");
                item.remark = jsonObject.optString("remark").replaceAll(Common.regEx_space, "").replaceAll(Common.regEx_html, "");
                item.stateFlag = jsonObject.optInt("stateFlag");
                constructionVideos.add(item);
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
//            jsonObject.put("videoName", "videoName");
//            jsonObject.put("videoType", "videoType");
//            jsonObject.put("attachcoverimgName", "attachcoverimgName");
//            jsonObject.put("videofileName", "videofileName");
//            jsonObject.put("attachvideoName", "attachvideoName");
//            jsonObject.put("remark", "remark");
//            jsonObject.put("stateFlag", 1);
//            array.put(jsonObject);
//
//            jsonObject = new JSONObject();
//            jsonObject.put("id", "id2");
//            jsonObject.put("isNewRecord", true);
//            jsonObject.put("createDate", "createDate");
//            jsonObject.put("updateDate", "updateDate");
//            jsonObject.put("nodeId", "nodeId2");
//            jsonObject.put("videoName", "videoName2");
//            jsonObject.put("videoType", "videoType2");
//            jsonObject.put("attachcoverimgName2", "attachcoverimgName2");
//            jsonObject.put("videofileName", "videofileName2");
//            jsonObject.put("attachvideoName", "attachvideoName2");
//            jsonObject.put("remark", "remark2");
//            jsonObject.put("stateFlag", 1);
//            array.put(jsonObject);
//
//            jsonObject = new JSONObject();
//            jsonObject.put("id", "id");
//            jsonObject.put("isNewRecord", true);
//            jsonObject.put("createDate", "createDate");
//            jsonObject.put("updateDate", "updateDate");
//            jsonObject.put("nodeId", "nodeId");
//            jsonObject.put("videoName", "videoName");
//            jsonObject.put("videoType", "videoType");
//            jsonObject.put("attachcoverimgName", "attachcoverimgName");
//            jsonObject.put("videofileName", "videofileName");
//            jsonObject.put("attachvideoName", "attachvideoName");
//            jsonObject.put("remark", "remark");
//            jsonObject.put("stateFlag", 1);
//            array.put(jsonObject);
//            object.put("videoList", array);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return object.toString();
//    }
    
    public List<ConstructionVideo> getCadImages() {
        return this.constructionVideos;
    }
}
