package com.architecture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.architecture.R;
import com.architecture.activity.base.BaseActivity;
import com.architecture.common.data.Account;
import com.architecture.common.data.Common;
import com.architecture.context.IntentCode;
import com.architecture.context.RequestCode;
import com.architecture.models.api.BaseOperater;
import com.architecture.models.api.GetNodeMgrInfoOperater;
import com.architecture.models.api.GetNodeMgrListOperater;
import com.architecture.models.entry.ProjectInfo;
import com.architecture.utils.ViewInject;
import com.architecture.view.TitleBar;
import com.architecture.zxing.MipcaActivityCapture;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by 20141022 on 2016/11/14.
 * 首页
 */
public class MainActivity extends BaseActivity {

    @ViewInject("titlebar")
    private TitleBar titleBar;
    @ViewInject("tv_major")
    private TextView tvMajor;
    @ViewInject("tv_class")
    private TextView tvClass;
    @ViewInject("tv_name")
    private TextView tvName;
    @ViewInject(value="img_scan", setClickListener = true)
    private ImageView imgScan;
    @ViewInject("sp_nodename")
    private Spinner spNodename;
    @ViewInject(value = "et_project_name", setClickListener = true)
    private EditText etProname;
    @ViewInject(value = "btn_search", setClickListener = true)
    private Button btnSearch;
    @ViewInject(value="img_arrow")
    private ImageView imgArrow;
    private ListPopupWindow mPop;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    protected void onApplyData() {
        super.onApplyData();
        tvMajor.setText(getString(R.string.major, Account.get().major));
        tvClass.setText(getString(R.string.rsclass, Account.get().rsclass));
        tvName.setText(getString(R.string.name, Account.get().userName));
//        try {
//            Field field = spNodename.getClass().getDeclaredField("mPopup");
//            field.setAccessible(true);
//            mPop = (ListPopupWindow) field.get(spNodename);
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e2) {
//            e2.printStackTrace();
//        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.img_scan) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, MipcaActivityCapture.class);
            startActivityForResult(intent, RequestCode.REQUEST_CODE_SCAN);
        } else if (id == R.id.btn_search) {
            getCodeSearchResult();
        }
    }

    private void getCodeSearchResult() {
        String key = etProname.getText().toString().trim();
        if (TextUtils.isEmpty(key)) {
            showToastMessage("请输入搜索关键字");
            return;
        }
        final GetNodeMgrListOperater operater = new GetNodeMgrListOperater(this);
        operater.setShowLoading(true);
        if (spNodename.getSelectedItemPosition() == 0) {
            operater.setParams(key, "", "");
        } else if (spNodename.getSelectedItemPosition() == 1) {
            operater.setParams("", key, "");
        } else if (spNodename.getSelectedItemPosition() == 2) {
            operater.setParams("", "", key);
        }
        operater.onReq(new BaseOperater.RspListener() {
            @Override
            public void onRsp(boolean success, Object obj) {
                if (success) {
                    List<ProjectInfo> projectInfos = operater.getProjectInfo();
                    if (projectInfos != null && projectInfos.size() > 0) {
                        Common.get().setProjectInfos(projectInfos);
                        Intent intent = new Intent(MainActivity.this, SearchListActivity.class);
                        startActivity(intent);
                    } else {
                        showToastMessage(R.string.tip_search_no_data);
                    }
                }
            }
        });
    }

    private void getCodeInfo(String id) {
        final GetNodeMgrInfoOperater operater = new GetNodeMgrInfoOperater(this);
        operater.setParams(id);
        operater.setShowLoading(false);
        operater.onReq(new BaseOperater.RspListener() {
            @Override
            public void onRsp(boolean success, Object obj) {
                showLoading(false);
                if (success) {
                    ProjectInfo projectInfo = operater.getProjectInfo();
                    if (projectInfo != null && !TextUtils.isEmpty(projectInfo.id)) {
                        Intent intent = new Intent();
                        intent.putExtra(IntentCode.INTENT_PROJECT_INFO, projectInfo);
                        startActivity(ProjectInfoActivity.class, intent);
                    }
                }
            }
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RequestCode.REQUEST_CODE_SCAN:
                if(resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
                    showToastMessage(bundle.getString("result"));
                }
                break;
        }
    }
}
