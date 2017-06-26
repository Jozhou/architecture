package com.architecture.models.entry;

/**
 * Created by 20141022 on 2016/11/15.
 * 节点信息
 */
public class NodeInfo extends  BaseEntry {

    public String id = "";
    public boolean isNewRecord;
    public String createDate = "";
    public String updateDate = "";
    public String nodeCode = ""; //节点编号
    public String nodeNames = ""; //节点名称
    public String projectName = ""; //工程项目名称
    public String buildingInfo = ""; //建筑信息
    public String equipInfo = ""; //设备信息
    public String materialInfo = ""; //材料信息
    public String status = "";
    public int stateFlag;
}
