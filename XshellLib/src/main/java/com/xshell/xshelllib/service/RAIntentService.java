//package com.xinyusoft.xshelllib.service;
//
//import android.app.IntentService;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.IBinder;
//import android.util.Log;
//
//import com.xinyusoft.xshelllib.application.AppConstants;
//
//import de.tavendo.autobahn.WebSocketConnection;
//import de.tavendo.autobahn.WebSocketException;
//import de.tavendo.autobahn.WebSocketHandler;
//
//public class RAIntentService extends IntentService {
//	String TAG = "MyIntentService";
//	Thread thr;
//	int running = 0;
//	WebSocketConnection wsc;
//	WSCInnerReceiver mReceiver;
//
//	private String data;
//
//	public RAIntentService() {
//		super("MyIntentService");
//	}
//
//	@Override
//	public void onCreate() {
//		Log.i(TAG, "onCreate");
//		wsc = new WebSocketConnection();
//		mRegReceiver();
//		super.onCreate();
//	}
//
//	@Override
//	public IBinder onBind(Intent intent) {
//		// TODO Auto-generated method stub
//		return super.onBind(intent);
//	}
//
//	@Override
//	public void onDestroy() {
//		// TODO Auto-generated method stub
//		Log.i(TAG, "onDestroy RAIntentService");
//		running = 0;
//		wsc.disconnect();
//		//LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mReceiver);
//		unregisterReceiver(mReceiver);
//		super.onDestroy();
//	}
//
//	@Override
//	protected void onHandleIntent(Intent intent) {
//		// 经测试，IntentService里面是可以进行耗时的操作的
//		// IntentService使用队列的方式将请求的Intent加入队列，然后开启一个worker
//		// thread(线程)来处理队列中的Intent
//		// 对于异步的startService请求，IntentService会处理完成一个之后再处理第二个
//		Log.i(TAG, "onHandleIntent"+this);
//		if (running != 1) {
//			running = 1;
//			connect();
//			while (running == 1) {
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				Log.i("zzy","this:"+this);
//				if(wsc.isConnected()) {
//					if(data != null) {
//						wsc.sendTextMessage(data);
//						data = null;
//					}
//
//				} else {
//					//链接不起来，就重新连接？ k可以这样？
//					connect();
//				}
//
//			}
//		}
//	}
//
//	private void connect() {
//		try {
//			wsc.connect("ws://121.40.129.195:9999/echo", new WebSocketHandler() {
//
//				@Override
//				public void onBinaryMessage(byte[] payload) {
//					System.out.println("onBinaryMessage size=" + payload.length);
//				}
//
//
//				@Override
//				public void onClose(int code, String reason) {
//					Log.i("zzy","code:"+code);
//					System.out.println("onClose reason=" + reason);
//				}
//
//				@Override
//				public void onOpen() {
//					Log.i("zzy", "onOpen");
//					//wsc.sendTextMessage("Hello!");
//					// wsc.disconnect();
//				}
//
//				@Override
//				public void onRawTextMessage(byte[] payload) {
//					Log.i(TAG, " onRawTextMessage payload = " + payload);
//				}
//
//				@Override
//				public void onTextMessage(String payload) {
//					Log.i(TAG, "onTextMessage payload = " + payload);
//					Intent intent = new Intent();
//					intent.setAction(AppConstants.WSC_SEND_PLUGIN);
//					intent.putExtra("message", payload);
//					sendBroadcast(intent);
//				}
//
//			});
//		} catch (WebSocketException e) {
//			e.printStackTrace();
//		}
//
//
//	}
//
//	private void mRegReceiver() {
//		mReceiver = new WSCInnerReceiver();
//		IntentFilter filter = new IntentFilter();
//		filter.addAction(AppConstants.WSC_SEND_SERVICE);
//		registerReceiver(mReceiver, filter);
//	}
//
//
//	/**
//	 * 广播接收器
//	 *
//	 * @author zzy,wn
//	 *
//	 */
//	private class WSCInnerReceiver extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			String action = intent.getAction();
//			if (AppConstants.WSC_SEND_SERVICE.equals(action)) {
//
//				String datas = intent.getStringExtra("data");
//				Log.i("zzy","进入data:"+datas + "isConnected:"+wsc.isConnected());
//				data = datas;
//
//			}
//		}
//
//	}
//
//
//}