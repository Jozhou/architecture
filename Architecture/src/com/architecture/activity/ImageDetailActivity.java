package com.architecture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.architecture.R;
import com.architecture.activity.base.BaseActivity;
import com.architecture.common.data.Common;
import com.architecture.context.IntentCode;
import com.architecture.models.adapter.CadImageDetailAdapter;
import com.architecture.models.entry.CadImage;
import com.architecture.models.entry.ProjectInfo;
import com.architecture.utils.ViewInject;
import com.architecture.view.TitleBar;

import java.util.List;

/**
 * Created by 20141022 on 2016/11/17.
 * 图纸详情页
 */
public class ImageDetailActivity extends BaseActivity{
    
    @ViewInject("titlebar")
    private TitleBar titleBar;
    @ViewInject("tv_code")
    private TextView tvCode;
    @ViewInject("tv_node_name")
    private TextView tvNodename;
    @ViewInject(value = "vp_image")
    private ViewPager vpImage;

    private ProjectInfo projectInfo;
    private List<CadImage> cadImages;
    private int curPos;
    private CadImageDetailAdapter mAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
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
        cadImages = Common.get().getCadImages();
        curPos = intent.getIntExtra(IntentCode.INTENT_IMAGE_POS, 0);
    }

    @Override
    protected void onApplyData() {
        super.onApplyData();
        tvCode.setText(getString(R.string.code, projectInfo.nodeCode));
        tvNodename.setText(getString(R.string.node_name, projectInfo.nodeNames));
        mAdapter = new CadImageDetailAdapter(this);
        mAdapter.setData(cadImages);
        vpImage.setAdapter(mAdapter);
        vpImage.setCurrentItem(curPos);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        int pos = vpImage.getCurrentItem();
        if (id == R.id.btn_pre) {
            if (pos == 0) {
                showToastMessage(getText(R.string.already_first).toString());
            } else {
                vpImage.setCurrentItem(pos - 1);
            }
        } else if (id == R.id.btn_next) {
            if (pos == cadImages.size()-1) {
                showToastMessage(getText(R.string.already_last).toString());
            } else {
                vpImage.setCurrentItem(pos + 1);
            }
        }
    }
}
