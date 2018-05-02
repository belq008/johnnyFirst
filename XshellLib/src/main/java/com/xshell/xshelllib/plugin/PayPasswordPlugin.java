package com.xshell.xshelllib.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PayPasswordPlugin extends CordovaPlugin {

	private MyReceiver mReceiver;
	private CallbackContext mCallbackContext;

	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);

		//动态注册广播
		mReceiver = new MyReceiver();
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction("PayPasswordPlugin");
		cordova.getActivity().registerReceiver(mReceiver, mFilter);
		Log.e("huanghu","SDK初始化");
	}

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		mCallbackContext = callbackContext;
		try {
			if ("setPayPassword".equals(action)) {//支付密码
//				JSONObject jos = args.getJSONObject(0);
//				String callbackName = jos.getString("callbackName");
				String callbackName = "callbackName";
				Intent intent = new Intent();
				intent.setAction("setPayPassword");
				intent.putExtra("callbackName", callbackName);
				LocalBroadcastManager.getInstance(cordova.getActivity()).sendBroadcast(intent);
				return true;
			} else if("payPasswordState".equals(action)){//支付密码状态
//				JSONObject jos = args.getJSONObject(0);
//				String payState = jos.getString("payState");
                String payState = args.getInt(0)+"";
				Intent intent = new Intent();
				intent.setAction("payState");
				intent.putExtra("payState", payState);
				LocalBroadcastManager.getInstance(cordova.getActivity()).sendBroadcast(intent);
				return true;
			}

		} catch (Exception e) {
			callbackContext.error(e.getMessage());
			return false;
		}
		return false;
	}

	public class MyReceiver extends BroadcastReceiver {
		//  当sendbroadcast发送广播时，系统会调用onReceive方法来接收广播
		@Override
		public void onReceive(Context context, Intent intent) {
			   Toast.makeText(context, "成功接收广播======：" , Toast.LENGTH_LONG).show();
			//  判断是否为sendbroadcast发送的广播
			if ("PayPasswordPlugin".equals(intent.getAction())) {
				Bundle bundle = intent.getExtras();
				if (bundle != null) {
					String resultInfo = bundle.getString("password");
					mCallbackContext.success(resultInfo);
					//   Toast.makeText(context, "成功接收广播pppppp："+resultInfo+"===:"+resultCode, Toast.LENGTH_LONG).show();
				}
			}
		}
	}

}
