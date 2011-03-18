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

import android.app.ListActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

/**
 * AFListActivity is the base class for other list activities. 
 * <p>
 * It create the
 * option menus for serverl usage. <br>
 * 1. navigating through the current view and the home view. <br>
 * 2. provides the filter for sorting current list by different resources. <br>
 * 3. provides the button for refreshing.
 * </p>
 * 
 * @author Bin Liu
 * 
 */
public abstract class AFListActivity extends ListActivity {
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "Dashboard").setIcon(
				android.R.drawable.ic_menu_upload);
		menu.add(0, 1, 0, "Refresh").setIcon(android.R.drawable.ic_menu_upload);
		SubMenu sortMenuItem = menu.addSubMenu(0, 1, 0, "sort");
		sortMenuItem.add(1, 0, 0, "cpu").setCheckable(true);
		sortMenuItem.add(1, 1, 0, "mem").setCheckable(true);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
