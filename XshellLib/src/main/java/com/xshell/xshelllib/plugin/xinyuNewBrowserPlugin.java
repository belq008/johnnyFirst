package com.xshell.xshelllib.plugin;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.xshell.xshelllib.application.AppConstants;
import com.xshell.xshelllib.utils.Log2FileUtil;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wn on 2016/5/8.
 */
public class xinyuNewBrowserPlugin extends CordovaPlugin {

    /**
     * 当打开一个页面时，需要调用回调函数名字
     */
    private String newBroserCallBcak;
    private boolean isFlay = true;
    private Handler handler = new Handler();

    @Override
    public boolean execute(String action, CordovaArgs args,
                           CallbackContext callbackContext) throws JSONException {
        try {
            if ("xinyuNewBrowser".endsWith(action)) { // 开启一个新的Activity
                if (isFlay) {
                    isFlay = false;
                    Log.e("huanghu", "xinyuNewBrowser:开启一个新的Activity");
                    JSONObject jos = args.getJSONObject(0);
                    if (jos.has("callbackName")) {
                        newBroserCallBcak = jos.getString("callbackName");
                    }
                    String url = jos.getString("url");
                    Intent intent1 = new Intent();
                    Log2FileUtil.getInstance(context).saveCrashInfo2File("开启了一个新的Activity");
                    intent1.setAction(AppConstants.ACTION_NEW_BROSER);
                    intent1.putExtra("url", url);
                    LocalBroadcastManager.getInstance(cordova.getActivity()).sendBroadcast(intent1);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isFlay = true;
                        }
                    }, 500);
                }
                return true;
            } else if ("closeNewBrowser".equals(action)) { // 关闭Activity
                Intent intent1 = new Intent();
                intent1.setAction(AppConstants.ACTION_CLOSE_BROSER);
                Log2FileUtil.getInstance(context).saveCrashInfo2File("关闭了一个新的Activity");
                LocalBroadcastManager.getInstance(cordova.getActivity()).sendBroadcast(intent1);
                return true;
            }
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }

    Context context;


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        context = cordova.getActivity();
    }

    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);
        if (newBroserCallBcak != null) {
            cordova.getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    webView.loadUrl("javascript:" + newBroserCallBcak + "()");
                    newBroserCallBcak = null;
                }
            });
        }

    }
}
