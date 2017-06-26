package com.architecture.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

public class OneDecimalEditText extends EditText {

    private static final int DECIMAL_DIGITS = 1;// 最多一位小数
    private static final int MAX_VALUE = 100000; // 最大长度5

    public OneDecimalEditText(Context context) {
        super(context);
        initEditText();
    }

    public OneDecimalEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initEditText();
    }

    public OneDecimalEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initEditText();
    }

    // 初始化edittext 控件
    private void initEditText() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

                // 限制小数点后最多1位
                try {
                    if (arg0 != null && !TextUtils.isEmpty(arg0)) {
                        String temp = arg0.toString();
                        if (arg0.toString().contains(".")) {
                            if (arg0.length() - 1 - arg0.toString().indexOf(".") > DECIMAL_DIGITS) {
                                arg0 = arg0.toString().subSequence(0,
                                        arg0.toString().indexOf(".") + 1 + DECIMAL_DIGITS);
                                temp = arg0.toString();
                                setText(arg0);
                                setSelection(arg0.length());
                            }
                        }
                        if (arg0.toString().trim().substring(0).equals(".")) {
                            arg0 = "0" + arg0;
                            temp = arg0.toString();
                            setText(arg0);
                            setSelection(2);
                        }

                        if (arg0.toString().startsWith("0")
                                && arg0.toString().trim().length() > 1) {
                            if (!arg0.toString().substring(1, 2).equals(".")) {
                                temp = arg0.subSequence(0, 1).toString();
                                setText(arg0.subSequence(0, 1));
                                setSelection(1);
                            }
                        }
                        int first = arg0.toString().trim().indexOf(".");
                        if (first >= 0) {
                            String temp1 = arg0.toString().trim()
                                    .substring(0, first + 1);
                            String temp2 = arg0.toString().trim()
                                    .substring(first + 1, arg0.toString().length());
                            if (temp2.startsWith(".")) {
                                temp = temp1;
                                setText(temp1);
                                setSelection(temp1.length());
                                return;
                            } else if (temp2.endsWith(".")) {
                                temp = temp1 + temp2.substring(0, 1);
                                setText(temp1 + temp2.substring(0, 1));
                                setSelection(temp1.length() + 1);
                                return;
                            }
                        }
                        if (null != temp && !TextUtils.isEmpty(temp)
                                && Double.parseDouble(temp) >= MAX_VALUE) {
                            setText(temp.substring(0, temp.length() - 1));
                            setSelection(temp.length() - 1);
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
