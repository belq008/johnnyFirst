//package com.xinyusoft.xshelllib.service;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.IBinder;
//import android.util.Log;
//
//import com.xinyusoft.xshelllib.application.AppConstants;
//import com.xinyusoft.xshelllib.plugin.WebSocketPlugin;
//
//import de.tavendo.autobahn.WebSocketConnection;
//import de.tavendo.autobahn.WebSocketException;
//import de.tavendo.autobahn.WebSocketHandler;
//
//public class RAService extends Service {
//	String TAG = "MyIntentService";
//	WebSocketConnection wsc;
//
//	private String data;
//	private String url;
//
//	@Override
//	public void onCreate() {
//		Log.i(TAG, "onCreate");
//		wsc = new WebSocketConnection();
//		super.onCreate();
//
//	}
//
//	@Override
//	public void onDestroy() {
//		Log.i(TAG, "onDestroy RAIntentService");
//		wsc.disconnect();
//		super.onDestroy();
//	}
//
//	@Override
//	public int onStartCommand(final Intent intent, int flags, int startId) {
//		data = intent.getStringExtra("data");
//		Thread thread = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				switch (intent.getIntExtra("flag", 0)) {
//				case WebSocketPlugin.OPEN_WBESOCKET:
//					url = intent.getStringExtra("url");
//					connect(WebSocketPlugin.OPEN_WBESOCKET);
//					break;
//				case WebSocketPlugin.SEND_MESSAGE:
//					if (wsc.isConnected()) {
//						if (data != null) {
//							wsc.sendTextMessage(data);
//							data = null;
//						}
//					} else {
//						// 链接不起来，就重新连接？ 可以这样？
//						connect(WebSocketPlugin.SEND_MESSAGE);
//
//						Thread mThread = new Thread(new Runnable() {
//
//							@Override
//							public void run() {
//								while (!wsc.isConnected()) {
//									try {
//										Thread.sleep(100);
//									} catch (InterruptedException e) {
//										e.printStackTrace();
//									}
//								}
//								wsc.sendTextMessage(data);
//							}
//						});
//						mThread.start();
//					}
//
//					break;
//
//				}
//
//			}
//		});
//		thread.start();
//
//		return Service.START_NOT_STICKY;
//	}
//
//	private void connect(final int flag) {
//		try {
//			wsc.connect(url, new WebSocketHandler() {
//
//				@Override
//				public void onBinaryMessage(byte[] payload) {
//					System.out.println("onBinaryMessage size=" + payload.length);
//				}
//
//				@Override
//				public void onClose(int code, String reason) {
//					System.out.println("onClose reason=" + reason);
//					if (WebSocketPlugin.OPEN_WBESOCKET == flag) {
//						Intent intent = new Intent();
//						intent.putExtra("failReason", reason);
//						intent.setAction(AppConstants.WSC_SEND_PLUGIN_LOAGIN_FAILUER);
//						sendBroadcast(intent);
//					}
//				}
//
//				@Override
//				public void onOpen() {
//					if (WebSocketPlugin.OPEN_WBESOCKET == flag) {// 连接成功就发送连接成功的广播
//						Intent intent = new Intent();
//						intent.setAction(AppConstants.WSC_SEND_PLUGIN_LOAGIN_SUCCESS);
//						sendBroadcast(intent);
//					}
//				}
//
//				@Override
//				public void onRawTextMessage(byte[] payload) {
//					Log.i(TAG, " onRawTextMessage payload = " + payload);
//				}
//
//				@Override
//				public void onTextMessage(String payload) {
//					Intent intent = new Intent();
//					intent.setAction(AppConstants.WSC_SEND_PLUGIN);
//					intent.putExtra("message", payload);
//					sendBroadcast(intent);
//
//				}
//
//			});
//		} catch (WebSocketException e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	@Override
//	public IBinder onBind(Intent intent) {
//		return null;
//	}
//
//}