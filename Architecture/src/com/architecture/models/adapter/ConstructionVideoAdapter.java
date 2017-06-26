package com.architecture.models.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.architecture.R;
import com.architecture.activity.ConstructionVideoDetailActivity3;
import com.architecture.context.Config;
import com.architecture.context.IntentCode;
import com.architecture.models.entry.ConstructionVideo;
import com.architecture.models.entry.ProjectInfo;
import com.architecture.widget.grid.StaggeredGridView;
import com.architecture.widget.imageview.MThumbImageView;

import java.util.List;

public class ConstructionVideoAdapter extends BaseAdapter {
	private Context context;
	private List<ConstructionVideo> data;
	private ProjectInfo projectInfo;
	private int videoType;
	private StaggeredGridView gv;
	
	public ConstructionVideoAdapter(Context context, StaggeredGridView gv) {
		this.context = context;
		this.gv = gv;
	}
	
	public void setData(List<ConstructionVideo> data, ProjectInfo projectInfo, int videoType) {
		this.data = data;
		this.projectInfo = projectInfo;
		this.videoType = videoType;
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
			convertView = View.inflate(context, R.layout.item_construction_video, null);
			holder.imgVideo = (MThumbImageView) convertView.findViewById(R.id.img_video);
			holder.txtTitle = (TextView) convertView.findViewById(R.id.tv_video_title);
			holder.txtAuthor = (TextView) convertView.findViewById(R.id.tv_video_author);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final ConstructionVideo item = data.get(position);
		holder.txtTitle.setText(context.getResources().getString(R.string.video_title, item.videoName));
		holder.txtAuthor.setText(context.getResources().getString(R.string.video_author, item.videoType));
		holder.imgVideo.setLoadingResID(R.drawable.placeholder);
		holder.imgVideo.setLoadErrResID(R.drawable.placeholder);
		holder.imgVideo.setImageUrl(Config.getImageUrl() + item.attachcoverimgName);
		holder.imgVideo.setClickable(false);
		// 绑定tag
		holder.txtTitle.setTag(position);
		//  绑定当前的item，也就是convertview
		holder.txtAuthor.setTag(convertView);
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(context, ConstructionVideoDetailActivity3.class);
				intent.putExtra(IntentCode.INTENT_PROJECT_INFO, projectInfo);
				intent.putExtra(IntentCode.INTENT_CONSTRUCTION_VIDEO, item);
				intent.putExtra(IntentCode.INTENT_VIDEO_TYPE, videoType);
				context.startActivity(intent);
			}
		});
		return convertView;
	}
	
	public class ViewHolder {
		public TextView txtTitle;
		public TextView txtAuthor;
		public MThumbImageView imgVideo;

//		public void update() {
//			// 精确计算GridView的item高度
//			txtTitle.getViewTreeObserver().addOnGlobalLayoutListener(
//					new ViewTreeObserver.OnGlobalLayoutListener() {
//						public void onGlobalLayout() {
//							int position = (Integer) txtTitle.getTag();
//							// 这里是保证同一行的item高度是相同的！！也就是同一行是齐整的 height相等
//							if (position > 0 && position % 2 == 1) {
//								View v = (View) txtAuthor.getTag();
//								int height = v.getHeight();
//
//								View view = gv.getChildAt(position - 1);
//								int lastheight = view.getHeight();
//								// 得到同一行的最后一个item和前一个item想比较，把谁的height大，就把两者中                                                                // height小的item的高度设定为height较大的item的高度一致，也就是保证同一                                                                 // 行高度相等即可
//								if (height > lastheight) {
//									view.setLayoutParams(new GridView.LayoutParams(
//											GridView.LayoutParams.FILL_PARENT,
//											height));
//								} else if (height < lastheight) {
//									v.setLayoutParams(new GridView.LayoutParams(
//											GridView.LayoutParams.FILL_PARENT,
//											lastheight));
//								}
//							}
//						}
//					});
//		}
	}
	
}
