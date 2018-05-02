package com.xshell.xshelllib.plugin;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.xshell.xshelllib.ui.InputPasswordActivity;
import com.xshell.xshelllib.ui.XinyuHomeActivity;


import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by DELL on 2017/11/8.
 */

public class InputBoxEidText extends CordovaPlugin {

    private Context mContext;
    private CallbackContext callbackContext;
    //    private SoftKeyboardInputCommentsDialog softKeyboardInputCommentsDialog;
    private MyReceiver mReceiver;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        mContext = cordova.getActivity();
        ///动态注册广播
        mReceiver = new MyReceiver();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction("inputThing");
        cordova.getActivity().registerReceiver(mReceiver, mFilter);
        // Log.e("huang","调起输入框");
    }

    @Override
    public boolean execute(String action, CordovaArgs args, final CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        if ("commentbox".equals(action)) {
            JSONObject jsonObject = args.getJSONObject(0);//title，boxcolor，btncolor，placetitle
            Log.e("huang", "commentbox==jsonObject:" + jsonObject);
            String title = jsonObject.optString("titel");//按钮的文字
            String boxcolor = jsonObject.optString("boxcolor");//输入框背景颜色
            String btncolor = jsonObject.optString("btncolor");//按钮的背景颜色
            String placetitle = jsonObject.optString("placetitle");//替换输入框文字
            String btnbordercolor = jsonObject.optString("btnbordercolor");//替换按钮边框的颜色
            String titlecolor = jsonObject.optString("titlecolor");//替换按钮字体的颜色
            String text = jsonObject.optString("text");//没发送后保存的内容
            //        //让布局向上移来显示软键盘
//            softKeyboardInputCommentsDialog = new SoftKeyboardInputCommentsDialog(mContext, R.style.xinyusoft_dialog_fullscreen,title,boxcolor,btncolor,placetitle,btnbordercolor,titlecolor,text);
//            Window window = softKeyboardInputCommentsDialog.getWindow();
//            softKeyboardInputCommentsDialog.getWindow().setDimAmount((float) 0.1);//设置昏暗度为0
//            softKeyboardInputCommentsDialog.setCanceledOnTouchOutside(true);
//            window.setGravity(Gravity.BOTTOM);
//            softKeyboardInputCommentsDialog.show();
            return true;
        }
        return true;
    }

    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);

    }


    public class MyReceiver extends BroadcastReceiver {
        //  当sendbroadcast发送广播时，系统会调用onReceive方法来接收广播
        @Override
        public void onReceive(Context context, Intent intent) {
            // Toast.makeText(context, "成功接收广播======：", Toast.LENGTH_LONG).show();
            //  判断是否为sendbroadcast发送的广播
            if ("inputThing".equals(intent.getAction())) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        String content = bundle.getString("send");
                        int statusCode = bundle.getInt("statusCode");
                        jsonObject.put("statusCode", statusCode);
                        jsonObject.put("content", content);
                        callbackContext.success(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cordova.getActivity().unregisterReceiver(mReceiver);
    }
}
