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
package com.appfirst.activities.lists;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.appfirst.monitoring2.R;
import com.appfirst.monitoring2.AFHomeScreen;
import com.appfirst.types.BaseObject;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.ListView;
import android.widget.TextView;

/**
 * AFListActivity is the base class for other list activities.
 * <p>
 * It create the option menus for server usage. <br>
 * 1. navigating through the current view and the home view. <br>
 * 2. provides the filter for sorting current list by different resources. <br>
 * 3. provides the button for refreshing.
 * </p>
 * 
 * @author Bin Liu
 * 
 */
public abstract class AFListActivity extends ListActivity {
	public abstract void sortListItems();

	/**
	 * This method should be called every time the search filter has been
	 * changed or sort field has been changed. It needs to be implemented for
	 * different activities.
	 */
	public abstract void displayList();

	/**
	 * Load the data list for the view, it should be called every time the view
	 * is initialized, in onCreate method.
	 */
	public abstract void loadResource();

	static final int PROGRESS_DIALOG = 0;

	protected TextView mTitle;
	protected TextView mSortText;
	protected String mSortName;
	protected ListView mListView;
	protected String filterString = "";
	
	protected void setCurrentView() {
		setContentView(R.layout.searchable_list);
		mTitle = (TextView) findViewById(R.id.searchable_title);
		mSortText = (TextView) findViewById(R.id.searchable_sort);
		mListView = (ListView) findViewById(android.R.id.list);
		handleIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	protected void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			filterString = query;
			displayList();
		}
	}
	
	protected String getSortAndFilter() {
		String sortPart = String.format("sorted by %s",  mSortName);
		String ret = sortPart;
		if (filterString != "") {
			ret += String.format(", filtered by %s", filterString);
		}
		return ret;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "Dashboard")
				.setIcon(android.R.drawable.ic_menu_today);
		menu.add(0, 1, 0, "Refresh").setIcon(android.R.drawable.ic_menu_rotate);
		menu.add(0, 1, 0, "Search").setIcon(
				android.R.drawable.ic_search_category_default);
		SubMenu sortMenuItem = menu.addSubMenu(0, 1, 0, "Sort").setIcon(
				android.R.drawable.ic_menu_sort_alphabetically);
		List<String> options = getSortOptions();
		for (int cnt = 0; cnt < options.size(); cnt++) {
			sortMenuItem.add(1, cnt, 0, options.get(cnt));
		}
		sortMenuItem.setGroupCheckable(1, true, true);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String fieldName = item.toString();
		Log.i(TAG, String.format("Field change to: %s", fieldName));
		if (item.toString() == "Dashboard") {
			finish();
			Intent i = new Intent(this, AFHomeScreen.class);
			startActivity(i);
		} else if (item.toString() == "Refresh") {
			showDialog(PROGRESS_DIALOG);
			new ResourceLoader().execute();
		} else if (item.toString() == "Sort") {
			sortField = null;
		} else if (item.toString() == "Search") {
			return super.onSearchRequested();
			// TODO: handle search
		} else {
			if (item.isChecked()) {
				item.setChecked(false);
			} else {
				item.setChecked(true);
			}
			try {
				sortField = objectClass.getDeclaredField(item.toString());
				sortField.setAccessible(true);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (sortField != null) {
				
				sortListItems();
			}
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * List all the fields that are sortable. Including Double, Integer, String,
	 * Boolean and Long and these are the types being used in resources fields.
	 * 
	 * @return list of sortable field's name.
	 */
	protected List<String> getSortOptions() {
		List<String> options = new ArrayList<String>();
		Field fieldlist[] = objectClass.getDeclaredFields();
		for (int i = 0; i < fieldlist.length; i++) {
			Field fld = fieldlist[i];
			Class cls = fld.getType();
			if (cls == Double.class || cls == String.class || cls == int.class
					|| cls == Long.class
					|| cls == Boolean.class) {
				options.add(fld.getName());
			}
		}
		return options;
	}

	@SuppressWarnings("unchecked")
	private Class objectClass = BaseObject.class;

	@SuppressWarnings("unchecked")
	public Class getObjectClass() {
		return objectClass;
	}

	@SuppressWarnings("unchecked")
	public void setObjectClass(Class objectClass) {
		this.objectClass = objectClass;
	}

	private String TAG = "AFListActivity";
	protected Field sortField;

	protected class ResourceLoader extends AsyncTask<URL, Integer, Long> {
		protected Long doInBackground(URL... urls) {
			loadResource();
			return 0L;
		}

		protected void onPostExecute(Long result) {
			displayList();
		}
	}

	protected ProgressDialog progressDialog;

	/**
	 * Create the alert box using for showing progress.
	 * 
	 * @return dialog box showing progress.
	 */
	protected ProgressDialog createProcessDialog() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Loading...");
		return progressDialog;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case PROGRESS_DIALOG:
			progressDialog.setProgress(0);
		}
	}

	/**
	 * 
	 */
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case PROGRESS_DIALOG:
			dialog = createProcessDialog();
			break;
		default:
			break;
		}

		return dialog;
	}

}
