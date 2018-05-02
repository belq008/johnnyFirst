package com.xshell.xshelllib.plugin;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.xshell.xshelllib.logutil.LogUtils;
import com.xshell.xshelllib.tools.socketutil.OnPushTextMessage;
import com.xshell.xshelllib.tools.socketutil.OnResultMessage;
import com.xshell.xshelllib.tools.socketutil.OnSocketListener;
import com.xshell.xshelllib.tools.socketutil.PushUtil;
import com.xshell.xshelllib.tools.socketutil.SocketUtil;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONException;

/**
 * Created by zzy on 2016/8/7.
 * <p>
 * WebSocket插件
 */
public class WebSocketPlugin extends CordovaPlugin implements OnSocketListener, OnPushTextMessage {
    private CallbackContext callbackContext;
    private CordovaArgs args;
    private String pushCallbackName;
    public final static int MESSAGE = 0;
    public final static int MESSAGE_tunnelname = 1;
    public final static int MESSAGE_topic = 2;
    private  Handler handler = new Handler(){
        public void handleMessage(Message msg){
            String obj = (String)msg.obj;
            LogUtils.e("huang","obj:"+obj);
            if(msg.what == MESSAGE) {
                callbackContext.success(obj);
            }else if(msg.what == MESSAGE_tunnelname) {
                callbackContext.success(obj);
            }else if(msg.what == MESSAGE_topic) {
                callbackContext.success(obj);
            }
        }
    };
    private SocketUtil socketUtil;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        socketUtil = new SocketUtil();
    }

    @Override
    public boolean execute(String action, CordovaArgs args, final CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        this.args = args;
        if ("open".equals(action)) {  //开启一个websoket
            LogUtils.e("huang", "开启一个websoket");
            String url = args.getString(0);
            if (url == null) {
                callbackContext.error("url is null!");
                return false;
            }
            socketUtil.connect(url, this);
            return true;
        } else if ("sendMessage".equals(action)) {  //发送websocket信息
            String cmd = args.getString(0);
            String str = args.getString(1);
            String content = "cmd="+cmd + "&" + str;
            LogUtils.e("huang", "发送websocket信息:"+content);
            if (content == null) {
                callbackContext.error("content is null!");
                return false;
            }
            socketUtil.sendTextMessage("sendMessage",handler,content, new OnResultMessage() {
                @Override
                public void resultMessage(String result) {
                    LogUtils.e("zzy", "result:" + result);
                    callbackContext.success(result);
                }
            });
//            PushUtil.registerPush(content, callbackContext);
            return true;
        } else if ("registerPush".equals(action)) {  //注册推送
            String cmd = args.getString(0);
            String str = args.getString(1);
            String content = "cmd="+cmd+"&"+str;
            LogUtils.e("huang", "注册推送:"+content);
            socketUtil.sendTextMessage("tradepush",handler,content, new OnResultMessage() {
                @Override
                public void resultMessage(String result) {
                    LogUtils.e("shen","注册推送");
                    callbackContext.success(result);
                }
            });
//            PushUtil.registerPush(content, callbackContext);
            return true;
        } else if ("subscribePush".equals(action)) {  //订阅推送
            String cmd = args.getString(0);
            String str = args.getString(1);
            String content = "cmd="+cmd+"&"+str;
//            pushCallbackName = args.getString(1);
            LogUtils.e("huang", "订阅推送:"+content);
            PushUtil.setOnPushTextMessages(content, this);
//            PushUtil.subscriptionPush(content, callbackContext);
            socketUtil.sendTextMessage("subscribePush",handler,content, new OnResultMessage() {
                @Override
                public void resultMessage(String result) {
                    LogUtils.e("zzy", "result:" + result);
                    callbackContext.success(result);
                }
            });
            return true;
        } else if ("close".equals(action)) {  //关闭
            LogUtils.e("huang", "关闭");
            boolean b = SocketUtil.disConnect();
            callbackContext.error(b+"");
            return true;
        } else if ("isConnect".equals(action)) {//WebSocket是否连接
            boolean connected = SocketUtil.isConnected();
            callbackContext.success(connected+"");
            LogUtils.e("huang", "WebSocket是否连接:" + connected);
            return true;
        } else if ("unSubscribePush".equals(action)) {//取消订阅
            String cmd = args.getString(0);
            String str = args.getString(1);
//            PushUtil.cancelSubscriptionOrderWTPush(cmd, str);
            socketUtil.sendTextMessage("cancel", handler, "cmd="+cmd+"&"+str, new OnResultMessage() {
                @Override
                public void resultMessage(String result) {
                    LogUtils.e("huang","撤回一个订阅:"+result);
                }
            });
            LogUtils.e("huang", "取消订阅:");
            return true;
        }
        return false;
    }

    @Override
    public void onSocketOpen() {
        Log.i("zzy", "0000000000000:");
        callbackContext.success("openSuccess!");
    }

    @Override
    public void onPushTextMessage(String result) {
        Log.i("zzy", "-----------111-------:" + result);
//        webView.loadUrl("javascript:" + pushCallbackName + "('"+result+"')");
        callbackContext.success(result);
    }
}
