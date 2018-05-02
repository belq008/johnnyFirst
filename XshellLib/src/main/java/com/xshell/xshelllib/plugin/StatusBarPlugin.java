package com.xshell.xshelllib.plugin;


import android.content.Context;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONException;

/**
 * Created by DELL on 2017/3/1.#00000000
 */

public class StatusBarPlugin extends CordovaPlugin {


    private Context context;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        context = cordova.getActivity();

    }

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
//        if ("statusBarHide".equals(action)) {
//            Log.e("huanghu","statusBarHide");
//            Intent intent = new Intent();
//            intent.setAction("StatusBar");		//设置Action
//            intent.putExtra("Status","Status");
//            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
//            return true;
//        } else if("statusBarShow".equals(action)){
//            Log.e("huanghu","statusBarShow");
//            Intent intent = new Intent();
//            intent.setAction("StatusBar");		//设置Action
//            intent.putExtra("Status","StatusShow");
//            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
//            return true;
//        }

        return false;

    }

    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);
        //Log.e("huanghu","再一次来临啊!");

    }
}
