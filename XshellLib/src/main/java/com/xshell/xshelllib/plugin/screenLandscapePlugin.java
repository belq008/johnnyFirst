package com.xshell.xshelllib.plugin;

import android.content.pm.ActivityInfo;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wn on 2016/5/8.
 */
public class screenLandscapePlugin extends CordovaPlugin {

    @Override
    public boolean execute(String action, CordovaArgs args,
                           CallbackContext callbackContext) throws JSONException {
        // TODO Auto-generated method stub
        try{
        String result = args.getString(0);
        if ("startToChangeOrientation".equals(action)) {
            JSONObject jos = new JSONObject(result);
            final String callBackName = jos.getString("callbackName");
            if ("1".equals(jos.getString("type"))) {
                cordova.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            } else {
                cordova.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            }
            cordova.getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    webView.loadUrl("javascript:" + callBackName.trim() + "('{\"result\":1}')");
                }
            });
        }
         return true;
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }

}
