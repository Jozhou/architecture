package com.architecture.view;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.architecture.R;
import com.architecture.context.Config;
import com.architecture.utils.LogcatUtils;
import com.architecture.utils.ViewInject;
import com.architecture.widget.imageview.MResizeThumbImageView;
import com.architecture.widget.imageview.MThumbImageView;
import com.architecture.widget.loading.FrameLayout;

/**
 * Created by 20141022 on 2016/12/14.
 */
public class PlayerView extends FrameLayout implements MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnInfoListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnSeekCompleteListener{

    private static final String TAG = "video";
    @ViewInject("vv_construction")
    private VideoView mVv;
//    @ViewInject("rl_vv")
//    private View mRlVv;

    @ViewInject("rl_cover")
    private View rlCover;
    @ViewInject(value = "img_cover", setClickListener = false)
    private MThumbImageView imgCover;
    @ViewInject(value = "btn_play", setClickListener = true)
    private Button btnPlay;

    private String imgUrl = "";
    private String videoUrl = "";

    public PlayerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayerView(Context context) {
        super(context);
    }

    @Override
    protected void onBindListener() {
        btnPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });
    }

    @Override
    protected void onApplyData() {
        super.onApplyData();
        int screenWidth = com.architecture.manager.WindowManager.get().getScreenWidth();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mVv.getLayoutParams();
//        params.width = screenWidth - getResources().getDimensionPixelOffset(R.dimen.dd_dimen_60px);
        params.height = (int) (9.0f/16 * screenWidth);
        mVv.setLayoutParams(params);
        imgCover.setLayoutParams(params);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.view_player;
    }

    @Override
    protected int getLoadingResId() {
        return R.layout.view_video_loading;
    }

    public void setData(String imgUrl, String videoUrl) {
        this.imgUrl = imgUrl;
        this.videoUrl = videoUrl;
        initVideoView();
    }

    private void initVideoView() {
        imgCover.setLoadErrResID(R.drawable.placeholder);
        imgCover.setLoadingResID(R.drawable.placeholder);
        imgCover.setImageUrl(imgUrl);
        rlCover.setVisibility(View.VISIBLE);
        Uri mVideoUri = Uri.parse(videoUrl);
        mVv.setVideoPath(mVideoUri.toString());

        //添加播放控制条并设置视频源
        mVv.setMediaController(new MediaController(mContext));

        mVv.setOnPreparedListener(this);
        mVv.setOnErrorListener(this);
        mVv.setOnCompletionListener(this);
        mVv.setOnInfoListener(this);
    }

    private void play() {
        gotoLoading();

        mVv.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        rlCover.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        LogcatUtils.e(TAG, "onError-what:" + what);
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        LogcatUtils.e(TAG, "onInfo-what:" + what);
        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
            gotoLoading();
        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
            gotoSuccessful();
        }
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.e(TAG, "onPrepared" + "");
        rlCover.setVisibility(View.GONE);
        gotoSuccessful();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }

    public void onResume() {
        LogcatUtils.e(TAG, "onResume");

    }

    public void onPause() {
        LogcatUtils.e(TAG, "onPause");
        mVv.pause();
    }

    public void onStop() {
        LogcatUtils.e(TAG, "onStop");
    }

    public void onDestroy() {
        LogcatUtils.e(TAG, "onDestroy");
    }
}
