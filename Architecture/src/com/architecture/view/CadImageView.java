package com.architecture.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.architecture.R;
import com.architecture.activity.ImageDetailActivity;
import com.architecture.context.Config;
import com.architecture.context.IntentCode;
import com.architecture.models.entry.CadImage;
import com.architecture.models.entry.ProjectInfo;
import com.architecture.utils.ViewInject;
import com.architecture.widget.imageview.MThumbImageView;
import com.architecture.widget.layoutview.MLinearLayout;

/**
 * Created by 20141022 on 2017/1/18.
 */
public class CadImageView extends MLinearLayout<CadImage>{

    @ViewInject("tv_image_name")
    public TextView txtName;
    @ViewInject("img_cad")
    public MThumbImageView imgCad;

    private int position;
    private ProjectInfo projectInfo;

    public CadImageView(Context context) {
        super(context);
    }

    public CadImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CadImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.item_cad_image;
    }

    @Override
    protected void onBindListener() {
        super.onBindListener();
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, ImageDetailActivity.class);
                intent.putExtra(IntentCode.INTENT_IMAGE_POS, position);
                intent.putExtra(IntentCode.INTENT_PROJECT_INFO, projectInfo);
                mContext.startActivity(intent);
            }
        });
    }

    public void setDataSource(CadImage cadImage,int position, ProjectInfo projectInfo) {
        super.setDataSource(cadImage);
        this.position = position;
        this.projectInfo = projectInfo;
    }

    @Override
    protected void onApplyData() {
        if (mDataItem != null) {
            txtName.setText(mContext.getResources().getString(R.string.cad_image_name, mDataItem.imageName));
            imgCad.setLoadingResID(R.drawable.placeholder);
            imgCad.setLoadErrResID(R.drawable.placeholder);
            imgCad.setImageUrl(Config.getImageUrl() + mDataItem.attachimafgeName);
            imgCad.setClickable(false);
        }
    }
}
