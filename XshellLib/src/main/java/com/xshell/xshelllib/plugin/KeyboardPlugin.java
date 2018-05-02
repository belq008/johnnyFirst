package com.xshell.xshelllib.plugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * 软键盘的插件
 * @author zzy
 *
 */
public class KeyboardPlugin extends CordovaPlugin {
	@Override
	public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
		if ("show".equals(action)) {
			Intent intent = new Intent("inputMethod");
			intent.putExtra("callbackName", args.getString(0));
			intent.putExtra("type", args.getString(1));
			intent.putExtra("defalutIndex", args.getString(2));
			LocalBroadcastManager.getInstance(cordova.getActivity()).sendBroadcast(intent);
			return true;
		}
		return true;
	}
	
}
