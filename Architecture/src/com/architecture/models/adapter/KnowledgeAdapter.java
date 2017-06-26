package com.architecture.models.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.architecture.R;
import com.architecture.models.entry.Knowledge;

import java.util.List;

public class KnowledgeAdapter extends BaseAdapter {
	private Context context;
	private List<Knowledge> data;
	
	public KnowledgeAdapter(Context context) {
		this.context = context;
	}
	
	public void setData(List<Knowledge> data) {
		this.data = data;
	}

	@Override
	public int getCount() {
		if (data != null) {
			return data.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_knowledge, null);
			holder.txtContent = (TextView) convertView.findViewById(R.id.tv_content);
			holder.txtName = (TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final Knowledge item = data.get(position);
		holder.txtName.setText(item.name);
		holder.txtContent.setText(item.content);
		
		return convertView;
	}
	
	public class ViewHolder {
		public TextView txtName;
		public TextView txtContent;
	}
	
}
