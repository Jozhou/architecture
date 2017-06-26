package com.architecture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.architecture.R;
import com.architecture.activity.base.BaseActivity;
import com.architecture.common.data.Common;
import com.architecture.context.IntentCode;
import com.architecture.models.adapter.CadImageAdapter;
import com.architecture.models.api.BaseOperater;
import com.architecture.models.api.GetImgListOperater;
import com.architecture.models.entry.CadImage;
import com.architecture.models.entry.ProjectInfo;
import com.architecture.utils.ViewInject;
import com.architecture.view.CadImageView;
import com.architecture.view.DoubleListView;
import com.architecture.view.TitleBar;
import com.architecture.widget.grid.StaggeredGridView;

import java.util.List;

/**
 * Created by 20141022 on 2016/11/17.
 * 图纸列表页
 */
public class ImageListActivity extends BaseActivity {

    @ViewInject("titlebar")
    private TitleBar titleBar;
    @ViewInject("tv_code")
    private TextView tvCode;
    @ViewInject("tv_node_name")
    private TextView tvNodename;
    @ViewInject(value = "gv_image")
    private StaggeredGridView gvImage;
//    @ViewInject("gv_image")
//    private DoubleListView gvImage;

    private ProjectInfo projectInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
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
        getImageList();
    }

    private void getImageList() {
        List<CadImage> cadImages = Common.get().getCadImages();
        if (cadImages != null && !cadImages.isEmpty()) {
            CadImageAdapter mAdapter = new CadImageAdapter(ImageListActivity.this);
            mAdapter.setData(cadImages, projectInfo);
            gvImage.setAdapter(mAdapter);
//            initListView(cadImages);
            return;
        }
        final GetImgListOperater operater = new GetImgListOperater(this);
        operater.setParams(projectInfo.id);
        operater.onReq(new BaseOperater.RspListener() {
            @Override
            public void onRsp(boolean success, Object obj) {
                if (success) {
                    List<CadImage> cadImages = operater.getCadImages();
                    if (cadImages != null && !cadImages.isEmpty()) {
                        CadImageAdapter mAdapter = new CadImageAdapter(ImageListActivity.this);
                        mAdapter.setData(cadImages, projectInfo);
                        gvImage.setAdapter(mAdapter);
//                        initListView(cadImages);
                        Common.get().setCadImages(cadImages);
                    }
                }
            }
        });
    }

//    public void initListView(List<CadImage> cadImages) {
//        CadImageView cadImageView = null;
//        CadImage cadImage = null;
//        for(int i=0; i< cadImages.size(); i++) {
//            cadImage = cadImages.get(i);
//            cadImageView = new CadImageView(this);
//            cadImageView.setDataSource(cadImage, i, projectInfo);
//            gvImage.addItem(cadImageView, i);
//        }
//    }

}
