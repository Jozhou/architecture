package com.architecture.fragment.base;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.architecture.activity.base.BaseActivity;
import com.architecture.application.App;
import com.architecture.manager.ActivityManager;
import com.architecture.utils.DialogUtils;
import com.architecture.utils.MemoryUtils;
import com.architecture.utils.ViewInjectUtils;

public abstract class BaseDialogFragment extends DialogFragment {

	public BaseDialogFragment() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		onQueryArguments();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		int resId = getContentResId();
		if(resId != 0) {
			return inflater.inflate(resId, container, false);
		} else {
			View view = getContentView(inflater, container, savedInstanceState);
			if(view == null) {
				throw new IllegalStateException("you should override getContentResId or getContentView method");
			} else {
				return view;
			}
		}
	}

	@Override
	public void onResume() {
	    super.onResume();
	}

	@Override
    public void onPause() {
        super.onPause();
    }
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		StringBuilder eventcode = new StringBuilder();
		DecimalFormat format = new DecimalFormat("0.0");
		eventcode.append("onDestroy(").
			append(format.format(MemoryUtils.getAllocated() / 1024f / 1024f)).append("mb").
			append("/").append(format.format(MemoryUtils.getTotal() / 1024f / 1024f)).
			append("mb, total:").append(MemoryUtils.getMax()).append("mb").
			append(")");
	}
	
	/**
	 * Fragment的layoutId
	 * @return
	 */
	protected int getContentResId() {
		return 0;
	}
	
	/**
	 * Fragment的layoutView
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	protected View getContentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return null;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initContentView();
	}
	
	/**
	 * 初始化Fragment
	 */
	private void initContentView() {
		onInjectView(getView());
		onFindView(getView());
		onBindListener();
		onApplyData();
	}
	
	/**
	 * 取得传递的参数
	 */
	protected void onQueryArguments() {
		
	}
	
	/**
	 * 初始化控件、获取内部控件（注释类遍历）
	 */
	protected void onInjectView(View rootView) {
		ViewInjectUtils.onInjectView(this);
	}
	
	/**
	 * 初始化控件、获取内部控件
	 */
	protected void onFindView(View rootView) {
		
	}
	
	/**
	 * 设置监听事件
	 */
	protected void onBindListener() {
		
	}
	
	/**
	 * 加载数据
	 */
	protected void onApplyData() {
		
	}
	
	/**
	 * 获取传递参数
	 * @param key
	 * @return
	 */
	public Object getQueryParam(String key) {
		Bundle bundle = getArguments();
		if(bundle != null) {
			return bundle.get(key);
		}
		return "";
	}
	
	/**
	 * 获取传递参数
	 * @param key
	 * @return
	 */
	public int getQueryParamInteger(String key) {
		return getQueryParamInteger(key, 0);
	}
	
	/**
	 * 获取传递参数
	 * @param key
	 * @param defvalue
	 * @return
	 */
	public int getQueryParamInteger(String key, int defvalue) {
		int value = defvalue;
		Bundle bundle = getArguments();
		if(bundle != null) {
			Object object = bundle.get(key);
			if(object != null) {
				try {
					value = Integer.parseInt(object.toString());
				} catch (Exception e) {
					e.printStackTrace();
					value = defvalue;
				}
			}
		}
		return value;
	}
	
	/**
	 * 获取传递参数
	 * @param key
	 * @return
	 */
	public long getQueryParamLong(String key) {
		return getQueryParamLong(key, 0);
	}
	
	/**
	 * 获取传递参数
	 * @param key
	 * @param defvalue
	 * @return
	 */
	public long getQueryParamLong(String key, long defvalue) {
		long value = defvalue;
		Bundle bundle = getArguments();
		if(bundle != null) {
			Object object = bundle.get(key);
			if(object != null) {
				try {
					value = Long.parseLong(object.toString());
				} catch (Exception e) {
					e.printStackTrace();
					value = defvalue;
				}
			}
		}
		return value;
	}
	
	/**
	 * 获取传递参数
	 * @param key
	 * @return
	 */
	public float getQueryParamFloat(String key) {
		return getQueryParamFloat(key, 0f);
	}
	
	/**
	 * 获取传递参数
	 * @param key
	 * @param defvalue
	 * @return
	 */
	public float getQueryParamFloat(String key, float defvalue) {
		float value = defvalue;
		Bundle bundle = getArguments();
		if(bundle != null) {
			Object object = bundle.get(key);
			if(object != null) {
				try {
					value = Float.parseFloat(object.toString());
				} catch (Exception e) {
					e.printStackTrace();
					value = defvalue;
				}
			}
		}
		return value;
	}
	
	/**
	 * 获取传递参数
	 * @param key
	 * @return
	 */
	public double getQueryParamDouble(String key) {
		return getQueryParamDouble(key, 0d);
	}
	
	/**
	 * 获取传递参数
	 * @param key
	 * @param defvalue
	 * @return
	 */
	public double getQueryParamDouble(String key, double defvalue) {
		double value = defvalue;
		Bundle bundle = getArguments();
		if(bundle != null) {
			Object object = bundle.get(key);
			if(object != null) {
				try {
					value = Double.parseDouble(object.toString());
				} catch (Exception e) {
					e.printStackTrace();
					value = defvalue;
				}
			}
		}
		return value;
	}
	
	/**
	 * 获取传递参数
	 * @param key
	 * @return
	 */
	public boolean getQueryParamBoolean(String key) {
		return getQueryParamBoolean(key, false);
	}
	
	/**
	 * 获取传递参数
	 * @param key
	 * @param defvalue
	 * @return
	 */
	public boolean getQueryParamBoolean(String key, boolean defvalue) {
		boolean value = defvalue;
		Bundle bundle = getArguments();
		if(bundle != null) {
			Object object = bundle.get(key);
			if(object != null) {
				try {
					value = Boolean.parseBoolean(object.toString());
				} catch (Exception e) {
					e.printStackTrace();
					value = defvalue;
				}
			}
		}
		return value;
	}
	
	/**
	 * 获取传递参数
	 * @param key
	 * @return
	 */
	public String getQueryParamString(String key) {
		return getQueryParamString(key, null);
	}
	
	/**
	 * 获取传递参数
	 * @param key
	 * @param defvalue
	 * @return
	 */
	public String getQueryParamString(String key, String defvalue) {
		String value = defvalue;
		Bundle bundle = getArguments();
		if(bundle != null) {
			Object object = bundle.get(key);
			if(object != null) {
				value = object.toString();
			}
		}
		return value;
	}
	
	/**
	 * 获取传递参数
	 * @param key
	 * @return
	 */
	public CharSequence getQueryParamCharSequence(String key) {
		return getQueryParamCharSequence(key, null);
	}
	
	/**
	 * 获取传递参数
	 * @param key
	 * @param defvalue
	 * @return
	 */
	public CharSequence getQueryParamCharSequence(String key, CharSequence defvalue) {
		CharSequence value = defvalue;
		Bundle bundle = getArguments();
		if(bundle != null) {
			final CharSequence cs = bundle.getCharSequence(key);
	        if(cs != null) {
	        	value = cs;
	        }
		}
		return value;
	}
	
	/**
	 * 获取传递参数
	 * @param key
	 * @return
	 */
	public String[] getQueryParamStringArray(String key) {
		Bundle bundle = getArguments();
		if(bundle != null) {
			return bundle.getStringArray(key);
		}
		return null;
	}
	
	/**
	 * 获取传递参数
	 * @param key
	 * @return
	 */
	public ArrayList<String> getQueryParamStringArrayList(String key) {
		Bundle bundle = getArguments();
		if(bundle != null) {
			return bundle.getStringArrayList(key);
		}
		return null;
	}
	
	/**
	 * 获取传递参数
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getQueryParamSerializable(String key) {
		Bundle bundle = getArguments();
		if(bundle != null) {
			Serializable sz = bundle.getSerializable(key);
			if(sz != null) {
				return (T) sz;
			}
		}
		return null;
	}
	
	/**
	 * 获取传递参数
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getQueryParamParcelable(String key) {
		Bundle bundle = getArguments();
		if(bundle != null) {
			Parcelable p = bundle.getParcelable(key);
			if(p != null) {
				return (T) p;
			}
		}
		return null;
	}

	/**
	 * 显示等待进度条
	 * @param show
	 */
	public void showLoading(final boolean show) {
		Activity activity = getActivity();
		if (activity == null) {
			activity = ActivityManager.get().currentActivity();
		}
		if (activity != null) {
			if(activity instanceof BaseActivity) {
				((BaseActivity) activity).showLoading(show);
			}
		}
	}

	/**
	 * 取得Resource资源
	 * @return
	 */
	public Resources getAppResources() {
		return getActivity() != null ? getActivity().getResources() : 
			App.getInstance().getResources();
	}
	
	/**
	 * 设置返回结果
	 * @param resultCode
	 */
	public void setResult(int resultCode) {
		setResult(resultCode);
	}
	
	/**
	 * 设置返回结果
	 * @param resultCode
	 * @param data
	 */
	public void setResult(int resultCode, Intent data) {
		Fragment fragment = getTargetFragment();
		int requestCode = getTargetRequestCode();
		if(fragment != null) {
			fragment.onActivityResult(requestCode, resultCode, data);
			finish();
		}
	}

    /**
     * 弹出fragment堆栈
     * @return
     */
    public void finish() {
    	try {
        	Activity activity = getActivity();
    		if(activity != null) {
    			if(activity instanceof BaseActivity) {
    				((BaseActivity) activity).onBackPressed();
    				return;
    			} 
    		}
    		new Thread() {
    			public void run() {
    				try {
    					Instrumentation inst = new Instrumentation();
    					inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
    				} catch (Exception e) {
    					e.printStackTrace();
    				}
    			}
    		}.start();
    	} catch (IllegalStateException e) {
    		e.printStackTrace();
    	}
    }
	
    protected void showToastMessage(String text) {
    	showToastMessage(text, Toast.LENGTH_SHORT);
	}
    
    protected void showToastMessage(String text, int duration) {
    	DialogUtils.showToastMessage(text, duration);
	}
    
    protected void showToastMessage(int resId) {
    	showToastMessage(resId, Toast.LENGTH_SHORT);
	}
    
    protected void showToastMessage(int resId, int duration) {
    	DialogUtils.showToastMessage(resId, duration);
	}
    
    protected void startActivity(Class<?> cls) {
    	startActivity(new Intent(getActivity(), cls));
    }
    
    protected void startActivity(String action) {
    	startActivity(new Intent(action));
    }
    
    protected void startActivity(Class<?> cls, Intent data) {
    	if(data == null) {
    		return;
    	}
    	data.setClass(getActivity(), cls);
    	startActivity(data);
    }
    
    protected void startActivity(String action, Intent data) {
    	if(data == null) {
    		return;
    	}
    	data.setAction(action);
    	startActivity(data);
    }
    
    protected void startActivityForResult(Class<?> cls, int requestCode) {
    	startActivityForResult(new Intent(getActivity(), cls), requestCode);
    }
    
    protected void startActivityForResult(String action, int requestCode) {
    	startActivityForResult(new Intent(action), requestCode);
    }
    
    protected void startActivityForResult(Class<?> cls, Intent data, int requestCode) {
    	if(data == null) {
    		return;
    	}
    	data.setClass(getActivity(), cls);
    	startActivityForResult(data, requestCode);
    }
    
    protected void startActivityForResult(String action, Intent data, int requestCode) {
    	if(data == null) {
    		return;
    	}
    	data.setAction(action);
    	startActivityForResult(data, requestCode);
    }
	
    public boolean dispatchKeyEvent(KeyEvent event) {
		return false;
	}
	
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
	
    public boolean onKeyUp(int keyCode, KeyEvent event) {
		return false;
	}
	
}
