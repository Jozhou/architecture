package com.architecture.models.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.architecture.R;
import com.architecture.activity.ImageDetailActivity;
import com.architecture.context.Config;
import com.architecture.context.IntentCode;
import com.architecture.models.entry.CadImage;
import com.architecture.models.entry.ProjectInfo;
import com.architecture.widget.imageview.MThumbImageView;
import com.bumptech.glide.Glide;

import java.util.List;

public class CadImageRecyclerAdapter extends RecyclerView.Adapter {
	private Context context;
	private List<CadImage> data;
	private ProjectInfo projectInfo;
	private LayoutInflater mInflater;

	public CadImageRecyclerAdapter(Context context) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}
	
	public void setData(List<CadImage> data, ProjectInfo projectInfo) {
		this.data = data;
		this.projectInfo = projectInfo;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ImageViewHolder(mInflater.inflate(R.layout.item_cad_image,parent,false));
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		((ImageViewHolder)holder).bind(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		if (data != null) {
			return data.size();
		}
		return 0;
	}

	private class ImageViewHolder extends RecyclerView.ViewHolder {

		View rootView;
		public TextView txtName;
		public ImageView imgCad;

		ImageViewHolder(View itemView) {
			super(itemView);
			rootView = itemView;
			imgCad = (MThumbImageView) rootView.findViewById(R.id.img_cad);
			txtName = (TextView) rootView.findViewById(R.id.tv_image_name);
		}

		void bind(final int position){
			final CadImage item = data.get(position);
			txtName.setText(context.getResources().getString(R.string.cad_image_name, item.imageName));
			Glide.with(context).load(Config.getImageUrl() + item.attachimafgeName).dontAnimate()
					.placeholder(R.drawable.placeholder)
					.error(R.drawable.placeholder)
					.into(imgCad);

//			imgCad.setImageUrl(Config.getImageUrl() + item.attachimafgeName);
			imgCad.setClickable(false);
			rootView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(context, ImageDetailActivity.class);
					intent.putExtra(IntentCode.INTENT_IMAGE_POS, position);
					intent.putExtra(IntentCode.INTENT_PROJECT_INFO, projectInfo);
					context.startActivity(intent);
				}
			});
		}

	}


	
}
