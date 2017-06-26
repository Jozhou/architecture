package com.architecture.models.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.architecture.R;
import com.architecture.activity.ImageDetailActivity;
import com.architecture.context.Config;
import com.architecture.context.IntentCode;
import com.architecture.models.entry.CadImage;
import com.architecture.models.entry.ProjectInfo;
import com.architecture.utils.DialogUtils;
import com.architecture.utils.ViewInject;
import com.architecture.widget.imageview.MThumbImageView;

import java.util.ArrayList;
import java.util.List;

public class CadImageAdapter extends BaseAdapter {
	private Context context;
	private List<CadImage> data;
	private ProjectInfo projectInfo;
	
	public CadImageAdapter(Context context) {
		this.context = context;
	}
	
	public void setData(List<CadImage> data, ProjectInfo projectInfo) {
		this.data = data;
		this.projectInfo = projectInfo;
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
			convertView = View.inflate(context, R.layout.item_cad_image, null);
			holder.imgCad = (MThumbImageView) convertView.findViewById(R.id.img_cad);
			holder.txtName = (TextView) convertView.findViewById(R.id.tv_image_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final CadImage item = data.get(position);
		holder.txtName.setText(context.getResources().getString(R.string.cad_image_name, item.imageName));
		holder.imgCad.setLoadingResID(R.drawable.placeholder);
		holder.imgCad.setLoadErrResID(R.drawable.placeholder);
		holder.imgCad.setImageUrl(Config.getImageUrl() + item.attachimafgeName);
		holder.imgCad.setClickable(false);
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(context, ImageDetailActivity.class);
				intent.putExtra(IntentCode.INTENT_IMAGE_POS, position);
				intent.putExtra(IntentCode.INTENT_PROJECT_INFO, projectInfo);
				context.startActivity(intent);
			}
		});
		return convertView;
	}
	
	public class ViewHolder {
		public TextView txtName;
		public MThumbImageView imgCad;
	}
	
}
