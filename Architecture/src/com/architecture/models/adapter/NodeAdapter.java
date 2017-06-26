package com.architecture.models.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.architecture.R;
import com.architecture.activity.ImageDetailActivity;
import com.architecture.activity.ProjectInfoActivity;
import com.architecture.context.Config;
import com.architecture.context.IntentCode;
import com.architecture.models.api.BaseOperater;
import com.architecture.models.api.GetNodeMgrInfoOperater;
import com.architecture.models.entry.CadImage;
import com.architecture.models.entry.NodeInfo;
import com.architecture.models.entry.ProjectInfo;
import com.architecture.widget.imageview.MThumbImageView;

import java.util.List;

public class NodeAdapter extends BaseAdapter {
	private Context context;
	private List<ProjectInfo> data;

	public NodeAdapter(Context context) {
		this.context = context;
	}
	
	public void setData(List<ProjectInfo> data) {
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
	public View getView(final int position, View convertView, final ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_search_node, null);
			holder.ivNode = (MThumbImageView) convertView.findViewById(R.id.iv_node);
			holder.tvCode = (TextView) convertView.findViewById(R.id.tv_code);
			holder.tvProName = (TextView) convertView.findViewById(R.id.tv_pro_name);
			holder.tvCodeName = (TextView) convertView.findViewById(R.id.tv_code_name);
			convertView.setTag(holder);
			holder.ivNode.setLoadErrResID(R.drawable.placeholder);
			holder.ivNode.setLoadingResID(R.drawable.placeholder);
			holder.ivNode.setClickable(false);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final ProjectInfo item = data.get(position);
		holder.ivNode.setImageUrl(Config.getImageUrl() + item.imageUrl);
		holder.tvCode.setText(context.getResources().getString(R.string.node_code, item.nodeCode));
		holder.tvProName.setText(context.getResources().getString(R.string.pro_part_name, item.projectName));
		holder.tvCodeName.setText(context.getResources().getString(R.string.node_name, item.nodeNames));
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getCodeInfo(item.id);
			}
		});
		return convertView;
	}

	private void getCodeInfo(String id) {
		final GetNodeMgrInfoOperater operater = new GetNodeMgrInfoOperater(context);
		operater.setParams(id);
		operater.onReq(new BaseOperater.RspListener() {
			@Override
			public void onRsp(boolean success, Object obj) {
				if (success) {
					ProjectInfo projectInfo = operater.getProjectInfo();
					if (projectInfo != null && !TextUtils.isEmpty(projectInfo.id)) {
						Intent intent = new Intent(context, ProjectInfoActivity.class);
						intent.putExtra(IntentCode.INTENT_PROJECT_INFO, projectInfo);
						context.startActivity(intent);
					}
				}
			}
		});
	}
	
	public class ViewHolder {
		public MThumbImageView ivNode;
		public TextView tvCode;
		public TextView tvProName;
		public TextView tvCodeName;
	}
	
}
