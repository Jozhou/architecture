package com.architecture.widget.adapterview;

import java.util.ArrayList;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.architecture.widget.layoutview.ILayoutView;

public abstract class LayoutViewAdapter<T> extends android.widget.BaseAdapter {

	protected LayoutInflater mInflater;
	protected ArrayList<T> mDataSource;
	
	public void setDataSource(ArrayList<T> data) {
		mDataSource = data;
	}
	
	public ArrayList<T> getDataSource() {
		return mDataSource;
	}
	
	public LayoutViewAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
	}
	
	// hack android 4.0中的unregisterDataSetObserver报异常
	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		if(observer != null) {
			super.unregisterDataSetObserver(observer);
		}
	}
	
	@Override
	public int getViewTypeCount() {
		return super.getViewTypeCount();
	}
	
	@Override
	public int getItemViewType(int position) {
		T t = mDataSource != null ? mDataSource.get(position) : null;
		if(t != null && t instanceof IListItem)
			return ((IListItem)t).getItemViewType();
		else
			return 0;
	}
	
	@Override
	public int getCount() {
		return mDataSource != null ? mDataSource.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return mDataSource != null ? mDataSource.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		T t = (T) getItem(position);
		ILayoutView<T> view;
		if(convertView != null) {
			view = (ILayoutView<T>) convertView;
		} else {
			int resId = getLayoutResId(position, t, getItemViewType(position));
			if(resId > 0) {
				view = (ILayoutView<T>)mInflater.inflate(resId, null, false);
			} else {
				view = getLayoutView(position, t, getItemViewType(position));
			}
			if(view == null) {
				throw new RuntimeException("LayoutViewAdapter.getView is null, you must override [getLayoutResId] or [getLayoutView]");
			}
		}
		if(view != null) {
			view.setPosition(position, getCount());
			view.setDataSource(t);
			onCreatedLayoutView(position, (View) view, t, getItemViewType(position));
		}
		return (View) view;
	}
	
	protected void onCreatedLayoutView(int position, View view, T t, int itemType) {
		
	}
	
	protected abstract int getLayoutResId(int position, T t, int itemViewType);
	
	protected abstract ILayoutView<T> getLayoutView(int position, T t, int itemViewType);

}
