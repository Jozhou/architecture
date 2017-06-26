package com.architecture.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.architecture.R;
import com.architecture.activity.NetworkHelper;
import com.architecture.activity.ProjectInfoActivity;
import com.architecture.activity.base.BaseActivity;
import com.architecture.common.data.Account;
import com.architecture.common.data.Common;
import com.architecture.context.Config;
import com.architecture.context.IntentCode;
import com.architecture.models.entry.ConstructionVideo;
import com.architecture.models.entry.ProjectInfo;
import com.architecture.utils.LogcatUtils;
import com.architecture.utils.ViewInject;
import com.architecture.view.PlayerView;
import com.architecture.view.TitleBar;
import com.architecture.widget.imageview.MResizeThumbImageView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 20141022 on 2016/11/17.
 * 图纸详情页
 */
public class ConstructionVideoDetailActivity3 extends BaseActivity {
    
    @ViewInject("titlebar")
    private TitleBar titleBar;
    @ViewInject("tv_code")
    private TextView tvCode;
    @ViewInject("tv_node_name")
    private TextView tvNodename;
    @ViewInject("tv_title")
    private TextView tvTitle;
    @ViewInject("tv_author")
    private TextView tvAuthor;
    @ViewInject("tv_remark")
    private TextView tvRemark;
    @ViewInject("view_player")
    private PlayerView playerView;

    private ProjectInfo projectInfo;
    private ConstructionVideo constructionVideo;
    private int videoType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail3);
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
        constructionVideo = (ConstructionVideo) intent.getSerializableExtra(IntentCode.INTENT_CONSTRUCTION_VIDEO);
        videoType = intent.getIntExtra(IntentCode.INTENT_VIDEO_TYPE, ProjectInfoActivity.TYPE_CONSTRUCTION);
    }

    @Override
    protected void onApplyData() {
        super.onApplyData();
        // 保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (videoType == ProjectInfoActivity.TYPE_CONSTRUCTION) {
            titleBar.setTitle(R.string.title_construction_video);
        } else {
            titleBar.setTitle(R.string.title_bim);
        }
        tvCode.setText(getString(R.string.code, projectInfo.nodeCode));
        tvNodename.setText(getString(R.string.node_name, projectInfo.nodeNames));
        tvTitle.setText(getString(R.string.video_title, constructionVideo.videoName));
        tvAuthor.setText(getString(R.string.video_author, constructionVideo.videoType));
        tvRemark.setText(constructionVideo.remark);
        int screenWidth = com.architecture.manager.WindowManager.get().getScreenWidth();
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mVv.getLayoutParams();
////        params.width = screenWidth - getResources().getDimensionPixelOffset(R.dimen.dd_dimen_60px);
//        params.height = (int) (3.0f/4 * screenWidth);
        playerView.setData(Config.getImageUrl() + constructionVideo.attachcoverimgName,
                Config.getVideoUrl() + constructionVideo.attachvideoName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        playerView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        playerView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        playerView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        playerView.onDestroy();
    }
}
