package com.xshell.xshelllib.plugin;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * 视频直播流，与点播
 * live,dov

 * @author hushen
 */
public class LiveVideoPlugin extends CordovaPlugin {

    private Activity activity;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        activity = cordova.getActivity();
        //  Log.e("huanghu","进43333333333");
    }

    @Override
    public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        Log.e("huanghu","进入微信");
        if ("livePlay".equals(action)) {
            Toast.makeText(activity, "进入腾讯直播", Toast.LENGTH_SHORT).show();
//            Toast.makeText(mcordova, "直播", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(mcordova, LiveAndDovActivity.class);
//            intent.putExtra("url","url");
//            mcordova.startActivity(intent);
            //   callbackContext.success();
                return true;

        }
        return false;
    }


}
