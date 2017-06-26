package com.architecture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.architecture.R;
import com.architecture.activity.base.BaseActivity;
import com.architecture.context.IntentCode;
import com.architecture.models.entry.ConstructionVideo;
import com.architecture.models.entry.ProjectInfo;
import com.architecture.utils.ViewInject;
import com.architecture.view.TitleBar;

/**
 * Created by 20141022 on 2016/11/17.
 * 建筑信息/设备信息页
 */
public class ArchitectureActivity extends BaseActivity{
    
    @ViewInject("titlebar")
    private TitleBar titleBar;
    @ViewInject("tv_remark")
    private TextView tvRemark;

    private String title;
    private String content;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_architecture);
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
        title = intent.getStringExtra(IntentCode.INTENT_TITLE);
        content = intent.getStringExtra(IntentCode.INTENT_CONTENT);
    }

    @Override
    protected void onApplyData() {
        super.onApplyData();
        titleBar.setTitle(title);
        tvRemark.setText(content);
    }

}
