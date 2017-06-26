package com.architecture.utils;

import java.lang.reflect.Field;

import com.architecture.application.App;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

public class ViewInjectUtils {

	/**
	 * 注入
	 * @param obj
	 */
	public static void onInjectView(Object obj) {
		Class<?> clazz = obj.getClass();
		_onInjectView(clazz, obj);
	}
	
	private static void _onInjectView(Class<?> clazz, Object obj) { 
		try {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				try {
					if (field.isAnnotationPresent(ViewInject.class)) {
						ViewInject inject = field.getAnnotation(ViewInject.class);
						View view = null;
						String name = inject.value();
						if(TextUtils.isEmpty(name)) {
							name = field.getName();
						}
						Context context = App.getInstance();
						int id = context.getResources().getIdentifier(name, "id", context.getPackageName());
						if (id != 0) {
							field.setAccessible(true);
							if(obj instanceof Activity) {
								field.set(obj, ((Activity) obj).findViewById(id));
							} else if(obj instanceof View) {
								field.set(obj, ((View) obj).findViewById(id));
							} else if(obj instanceof Fragment) {
								if(((Fragment) obj).getView() == null) {
									continue;
								}
								field.set(obj, ((Fragment) obj).getView().findViewById(id));
							}
							if(field.get(obj) instanceof View) { 
								view = (View) field.get(obj);
							}
						}
						boolean clickListener = inject.setClickListener();
						if(clickListener) {
							if(view != null && obj instanceof OnClickListener) {
								view.setOnClickListener((OnClickListener) obj);
							}
						}
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
			Class<?> superclazz = clazz.getSuperclass();
			String superclazzname = superclazz.getName().toLowerCase();
			if(superclazzname.startsWith("java") || superclazzname.startsWith("android")) {
				return;
			}
			_onInjectView(superclazz, obj);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
}
