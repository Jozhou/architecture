package com.architecture.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.architecture.R;
import com.architecture.activity.base.BaseActivity;
import com.architecture.common.data.Common;
import com.architecture.context.Config;
import com.architecture.context.IntentCode;
import com.architecture.models.adapter.CadImageAdapter;
import com.architecture.models.api.BaseOperater;
import com.architecture.models.api.GetImgListOperater;
import com.architecture.models.entry.CadImage;
import com.architecture.models.entry.ProjectInfo;
import com.architecture.utils.ViewInject;
import com.architecture.view.TitleBar;
import com.architecture.widget.CheckOverSizeTextView;
import com.architecture.widget.imageview.MResizeThumbImageView;

import java.util.List;

/**
 * Created by 20141022 on 2016/11/17.
 * 二维码详细页面
 */
public class ProjectInfoActivity extends BaseActivity{
    
    @ViewInject("titlebar")
    private TitleBar titleBar;
    @ViewInject("tv_code")
    private TextView tvCode;
    @ViewInject("tv_node_name")
    private TextView tvNodename;
    @ViewInject("tv_pro_name")
    private TextView tvProName;
    @ViewInject(value = "btn_cad", setClickListener = true)
    private Button btnCad;
    @ViewInject("img_cad")
    private MResizeThumbImageView imgCad;
    @ViewInject("tv_image_text_content")
    private CheckOverSizeTextView tvImageText;
    @ViewInject(value = "ll_stretch_image", setClickListener = true)
    private View llStrechImage;
    @ViewInject("tv_more_image")
    private TextView tvMoreImage;
    @ViewInject("img_arrow_image")
    private ImageView imgArrowImage;
    @ViewInject("tv_attentions_content")
    private CheckOverSizeTextView tvAttentions;
    @ViewInject(value = "ll_stretch_attentions", setClickListener = true)
    private View llAttentions;
    @ViewInject("tv_more_attentions")
    private TextView tvMoreAttentions;
    @ViewInject("img_more_attentions")
    private ImageView imgArrowAttentions;
    @ViewInject("tv_construction_points_content")
    private CheckOverSizeTextView tvConstructionPoints;
    @ViewInject(value = "ll_stretch_construction_points", setClickListener = true)
    private View llStrechConstructionPoints;
    @ViewInject("tv_more_construction_points")
    private TextView tvMoreConstructionPoints;
    @ViewInject("img_construction_points")
    private ImageView imgArrowConstructionPoints;
    
    @ViewInject(value = "tv_construction", setClickListener = true)
    private TextView tvConstruction;
    @ViewInject(value = "tv_bim", setClickListener = true)
    private TextView tvBim;
    @ViewInject(value = "tv_repository", setClickListener = true)
    private TextView tvRepository;
    @ViewInject(value = "tv_architecture", setClickListener = true)
    private TextView tvArchitecture;
    @ViewInject(value = "tv_equipment", setClickListener = true)
    private TextView tvEquipment;
    
    private ProjectInfo projectInfo;
    private static final int MAX_LINE = 3;
    private Bitmap downBitmap;
    private Bitmap upBitmap;
    public static final int TYPE_CONSTRUCTION = 1;
    public static final int TYPE_BIM = 2;
    
    class OverSizeChangedListener implements CheckOverSizeTextView.OnOverSizeChangedListener {
        private View vLayout;
        private CheckOverSizeTextView checkOverSizeTextView;
        private TextView textView;
        private ImageView imageView;
        public OverSizeChangedListener (View vLayout, CheckOverSizeTextView checkOverSizeTextView, TextView textView, ImageView imageView) {
            this.checkOverSizeTextView = checkOverSizeTextView;
            this.textView = textView;
            this.imageView = imageView;
            this.vLayout = vLayout;
        }
        @Override
        public void onChanged(boolean isOverSize) {
            if (isOverSize) {
                vLayout.setVisibility(View.VISIBLE);
                textView.setText(R.string.btn_display_more);
                imageView.setImageBitmap(downBitmap);
            } else {
                if (checkOverSizeTextView.getMaxLineLowV() == MAX_LINE) {
                    vLayout.setVisibility(View.GONE);
                } else {
                    vLayout.setVisibility(View.VISIBLE);
                    textView.setText(R.string.btn_dismiss_more);
                    imageView.setImageBitmap(upBitmap);
                }
            }
        }
    }
    
    class ClickListener implements View.OnClickListener {
        private CheckOverSizeTextView textView;
        
        public ClickListener (CheckOverSizeTextView textView) {
            this.textView = textView;
        }
        @Override
        public void onClick(View v) {
            if (textView.getMaxLineLowV() == MAX_LINE) {
                textView.displayAll();
            } else {
                textView.hide(MAX_LINE);
            }
        }
    }
            
            

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projectinfo);
        downBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_qrcode_down);
        upBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_qrcode_down);
        Matrix matrix = new Matrix();
        matrix.setRotate(180);
        upBitmap = Bitmap.createBitmap(upBitmap, 0, 0, upBitmap.getWidth(),upBitmap.getHeight(), matrix, true);
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
        tvProName.setText(getString(R.string.pro_name, projectInfo.projectName));
        tvImageText.setText(projectInfo.graphicInfo);
        tvAttentions.setText(projectInfo.attention);
        tvConstructionPoints.setText(projectInfo.essentials);
        llStrechImage.setOnClickListener(new ClickListener(tvImageText));
        llAttentions.setOnClickListener(new ClickListener(tvAttentions));
        llStrechConstructionPoints.setOnClickListener(new ClickListener(tvConstructionPoints));
        tvImageText.setOnOverLineChangedListener(new OverSizeChangedListener(llStrechImage, tvImageText, tvMoreImage, imgArrowImage));
        tvAttentions.setOnOverLineChangedListener(new OverSizeChangedListener(llAttentions, tvAttentions, tvMoreAttentions, imgArrowAttentions));
        tvConstructionPoints.setOnOverLineChangedListener(new OverSizeChangedListener(llStrechConstructionPoints, tvConstructionPoints, tvMoreConstructionPoints, imgArrowConstructionPoints));
        tvImageText.hide(MAX_LINE);
        tvAttentions.hide(MAX_LINE);
        tvConstructionPoints.hide(MAX_LINE);
        imgCad.setLoadingResID(R.drawable.placeholder);
        imgCad.setLoadErrResID(R.drawable.placeholder);
        getImageList();
    }

    private void getImageList() {
        final GetImgListOperater operater = new GetImgListOperater(this);
        operater.setParams(projectInfo.id);
        operater.onReq(new BaseOperater.RspListener() {
            @Override
            public void onRsp(boolean success, Object obj) {
                if (success) {
                    List<CadImage> cadImages = operater.getCadImages();
                    if (cadImages != null && !cadImages.isEmpty()) {
                        CadImage cadImage = cadImages.get(0);
                        imgCad.setImageUrl(Config.getImageUrl() + cadImage.attachimafgeName);
                        Common.get().setCadImages(cadImages);
                    } else {
                        imgCad.setImageResource(R.drawable.placeholder);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.btn_cad) {
            Intent intent = new Intent();
            intent.putExtra(IntentCode.INTENT_PROJECT_INFO, projectInfo);
            startActivity(ImageListActivity.class, intent);
        } else if (id == R.id.tv_construction) {
            Intent intent = new Intent();
            intent.putExtra(IntentCode.INTENT_PROJECT_INFO, projectInfo);
            intent.putExtra(IntentCode.INTENT_VIDEO_TYPE, TYPE_CONSTRUCTION);
            startActivity(VideoListActivity.class, intent);
        } else if (id == R.id.tv_bim) {
            Intent intent = new Intent();
            intent.putExtra(IntentCode.INTENT_PROJECT_INFO, projectInfo);
            intent.putExtra(IntentCode.INTENT_VIDEO_TYPE, TYPE_BIM);
            startActivity(VideoListActivity.class, intent);
        } else if (id == R.id.tv_repository) {
            Intent intent = new Intent();
            intent.putExtra(IntentCode.INTENT_PROJECT_INFO, projectInfo);
            startActivity(RepositoryActivity.class, intent);
        } else if (id == R.id.tv_architecture) {
            Intent intent = new Intent();
            intent.putExtra(IntentCode.INTENT_TITLE, getText(R.string.title_architecture));
            intent.putExtra(IntentCode.INTENT_CONTENT, projectInfo.materialInfo);
            startActivity(ArchitectureActivity.class, intent);
        } else if (id == R.id.tv_equipment) {
            Intent intent = new Intent();
            intent.putExtra(IntentCode.INTENT_TITLE, getText(R.string.title_equipment));
            intent.putExtra(IntentCode.INTENT_CONTENT, projectInfo.equipInfo);
            startActivity(ArchitectureActivity.class, intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (downBitmap != null) {
            downBitmap.recycle();
        }
        if (upBitmap != null) {
            upBitmap.recycle();
        }
    }
}
