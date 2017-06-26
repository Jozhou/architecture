package com.architecture.activity.base;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;


import com.architecture.R;
import com.architecture.fragment.base.BaseDialogFragment;
import com.architecture.fragment.base.BaseFragment;
import com.architecture.manager.ActivityManager;
import com.architecture.models.api.IOperater;
import com.architecture.utils.DialogUtils;
import com.architecture.utils.LogcatUtils;
import com.architecture.utils.OSUtils;
import com.architecture.utils.ViewInjectUtils;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class BaseActivity extends FragmentActivity implements android.view.View.OnClickListener {

	private static final String TAG = "ActivityStack";
	
	protected Context context;
	protected boolean mInitialized;
	protected FragmentManager manager;
	
	protected RelativeLayout rootLayout;
	protected FrameLayout fragmentContainerLayout;
	protected FrameLayout loadingContainerLayout;
	protected ArrayList<WeakReference<IOperater>> operaters;

	/**
	 * 是否开启硬件加速
	 * @return
	 */
	protected boolean enabledHardwareAccelerated() {
		return true;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().getDecorView().setBackgroundResource(0);
		LogcatUtils.i(TAG, "onCreate " + this.getClass().getName());
		ActivityManager.get().putActivity(this);
		context = this;
		manager = getSupportFragmentManager();
		// 取得参数
		onQueryArguments(getIntent());
	}
	
	@Override
	public void setContentView(int layoutResID) {
		View view = LayoutInflater.from(this).inflate(layoutResID, null);
		super.setContentView(generateContentView(view));
		initContentView();
	}
	
	@Override
	public void setContentView(View view) {
		super.setContentView(generateContentView(view));
		initContentView();
	}
	
	@Override
	public void setContentView(View view, LayoutParams params) {
		super.setContentView(generateContentView(view), params);
		initContentView();
	}
	
	private View generateContentView(View view) {
		rootLayout = new RelativeLayout(getBaseContext());
		rootLayout.addView(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		return rootLayout;
	}
	
	/**
	 * 初始化Fragment的container
	 */
	private void initFragmentContainer() {
		if(fragmentContainerLayout == null) {
			fragmentContainerLayout = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.fragment_container, null);
			int index = rootLayout.getChildCount();
			if(index > 0 && rootLayout.getChildAt(index - 1).equals(loadingContainerLayout)) {
				index--;
			}
			rootLayout.addView(fragmentContainerLayout, index, 
					new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}
	}
	
	/**
	 * 初始化等待进度条
	 */
	private void initLoadingView() {
		if(loadingContainerLayout == null) {
			loadingContainerLayout = new FrameLayout(this);
			rootLayout.addView(loadingContainerLayout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		}
	}
	
	private synchronized void initContentView() {
		if (mInitialized) {
			return;
		}
		onHardwareAccelerated();
		onInjectView();
		onFindView();
		onBindListener();
		onApplyData();
		mInitialized = true;
	}

	/**
	 * 开启硬件加速
	 */
	@SuppressLint("InlinedApi")
	protected void onHardwareAccelerated() {
		if(enabledHardwareAccelerated()) {
			if (OSUtils.hasHoneycomb()) {
				getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, 
					WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
			}
		}
	}
	

	/**
	 * 取得传递的参数
	 */
	protected void onQueryArguments(Intent intent) {
		
	}
	
	/**
	 * 初始化控件、获取内部控件（注释类遍历）
	 */
	protected void onInjectView() {
		ViewInjectUtils.onInjectView(this);
	}
	
	/**
	 * 初始化控件、获取内部控件
	 */
	protected void onFindView() {
		
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
    	startActivity(new Intent(this, cls));
    }
    
    protected void startActivity(String action) {
    	startActivity(new Intent(action));
    }
    
    protected void startActivity(Class<?> cls, Intent data) {
    	if(data == null) {
    		return;
    	}
    	data.setClass(this, cls);
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
    	startActivityForResult(new Intent(this, cls), requestCode);
    }
    
    protected void startActivityForResult(String action, int requestCode) {
    	startActivityForResult(new Intent(action), requestCode);
    }
    
    protected void startActivityForResult(Class<?> cls, Intent data, int requestCode) {
    	if(data == null) {
    		return;
    	}
    	data.setClass(this, cls);
    	startActivityForResult(data, requestCode);
    }
    
    protected void startActivityForResult(String action, Intent data, int requestCode) {
    	if(data == null) {
    		return;
    	}
    	data.setAction(action);
    	startActivityForResult(data, requestCode);
    }
    
    /**
     * 统一管理operater
     * @param operater
     */
    public void addOperater(IOperater operater) {
    	if (operaters == null) {
    		operaters = new ArrayList<WeakReference<IOperater>>();
    	}
    	if (operater != null) {
    		operaters.add(new WeakReference<IOperater>(operater));
    	}
    }
	
	public <T extends Fragment> T addFragment(Class<T> cls, String tag) {
		return addFragment(cls, tag, R.id.frame_container);
	}
	
	public <T extends Fragment> T addFragment(Class<T> cls, String tag, int containerID) {
		return addFragment(cls, tag, containerID, true);
	}
	
	public <T extends Fragment> T addFragment(Class<T> cls, String tag, int containerID, boolean addToBackStack) {
		if(Looper.getMainLooper() == Looper.myLooper()) {
			return _addFragment(cls, tag, containerID, addToBackStack);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends Fragment> T _addFragment(Class<T> cls, String tag, int containerID, boolean addToBackStack) {
		if (this.isFinishing()) {
			return null;
		}
		initFragmentContainer();
		Fragment fragment = null;
		Fragment old = manager.findFragmentByTag(tag);
		FragmentTransaction ft = manager.beginTransaction();
		ft.setCustomAnimations(R.anim.bottom_in, R.anim.bottom_out);
		if (old != null) {
			ft.remove(old).detach(old);
		}
		try {
			fragment = cls.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		if (fragment == null) {
			showToastMessage("fragment is null");
			return null;
		}
		ft.add(containerID, fragment, tag);
		if (addToBackStack) {
			ft.addToBackStack(null);
		}
		ft.commitAllowingStateLoss();
		return (T) fragment;
	}

	public <T extends Fragment> T replaceFragment(Class<T> cls, String tag) {
		return replaceFragment(cls, tag, R.id.frame_container);
	}

	@SuppressWarnings("unchecked")
	public <T extends Fragment> T replaceFragment(Class<T> cls, String tag, int containerId) {
		if (this.isFinishing()) {
			return null;
		}
		initFragmentContainer();
		Fragment fragment = null;
		FragmentTransaction ft = manager.beginTransaction();
		Fragment page = manager.findFragmentByTag(tag);
		if (page != null) {
			fragment = page;
		} else {
			try {
				fragment = cls.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if (fragment == null) {
				showToastMessage("fragment is null");
				return null;
			}
		}
		ft.replace(containerId, fragment, tag);
		ft.commitAllowingStateLoss();
		return (T) fragment;
	}

	public void closeFragment(final String tag) {
		if (this.isFinishing()) {
			return;
		}
		FragmentTransaction ft = manager.beginTransaction();
		ft.setCustomAnimations(R.anim.bottom_in, R.anim.bottom_out);
		Fragment prev = manager.findFragmentByTag(tag);
		if (prev != null) {
			ft.remove(prev).detach(prev).commitAllowingStateLoss();
		}
	}
	
	private void hackFragmentState() {
		try {
			Field mStateSaved = manager.getClass().getDeclaredField("mStateSaved");
			mStateSaved.setAccessible(true);
			mStateSaved.set(manager, Boolean.valueOf(false));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
    /**
     * 模拟按下back键
     */
    public void performBackKeyClicked() {
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
    }
	
    /**
     * 弹出fragment堆栈
     * @return
     */
	public boolean popupBackStack() {
		try {
	        if (manager.getBackStackEntryCount() > 0) {
	        	hackFragmentState();
	            manager.popBackStackImmediate();
	            return true;
	        }
		} catch (IllegalStateException e) {
			
		}
		return false;
	}
	
	/**
	 * 获取当前正在显示的fragment
	 * @return
	 */
	public Fragment getCurrentFragment() {
		if(manager.getFragments() != null && manager.getFragments().size() > 0) {
			int index = manager.getFragments().size() - 1;
			Fragment fragment = null;
			while(index >= 0) {
				fragment = manager.getFragments().get(index);
				if(fragment != null && fragment.isVisible())
					return fragment;
				index--;
			}
		}
		return null;
	}

	/**
	 * 显示等待进度条
	 * @param show
	 */
	public void showLoading(final boolean show) {
		initLoadingView();
		if(loadingContainerLayout != null) {
			loadingContainerLayout.removeAllViews();
			if(show) {
				try {
					loadingContainerLayout.addView(LayoutInflater.from(this).inflate(R.layout.view_activity_loading, null), 
							LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		LogcatUtils.i(TAG, "onResume " + this.getClass().getName());
		MobclickAgent.onResume(context);
	}

	@Override
	protected void onPause() {
		super.onPause();
		LogcatUtils.i(TAG, "onPause " + this.getClass().getName());
		MobclickAgent.onPause(context);
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		Fragment fragment = getCurrentFragment();
		if(fragment != null) {
			if(fragment instanceof BaseFragment) {
				if(((BaseFragment) fragment).dispatchKeyEvent(event)) {
					return true;
				}
			}
			if(fragment instanceof BaseDialogFragment) {
				if(((BaseDialogFragment) fragment).dispatchKeyEvent(event)) {
					return true;
				}
			}
		}
		return super.dispatchKeyEvent(event);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Fragment fragment = getCurrentFragment();
		if(fragment != null) {
			if(fragment instanceof BaseFragment) {
				if(((BaseFragment) fragment).onKeyDown(keyCode, event)) {
					return true;
				}
			}
			if(fragment instanceof BaseDialogFragment) {
				if(((BaseDialogFragment) fragment).onKeyDown(keyCode, event)) {
					return true;
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		Fragment fragment = getCurrentFragment();
		if(fragment != null) {
			if(fragment instanceof BaseFragment) {
				if(((BaseFragment) fragment).onKeyUp(keyCode, event)) {
					return true;
				}
			}
			if(fragment instanceof BaseDialogFragment) {
				if(((BaseDialogFragment) fragment).onKeyUp(keyCode, event)) {
					return true;
				}
			}
		}
		return super.onKeyUp(keyCode, event);
	}
	
	@Override
	public void onBackPressed() {
		try {
			super.onBackPressed();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		cancelAllOperators();
		ActivityManager.get().popupActivity(this);
		// 取消音量键设置
		setVolumeControlStream(AudioManager.STREAM_RING);
		// 取消屏幕常亮
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onDestroy();
		LogcatUtils.i(TAG, "onDestroy " + this.getClass().getName());
	}
	
	/**
	 * 取消http请求
	 */
	protected void cancelAllOperators() {
		if (operaters != null) {
			IOperater o = null;
			for (WeakReference<IOperater> wk : operaters) {
				if (wk != null) {
					o = wk.get();
					if (o != null && o.isAutoClose()) {
						o.cancel();
					}
				}
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		
	}

}
