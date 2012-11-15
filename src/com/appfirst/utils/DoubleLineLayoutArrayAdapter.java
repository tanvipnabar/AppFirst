/*
 * Copyright 2009-2011 AppFirst, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.appfirst.utils;

import java.util.List;

import com.appfirst.activities.details.AFApplicationDetail;
import com.appfirst.activities.details.AFMinuteDetail;
import com.appfirst.activities.details.AFProcessDetail;
import com.appfirst.activities.lists.AFApplicationList;
import com.appfirst.monitoring2.R;
import com.appfirst.types.Application;

import android.app.Activity;
import android.content.Intent;
import android.opengl.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author Bin Liu
 * 
 */
public class DoubleLineLayoutArrayAdapter extends ArrayAdapter<String>
		implements OnItemClickListener {
	protected static final String TAG = "DoubleLineLayoutArrayAdapter";
	private LayoutInflater mInflator;
	private List<String> mValues1;
	private List<String> mValues2;
	private List<Integer> mImageResources;
	private List<Integer> mIds;
	private Class mClass;
	private Activity mContext;

	static class ViewHolder {
		public ImageView image;
		public TextView line1;
		public TextView line2;
		public TextView id;
		public TextView position;
	}

	public DoubleLineLayoutArrayAdapter(Activity context, List<String> values,
			List<String> values2, List<Integer> ids, Class cls) {
		super(context, R.id.TextView01, values);
		mInflator = context.getLayoutInflater();
		mValues1 = values;
		mValues2 = values2;
		mIds = ids;
		mContext = context;
		mClass = cls;
	}

	public DoubleLineLayoutArrayAdapter(Activity context, List<String> values,
			List<String> values2, List<Integer> images, List<Integer> ids,
			Class cls) {
		super(context, R.id.TextView01, values);
		mInflator = context.getLayoutInflater();
		mValues1 = values;
		mValues2 = values2;
		mImageResources = images;
		mIds = ids;
		mContext = context;
		mClass = cls;
	}

	public DoubleLineLayoutArrayAdapter(Activity context, List<String> values,
			List<String> values2) {
		super(context, R.id.TextView01, values);
		mInflator = context.getLayoutInflater();
		mValues1 = values;
		mValues2 = values2;
		mContext = context;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return (position % 2 == 0) ? 0 : 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		ViewHolder viewHolder;
		if (rowView == null) {
			if (getItemViewType(position) == 0) {
				rowView = mInflator.inflate(R.layout.double_line, null);
			} else {
				rowView = mInflator.inflate(R.layout.double_line, null);
			}
			viewHolder = new ViewHolder();
			viewHolder.line1 = (TextView) rowView
					.findViewById(R.id.double_line_line1);
			viewHolder.line2 = (TextView) rowView
					.findViewById(R.id.double_line_line2);
			viewHolder.id = (TextView) rowView
					.findViewById(R.id.double_line_id);
			viewHolder.position = (TextView) rowView
					.findViewById(R.id.double_line_position);
			viewHolder.image = (ImageView) rowView
					.findViewById(R.id.double_line_image);
			rowView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) rowView.getTag();
		}
		rowView.setClickable(true);
		rowView.setFocusable(true);
		viewHolder.line1.setText(mValues1.get(position));
		viewHolder.line2.setText(mValues2.get(position));

		if (mImageResources != null) {
			viewHolder.image.setVisibility(View.VISIBLE);
			viewHolder.image.setBackgroundDrawable(mContext.getResources()
					.getDrawable(mImageResources.get(position)));
		} else {
			viewHolder.image.setVisibility(View.GONE);
		}

		if (mIds != null) {
			rowView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ViewHolder viewHolder = (ViewHolder) v.getTag();
					Log.v(TAG, viewHolder.id.getText().toString());
					Intent intent = new Intent(mContext, mClass);
					intent.putExtra(mClass.getName() + ".id", viewHolder.id
							.getText().toString());
					intent
							.putExtra(mClass.getName() + ".selected", Integer
									.parseInt(viewHolder.position.getText()
											.toString()));
					mContext.startActivity(intent);
				}
			});
			viewHolder.id.setText(String.format("%d", mIds.get(position)));
			viewHolder.position.setText(String.format("%d", position));
		} else {
			rowView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ViewHolder viewHolder = (ViewHolder) v.getTag();
					String text = viewHolder.line1.getText() + "\n" + viewHolder.line2.getText();
					Toast.makeText(mContext, text, 1000000).show();
				}
			});
		}

		return rowView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
	}
}
