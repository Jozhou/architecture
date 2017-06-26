package com.architecture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.architecture.R;
import com.architecture.activity.base.BaseActivity;
import com.architecture.common.data.Common;
import com.architecture.models.adapter.NodeAdapter;
import com.architecture.models.entry.NodeInfo;
import com.architecture.models.entry.ProjectInfo;
import com.architecture.utils.ViewInject;
import com.architecture.view.TitleBar;

import java.util.List;

/**
 * Created by Administrator on 2017/4/17.
 */
public class SearchListActivity extends BaseActivity{

    @ViewInject("titlebar")
    private TitleBar titleBar;
    @ViewInject("lv_node")
    private ListView lvSearch;

    private List<ProjectInfo> projectInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
    }

    @Override
    protected void onBindListener() {
        super.onBindListener();
        titleBar.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onQueryArguments(Intent intent) {
        super.onQueryArguments(intent);
    }

    @Override
    protected void onApplyData() {
        super.onApplyData();
        projectInfos = Common.get().getProjectInfos();
        if(projectInfos != null) {
            NodeAdapter nodeAdapter = new NodeAdapter(this);
            nodeAdapter.setData(projectInfos);
            lvSearch.setAdapter(nodeAdapter);
        }
    }
}
