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
package com.appfirst.monitoring;

import com.appfirst.utils.VerticalImageTextGroupAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;

/**
 * AFHomeScreen holds a {@link GridView} which displays each of AppFirst's
 * monitoring solutions with an icon and a title.
 * <p>
 * It uses {@link VerticalImageTextGroupAdapter} to render each of the solutions.
 * </p>
 * 
 * @author Bin Liu
 */
public class AFHomeScreen extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		GridView gridview = (GridView) findViewById(R.id.homeview);
		gridview.setAdapter(new VerticalImageTextGroupAdapter(this));

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Toast.makeText(AFHomeScreen.this, "" + position,
						Toast.LENGTH_SHORT).show();
			}
		});
	}
}
