package com.debla.hashpet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.debla.hashpet.Model.UserImgs;
import com.debla.hashpet.R;
import com.debla.hashpet.Utils.ImageLoader;

import java.util.List;

public class PetCircleGridAdapter extends BaseAdapter {
	private List<UserImgs> mUI;

	private LayoutInflater mLayoutInflater;
	private Context context;

	public PetCircleGridAdapter(List<UserImgs> ui, Context context) {
		mLayoutInflater = LayoutInflater.from(context);
	    this.mUI = ui;
		this.context=context;
	}

	@Override
	public int getCount() {
		return mUI == null ? 0 : mUI.size();
	}

	@Override
	public String getItem(int position) {
		return mUI.get(position).urls;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyGridViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new MyGridViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.user_img_item,
					parent, false);
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.iv_user_img);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (MyGridViewHolder) convertView.getTag();
		}
		String url = getItem(position);
		ImageLoader imageLoader = new ImageLoader(context);
		imageLoader.setDefaultImage(R.drawable.img_error);
		imageLoader.DisplayImage(url,viewHolder.imageView);
		//ImageLoader.getInstance().displayImage(url, viewHolder.imageView);
		return convertView;
	}

	private static class MyGridViewHolder {
		ImageView imageView;
	}
}
