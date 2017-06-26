package com.architecture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.architecture.R;
import com.architecture.activity.base.BaseActivity;
import com.architecture.common.data.Common;
import com.architecture.context.IntentCode;
import com.architecture.models.adapter.CadImageAdapter;
import com.architecture.models.adapter.ConstructionVideoAdapter;
import com.architecture.models.api.BaseOperater;
import com.architecture.models.api.GetImgListOperater;
import com.architecture.models.api.GetVideoListOperater;
import com.architecture.models.entry.CadImage;
import com.architecture.models.entry.ConstructionVideo;
import com.architecture.models.entry.ProjectInfo;
import com.architecture.utils.ViewInject;
import com.architecture.view.TitleBar;
import com.architecture.widget.grid.StaggeredGridView;

import java.util.List;

/**
 * Created by 20141022 on 2016/11/17.
 * 图纸列表页
 */
public class VideoListActivity extends BaseActivity{
    
    @ViewInject("titlebar")
    private TitleBar titleBar;
    @ViewInject("tv_code")
    private TextView tvCode;
    @ViewInject("tv_node_name")
    private TextView tvNodename;
    @ViewInject(value = "gv_video")
    private StaggeredGridView gvVideo;
    
    private ProjectInfo projectInfo;
    private int videoType;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
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
        videoType = intent.getIntExtra(IntentCode.INTENT_VIDEO_TYPE, ProjectInfoActivity.TYPE_CONSTRUCTION);
    }

    @Override
    protected void onApplyData() {
        super.onApplyData();
        if (videoType == ProjectInfoActivity.TYPE_CONSTRUCTION) {
            titleBar.setTitle(R.string.title_construction_video);
        } else {
            titleBar.setTitle(R.string.title_bim);
        }
        tvCode.setText(getString(R.string.code, projectInfo.nodeCode));
        tvNodename.setText(getString(R.string.node_name, projectInfo.nodeNames));
        getVideoList();
    }
    
    private void getVideoList() {
        final GetVideoListOperater operater = new GetVideoListOperater(this);
        operater.setParams(projectInfo.id, videoType);
        operater.onReq(new BaseOperater.RspListener() {
            @Override
            public void onRsp(boolean success, Object obj) {
                if (success) {
                    List<ConstructionVideo> constructionVideos = operater.getCadImages();
                    if (constructionVideos != null && !constructionVideos.isEmpty()) {
                        ConstructionVideoAdapter mAdapter = new ConstructionVideoAdapter(VideoListActivity.this, gvVideo);
                        mAdapter.setData(constructionVideos, projectInfo, videoType);
                        gvVideo.setAdapter(mAdapter);
                    }
                }
            }
        });
    }

}
