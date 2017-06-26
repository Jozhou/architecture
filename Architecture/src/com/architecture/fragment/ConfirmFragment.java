package com.architecture.fragment;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.architecture.activity.base.BaseActivity;
import com.architecture.context.IntentCode;
import com.architecture.fragment.base.BaseDialogFragment;
import com.architecture.fragment.base.IDialogInterface;
import com.architecture.utils.ButtonUtils;
import com.architecture.utils.ViewInject;
import com.architecture.R;

public class ConfirmFragment extends BaseDialogFragment implements OnClickListener {

    private static final int MSGWHAT_UPDATETIME = 0;
    private static final int MSGWHAT_CANCELTIME = 1;
    
	@ViewInject("fragment_bg_click")
	private View rootView;
	@ViewInject("img_tip_icon")
	private ImageView imgTitle;
	@ViewInject("confirm_text")
	private TextView tvTitle;
	@ViewInject("confirm_desc")
	private TextView tvContent;
	@ViewInject("ok_btn")
	private Button btnOk;
	@ViewInject("close_btn")
	private Button btnCancel;

	private CharSequence title = "";
	private int titleGravity;
	private int titleIcon;
	private CharSequence content = "";
	private int contentGravity;
	private String ok = "";
	private String cancel = "";
	private int autoClose;
	private boolean addToBackstack;
	private IDialogInterface listener;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSGWHAT_UPDATETIME:
                	autoClose -= 1000;
                    if (autoClose > 0) {
                    	resetAutoCloseText();
                    } else {
                    	mHandler.removeCallbacksAndMessages(null);
                        return;
                    }
                    break;
                    
                case MSGWHAT_CANCELTIME:
                	mHandler.removeCallbacksAndMessages(null);
                	if (getActivity() != null && getActivity() instanceof BaseActivity && !TextUtils.isEmpty(getTag())) {
                		((BaseActivity) getActivity()).closeFragment(getTag());
                	} else {
                		finish();
                	}
        			if (listener != null) {
        				boolean result = TextUtils.isEmpty(cancel);
        				listener.onCallBack(result);
        			}
                    break;
                    
                default:
                    break;
            }
		};
	};

	@Override
	protected int getContentResId() {
		return R.layout.confirm_fragment;
	}

	@Override
	protected void onQueryArguments() {
		super.onQueryArguments();
		title = getQueryParamCharSequence(IntentCode.INTENT_TITLE);
		titleGravity = getQueryParamInteger(IntentCode.INTENT_TITLE_GRAVITY, Gravity.CENTER);
		titleIcon = getQueryParamInteger(IntentCode.INTENT_TITLE_ICON, 0);
		content = getQueryParamCharSequence(IntentCode.INTENT_MESSAGE);
		contentGravity = getQueryParamInteger(IntentCode.INTENT_CONTENT_GRAVITY, Gravity.CENTER);
		ok = getQueryParamString(IntentCode.INTENT_OKTEXT);
		cancel = getQueryParamString(IntentCode.INTENT_CANCELTEXT);
		autoClose = getQueryParamInteger(IntentCode.INTENT_AUTOCLOSE, 0);
		addToBackstack = getQueryParamBoolean(IntentCode.INTENT_ADDTOBACKSTACK, true);
		setCancelable(false);
	}

	@Override
	protected void onApplyData() {
		super.onApplyData();
		setTitle(title);
		setTitleGravity(titleGravity);
		setTitleIcon(titleIcon);
		setContent(content);
		setContentGravity(contentGravity);
		setPositive(ok);
		setNegative(cancel);
		if (autoClose > 0) {
			resetAutoCloseText();
            mHandler.sendEmptyMessageDelayed(MSGWHAT_CANCELTIME, autoClose);
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		if (listener != null) {
			listener.onDismiss();
		}
	}

	public void setTitle(CharSequence text) {
		if (!TextUtils.isEmpty(text)) {
			tvTitle.setVisibility(View.VISIBLE);
			tvTitle.setText(text);
		} else {
			tvTitle.setVisibility(View.GONE);
		}
	}

	public void setTitleGravity(int gravity) {
		tvTitle.setGravity(gravity);
	}

	public void setTitleIcon(int icon) {
		if (icon == 0) {
			imgTitle.setVisibility(View.GONE);
		} else {
			imgTitle.setVisibility(View.VISIBLE);
			imgTitle.setImageResource(icon);
		}
	}

	public void setContent(CharSequence text) {
		if (!TextUtils.isEmpty(text)) {
			tvContent.setVisibility(View.VISIBLE);
			tvContent.setText(text);
		} else {
			tvContent.setVisibility(View.GONE);
		}
	}

	public void setContentGravity(int gravity) {
		tvContent.setGravity(gravity);
	}

	public void setPositive(String text) {
		btnOk.setText(text);
	}

	public void setNegative(String text) {
		if (!TextUtils.isEmpty(text)) {
			btnCancel.setText(text);
			btnCancel.setVisibility(View.VISIBLE);
		} else {
			btnCancel.setVisibility(View.GONE);
		}
	}

	public void setDialogListener(IDialogInterface listener) {
		this.listener = listener;
	}

    protected void resetAutoCloseText() {
        Message msg = mHandler.obtainMessage();
        msg.what = MSGWHAT_UPDATETIME;
        mHandler.sendMessageDelayed(msg, 1000);
        if (!TextUtils.isEmpty(cancel)) {
        	setNegative(cancel + "(" + autoClose / 1000 + ")");
        } else {
        	setPositive(ok + "(" + autoClose / 1000 + ")");
        }
    }

	@Override
	public void onClick(View v) {
	    if (ButtonUtils.isFastClick()) {
	        return;
	    }
		int id = v.getId();
		if (id == R.id.fragment_bg_click) {
			return;
		} else if (id == R.id.close_btn) {
        	mHandler.removeCallbacksAndMessages(null);
			finish();
			if (listener != null) {
				listener.onCallBack(false);
			}
		} else if (id == R.id.ok_btn) {
			setNegative(cancel);
        	mHandler.removeCallbacksAndMessages(null);
			finish();
			if (listener != null) {
				listener.onCallBack(true);
			}
		}
	}
	
	@Override
	public void finish() {
		if (!addToBackstack) {
			if (getActivity() != null && getActivity() instanceof BaseActivity && !TextUtils.isEmpty(getTag())) {
        		((BaseActivity) getActivity()).closeFragment(getTag());
        	} else {
    			super.finish();
        	}
		} else {
			super.finish();
		}
	}

}