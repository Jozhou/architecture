package com.architecture.manager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.architecture.application.App;

import android.app.Activity;
import android.text.TextUtils;

public class ActivityManager {

    private static final byte[] mLock = new byte[0];
	private static ActivityManager mInstance = null;
	public final static ActivityManager get() {
        synchronized (mLock) {
            if (mInstance == null) {
                mInstance = new ActivityManager();
            }
            return mInstance;
        }
    }
	
	private ArrayList<WeakReference<Activity>> activityStack; 
    
    private ActivityManager() {
    	activityStack = new ArrayList<WeakReference<Activity>>();
    }
    
    /**
     * 删除指定acitivity
     */
    public synchronized void removeActivity(String activityName) {
    	if (TextUtils.isEmpty(activityName)) {
    		return;
    	}
    	for(int i = 0; i < activityStack.size(); i++){
    		WeakReference<Activity> reference = activityStack.get(i);
    		if(reference != null && reference.get() != null && 
    			activityName.equals(reference.get().getClass().getName())){
    			activityStack.remove(i).get().finish();
    		}
    	}
    }
    
    /**
     * 当前activity name（包括包名）
     * @return
     */
    public synchronized String currentActivityName() {
    	Activity activity = currentActivity();
		return activity != null ? activity.getClass().getName() : "";
    }
    
    /**
     * 当前activity
     * @return
     */
    public synchronized Activity currentActivity() { 
    	Activity activity = null; 
    	if(activityStack.size() > 0) {
    		WeakReference<Activity> reference= null;
    		for (int i = activityStack.size() - 1; i >= 0; i--) {
    			reference = activityStack.get(i);
	    		if(reference != null) {
	    			activity = reference.get();
	    			if (activity != null && !activity.isFinishing()) {
	    				return activity;
	    			}
	    		}
    		}
    	}
        return null;
    }
    
    /**
     * 当前activity是否为顶部activity
     * @return
     */
    public synchronized boolean isCurrentActivity(Activity activity) { 
        return activity == currentActivity(); 
    }
    
    /**
     * 底部activity
     * @return
     */
    public synchronized Activity bottomActivity() { 
    	Activity activity = null; 
    	if(activityStack.size() > 0) {
    		WeakReference<Activity> reference= null;
    		for (int i = 0; i < activityStack.size(); i++) {
    			reference = activityStack.get(i);
	    		if(reference != null) {
	    			activity = reference.get();
	    			if (activity != null && !activity.isFinishing()) {
	    				return activity;
	    			}
	    		}
    		}
    	}
        return null;
    }
    
    /**
     * 加入activity
     * @param activity
     */
    public synchronized void putActivity(Activity activity) { 
        if (activityStack == null || activity == null) {
        	return;
        }
        for(int i = activityStack.size() - 1; i >= 0; i--) {
    		WeakReference<Activity> reference= activityStack.get(i);
    		if(reference != null) {
    			Activity context = reference.get();
        		if(context != null && context.equals(activity)) {
        			return;
        		}
    		}
    	}
        activityStack.add(new WeakReference<Activity>(activity)); 
    } 
    
    /**
     * 弹出activity
     * @param activity
     */
    public synchronized void popupActivity(Activity activity) { 
    	popupActivity(activity, true, false);
    } 
    
    private void popupActivity(Activity activity, boolean callgc, boolean finish) { 
        if (activityStack == null || activity == null) {
        	return;
        }
        if (activity != null) { 
        	if(finish) {
        		activity.finish(); 
        	}
        	for(int i = activityStack.size() - 1; i >= 0; i--) {
        		WeakReference<Activity> reference= activityStack.get(i);
        		if(reference != null) {
        			Activity context = reference.get();
            		if(context != null && context.equals(activity)) {
            			activityStack.remove(i);
            			break;
            		}
        		}
        	}
            activity = null; 
        }
        if(callgc) {
        	if(activityStack.size() < 1)
        		android.os.Process.killProcess(android.os.Process.myPid());
        	System.gc();
        }
    } 
    
    /**
     * 弹出当前activity以前的所有堆栈
     */
    public void popupAllActivityExclusiveCurrent() { 
        if (activityStack == null || activityStack.size() <= 1) {
        	return;
        }
    	for(int i = activityStack.size() - 2; i >= 0; i--) {
    		WeakReference<Activity> reference= activityStack.get(i);
    		if(reference != null) {
    			Activity context = reference.get();
        		if(context != null) {
       				activityStack.remove(i);
    				context.finish();
        		}
    		}
    	}
    	if(activityStack.size() < 1)
    		android.os.Process.killProcess(android.os.Process.myPid());
        System.gc();
    }
    
    /**
     * 弹出所有堆栈
     */
    public void popupAllActivity() { 
        if (activityStack != null) {
	    	for(int i = activityStack.size() - 1; i >= 0; i--) {
	    		WeakReference<Activity> reference= activityStack.get(i);
	    		if(reference != null) {
	    			Activity context = reference.get();
	        		if(context != null) {
	       				activityStack.remove(i);
	    				context.finish();
	        		}
	    		}
	    	}
        }
        App.getInstance().exit();
    	android.os.Process.killProcess(android.os.Process.myPid());
        System.gc();
    }
    
}