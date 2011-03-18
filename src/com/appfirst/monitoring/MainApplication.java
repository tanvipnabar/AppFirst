package com.appfirst.monitoring;

import com.appfirst.communication.AFClient;

import android.app.Application;

/**
 * Represents the whole application and serves as a global variable. 
 * It holds a {@link AFClient} member to access all the datas. 
 * @author Bin Liu
 *
 */
public class MainApplication extends Application {
	public static AFClient client = new AFClient();
}
