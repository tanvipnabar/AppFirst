package com.appfirst.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.appfirst.datatypes.SystemData;
import com.appfirst.types.Server;

/**
 * 
 * 
 * @author Bin Liu
 *
 */
public class Helper {
	/**
    *
    * @param is
    * @return one line of string
    */
	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	public static List<Server> convertServerList(JSONArray jsonArray) {
		List<Server> serverList = new ArrayList<Server>();
		for (int i = 0; i < jsonArray.length(); i ++) {
			try {
				JSONObject jsonObject = (JSONObject)jsonArray.get(i);
				Server server = new Server(jsonObject);
				serverList.add(server);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return serverList;
	}
	
	public static List<SystemData> convertServerDataList(JSONArray jsonArray) {
		List<SystemData> serverList = new ArrayList<SystemData>();
		for (int i = 0; i < jsonArray.length(); i ++) {
			try {
				JSONObject jsonObject = (JSONObject)jsonArray.get(i);
				SystemData systemData = new SystemData(jsonObject);
				serverList.add(systemData);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return serverList;
	}
}
