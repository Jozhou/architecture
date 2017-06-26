package com.architecture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.architecture.R;
import com.architecture.activity.base.BaseActivity;
import com.architecture.context.IntentCode;
import com.architecture.models.adapter.KnowledgeAdapter;
import com.architecture.models.api.BaseOperater;
import com.architecture.models.api.GetKnowledgeOperater;
import com.architecture.models.entry.Knowledge;
import com.architecture.models.entry.ProjectInfo;
import com.architecture.utils.ViewInject;
import com.architecture.view.RepositoryHeadView;
import com.architecture.view.TitleBar;

import java.util.List;

/**
 * Created by 20141022 on 2016/11/17.
 * 知识库列表页
 */
public class RepositoryActivity extends BaseActivity{
    
    @ViewInject("titlebar")
    private TitleBar titleBar;
    @ViewInject("tv_code")
    private TextView tvCode;
    @ViewInject("tv_node_name")
    private TextView tvNodename;
    @ViewInject(value = "lv_knowledge")
    private ListView lvKnowledge;
    
    private ProjectInfo projectInfo;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository);
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
        projectInfo = (ProjectInfo) intent.getSerializableExtra(IntentCode.INTENT_PROJECT_INFO);
    }

    @Override
    protected void onApplyData() {
        super.onApplyData();
        tvCode.setText(getString(R.string.code, projectInfo.nodeCode));
        tvNodename.setText(getString(R.string.node_name, projectInfo.nodeNames));
        getKnowledgeList();
    }
    
    private void getKnowledgeList() {
        final GetKnowledgeOperater operater = new GetKnowledgeOperater(this);
        operater.setParams(projectInfo.id);
        operater.onReq(new BaseOperater.RspListener() {
            @Override
            public void onRsp(boolean success, Object obj) {
                if (success) {
                    List<Knowledge> knowledges = operater.getKnowledges();
                    if (knowledges != null && !knowledges.isEmpty()) {
                        RepositoryHeadView headView = new RepositoryHeadView(RepositoryActivity.this);
                        lvKnowledge.addHeaderView(headView);
                        KnowledgeAdapter mAdapter = new KnowledgeAdapter(RepositoryActivity.this);
                        mAdapter.setData(knowledges);
                        lvKnowledge.setAdapter(mAdapter);
                    }
                }
            }
        });
    }

}
