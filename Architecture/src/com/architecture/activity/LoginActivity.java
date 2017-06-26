package com.architecture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.architecture.R;
import com.architecture.activity.base.BaseActivity;
import com.architecture.common.data.Account;
import com.architecture.models.api.BaseOperater;
import com.architecture.models.api.LoginOperater;
import com.architecture.utils.ViewInject;

/**
 * Created by 20141022 on 2016/11/14.
 * 登录页
 */
public class LoginActivity extends BaseActivity {

    @ViewInject("et_username")
    private EditText etUsername;
    @ViewInject("et_pass")
    private EditText etPass;
    @ViewInject(value = "btn_login", setClickListener = true)
    private Button btnLogin;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onApplyData() {
        super.onApplyData();
        etUsername.setPadding(0, etUsername.getPaddingTop(), etUsername.getPaddingRight(), etUsername.getPaddingBottom());
        etPass.setPadding(0, etPass.getPaddingTop(), etPass.getPaddingRight(), etPass.getPaddingBottom());
        etUsername.setText(Account.get().name);
        etPass.setText(Account.get().password);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.btn_login) {
            loginCommit();
        }
    }
    
    private void loginCommit() {
        String username = etUsername.getText().toString();
        String pass = etPass.getText().toString();
        if (TextUtils.isEmpty(username)) {
            showToastMessage(R.string.tip_username_null);
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            showToastMessage(R.string.tip_pass_null);
            return;
        }
        _loginCommit(username, pass);
    }

    private void _loginCommit(String username, String pass) {
        final LoginOperater operater = new LoginOperater(this);
        operater.setParams(username, pass);
        operater.onReq(new BaseOperater.RspListener() {
            @Override
            public void onRsp(boolean success, Object obj) {
                if (success) {
                    startActivity(MainActivity.class);
                    finish();
                }
            }
        });
    }
}
