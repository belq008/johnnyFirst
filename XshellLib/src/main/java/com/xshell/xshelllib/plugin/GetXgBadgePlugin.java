package com.xshell.xshelllib.plugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;

import android.os.Handler;

import com.xshell.xshelllib.badger.ShortcutBadgeException;
import com.xshell.xshelllib.badger.ShortcutBadger;

/**
 * 角标插件
 * @author zzy
 *
 */
public class GetXgBadgePlugin extends CordovaPlugin {

	
	@Override
	public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
		if("setXGBadge".equals(action)) {
			final String badgeCount = args.getString(0);
			Handler handler  =  new Handler();
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					try {
						ShortcutBadger.setBadge(cordova.getActivity().getApplicationContext(), Integer.parseInt(badgeCount));
					} catch (ShortcutBadgeException e) {
						e.printStackTrace();
					}
				}
			}, 3000);
		}
		
		return true;
	}
}
