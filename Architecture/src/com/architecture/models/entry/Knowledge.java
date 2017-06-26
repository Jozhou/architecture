package com.architecture.models.entry;

/**
 * Created by 20141022 on 2016/11/18.
 * 知识库实体类
 */
public class Knowledge extends  BaseEntry{
    
    public String id = "";
    public boolean isNewRecord;
    public String createDate = "";
    public String updateDate = "";
    public String nodeId = "";
    public String name = ""; //名称
    public String content = ""; //内容
    public int stateFlag;
    
}

