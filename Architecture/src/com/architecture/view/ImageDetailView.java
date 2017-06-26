package com.architecture.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.architecture.R;
import com.architecture.context.Config;
import com.architecture.models.entry.CadImage;
import com.architecture.utils.DialogUtils;
import com.architecture.utils.ViewInject;
import com.architecture.widget.imageview.MThumbImageView;
import com.architecture.widget.layoutview.MRelativeLayout;

/**
 * Created by 20141022 on 2016/11/18.
 */
public class ImageDetailView extends MRelativeLayout<CadImage> {

    @ViewInject("tv_image_name")
    private TextView tvImgName;
    @ViewInject("img_cad")
    private MThumbImageView imgCad;
    @ViewInject("btn_pre")
    private Button btnPre;
    @ViewInject("btn_next")
    private Button btnNext;
    private ViewPager parent;
    
    public ImageDetailView(Context context) {
        super(context);
    }
    
    public ImageDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.item_cad_image_detail;
    }

    public void setParent(ViewPager parent) {
        this.parent = parent;
    }

    @Override
    protected void onBindListener() {
        super.onBindListener();
        btnPre.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDataItem.position == 0) {
                    DialogUtils.showToastMessage(mContext.getText(R.string.already_first).toString());
                } else {
                    if (parent != null) {
                        parent.setCurrentItem(mDataItem.position - 1);
                    }
                }
            }
        });

        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDataItem.position == parent.getAdapter().getCount()-1) {
                    DialogUtils.showToastMessage(mContext.getText(R.string.already_last).toString());
                } else {
                    parent.setCurrentItem(mDataItem.position + 1);
                }
            }
        });
    }

    @Override
    protected void onApplyData() {
        tvImgName.setText(getResources().getString(R.string.cad_image_name, mDataItem.imageName));
        imgCad.setLoadingResID(R.drawable.placeholder);
        imgCad.setLoadErrResID(R.drawable.placeholder);
        imgCad.setImageUrl(Config.getImageUrl() + mDataItem.attachimafgeName);
    }
}
