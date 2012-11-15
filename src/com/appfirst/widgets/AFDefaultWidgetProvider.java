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
package com.appfirst.widgets;

import com.appfirst.activities.lists.AFAlertList;
import com.appfirst.activities.lists.AFServerList;
import com.appfirst.communication.Helper;
import com.appfirst.monitoring2.R;
import com.appfirst.monitoring2.LoginScreen;
import com.appfirst.monitoring2.MainApplication;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * @author Bin Liu
 * 
 */
public class AFDefaultWidgetProvider extends AppWidgetProvider {
	public class WidgetUpdateActivity extends Activity {

		public WidgetUpdateActivity() {
			super();
		}

	}

	public static final String TAG = "AFDefaultWidgetProvider";

	@Override
	public void onReceive(Context context, Intent intent)

	{
		super.onReceive(context, intent);

		if (intent.getAction().equals("com.google.app.myapp.CLICK")) {
			// Write here your button click code
			Toast.makeText(context, "It works!!", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onEnabled(Context context) {
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		// Loop through all widgets to display an update
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.widget_layout);
			setUpdateIntent(context, views);
			setHomeScreenIntent(context, views);
			setServerViewIntent(context, views);
			setAlertViewIntent(context, views);
			
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		context.startService(new Intent(context, UpdateService.class));

	}

	/**
	 * @param context
	 * @param views
	 */
	private void setServerViewIntent(Context context, RemoteViews views) {
		Intent intent = new Intent(context, AFServerList.class);
		intent.putExtra(context.getString(R.string.redirect_key), AFServerList.class.getName().toString());
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		views.setOnClickPendingIntent(R.id.widget_server_container, pendingIntent);
		views.setOnClickPendingIntent(R.id.widget_server_count, pendingIntent);
	}
	
	/**
	 * @param context
	 * @param views
	 */
	private void setAlertViewIntent(Context context, RemoteViews views) {
		Intent intent = new Intent(context, AFAlertList.class);
		intent.putExtra(context.getString(R.string.redirect_key), AFAlertList.class.getName().toString());
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		views.setOnClickPendingIntent(R.id.widget_alert_container, pendingIntent);
		views.setOnClickPendingIntent(R.id.widget_alert_count, pendingIntent);
	}

	/**
	 * @param context
	 * @param views
	 */
	private void setUpdateIntent(Context context, RemoteViews views) {
		Intent clickintent = new Intent(context, UpdateService.class);
		PendingIntent pendingIntentClick = PendingIntent.getService(
				context, 0, clickintent, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.widget_refresh_button,
				pendingIntentClick);
	}

	/**
	 * @param context
	 * @param views
	 */
	private void setHomeScreenIntent(Context context, RemoteViews views) {
		Intent homeIntent = new Intent(context, LoginScreen.class);
		PendingIntent homePendingIntent = PendingIntent.getActivity(context, 0, homeIntent, 0);
		//views.setOnClickPendingIntent(R.id.widget_inner_container, homePendingIntent);
		views.setOnClickPendingIntent(R.id.widget_icon, homePendingIntent);
		views.setOnClickPendingIntent(R.id.widget_text, homePendingIntent);
	}

	/**
	 * Set the server count in the widget.
	 * 
	 * @param views
	 *            the remote view instance
	 * @param context
	 *            the application running context.
	 */
	protected static int setServerCount(RemoteViews views, Context context) {
		int ret = 0;
		MainApplication.loadServerList(Helper.getServerListUrl(context));
		if (MainApplication.getServers() != null) {
			ret = MainApplication.getServers().size();
			String serverCount = String.format("%d", ret);
			views.setTextViewText(R.id.widget_server_count, serverCount);
		}
		return ret;
	}

	/**
	 * Set the alert count in the widget.
	 * 
	 * @param views
	 *            the remote view instance
	 * @param context
	 *            the application running context.
	 */
	protected static int setAlertCount(RemoteViews views, Context context) {
		int incidents = MainApplication.getAlertIncidents(context);
		views.setTextViewText(R.id.widget_alert_count, String.format("%d", incidents));
		return incidents;
	}

	/**
	 * 
	 * @param context
	 * @param manager
	 * @param thisWidget
	 */
	protected static void setLoading(Context context, AppWidgetManager manager,
			ComponentName thisWidget) {
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);
		views.setTextViewText(R.id.widget_text, "Updating...");
		manager.updateAppWidget(thisWidget, views);
	}

	/**
	 * Set the updated time in the widget.
	 * 
	 * @param views
	 *            the remote view instance
	 * @param context
	 *            the application running context.
	 */
	protected static void setUpdateTime(RemoteViews views, Context context) {
		String text = Helper.formatTime(System.currentTimeMillis());
		views.setTextViewText(R.id.widget_text, text);

	}
	
	protected static void setUpdateFailure(RemoteViews views, Context context) {
		String text = "Not logged in.";
		views.setTextViewText(R.id.widget_text, text);
		views.setTextViewText(R.id.widget_server_count, "");
		views.setTextViewText(R.id.widget_alert_count, "");

	}

	public static class UpdateService extends Service {
		@Override
		public void onStart(Intent intent, int startId) {
			Log.d("AFDefaultWidgetProvider.UpdateService", "onStart()");

			// Push update for this widget to the home screen
			ComponentName thisWidget = new ComponentName(this,
					AFDefaultWidgetProvider.class);
			AppWidgetManager manager = AppWidgetManager.getInstance(this);
			setLoading(this, manager, thisWidget);
			RemoteViews updateViews = buildUpdate(this);
			manager.updateAppWidget(thisWidget, updateViews);
			Log.d("AFDefaultWidgetProvider.UpdateService", "Update done. ");
		}

		@Override
		public IBinder onBind(Intent intent) {
			return null;
		}

		/**
		 * Update the view context inside the service. All the update function
		 * should be called here.
		 * 
		 * @param context
		 *            the running context of the application.
		 * @return the udpated views
		 */
		public RemoteViews buildUpdate(Context context) {
			// Pick out month names from resources
			RemoteViews views = null;
			views = new RemoteViews(context.getPackageName(),
					R.layout.widget_layout);

			byte[] ret = MainApplication.getSavedLogin(context);
			if (ret == null) {
				return views;
			}
			Log.i(TAG, ret.toString());
			MainApplication.client.setmAuthString(ret);
			try {
				if (!MainApplication.checkClientLogin(context)) {
					setUpdateFailure(views, context);
				} else {
					setServerCount(views, context);
					setAlertCount(views, context);
					setUpdateTime(views, context);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			views.setViewVisibility(R.id.widget_refresh_button, View.VISIBLE);
			return views;
		}
	}
}
