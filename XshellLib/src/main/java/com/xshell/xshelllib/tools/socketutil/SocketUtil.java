package com.xshell.xshelllib.tools.socketutil;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.xshell.xshelllib.logutil.LogUtils;
import com.xshell.xshelllib.plugin.WebSocketPlugin;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

/**
 * Created by ZHOUCHAO on 2016/3/21.
 * socket的核心工具类
 */
public class SocketUtil {


    private Handler mHandler;

    static class MyWebSocketConnection extends WebSocketConnection {
        public void close() {
            try {

                Socket socket = mTransportChannel.socket();
                if (null != socket && socket.isConnected()) {
                    socket.shutdownInput();
                    socket.shutdownOutput();
                    socket.close();
                }
                mTransportChannel.close();
                webSocketConnection = null;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class MyWebSocketHandler extends WebSocketHandler {

        private OnSocketListener onSocketOpen;

        public MyWebSocketHandler(OnSocketListener onSockeOpen) {
            this.onSocketOpen = onSockeOpen;
        }

        @Override
        public void onOpen() {
            super.onOpen();
            onSocketOpen.onSocketOpen();
            Log.i("zzy", "1111111111111111111111111111111");
        }

        @Override
        public void onClose(int code, String reason) {
            super.onClose(code, reason);
            Log.i("zzy", "code:" + code + "-------------" + reason);
            //onSocketOpen.onSocketClose();
        }

        @Override
        public void onTextMessage(String payload) {
            try {
                Log.e("zzy", "payload----:" + payload);
                JSONObject jsonObject = new JSONObject(payload);
                Message obtain = Message.obtain();
                obtain.obj = payload;

                String req = "";
                if (jsonObject.has("requestseq")) {
                    req = jsonObject.getString("requestseq");
                    obtain.what = WebSocketPlugin.MESSAGE;
                    mHandler.sendMessage(obtain);
                    Log.e("zzy", "req----:" + req);
                } else if (jsonObject.has("tunnelname")) {
                    req = jsonObject.getString("tunnelname");
                    obtain.what = WebSocketPlugin.MESSAGE_tunnelname;
                    mHandler.sendMessage(obtain);
                } else if (jsonObject.has("topic")) {
                    req = jsonObject.getString("topic");
                    obtain.what = WebSocketPlugin.MESSAGE_topic;
                    mHandler.sendMessage(obtain);
                    if (req.equals("order")) {
                        Log.d("topic111", payload);
                    }
                } else if (jsonObject.has("cmd")) {
                    req = jsonObject.getString("cmd");
                    obtain.what = WebSocketPlugin.MESSAGE;
                    mHandler.sendMessage(obtain);
                }
//                OnResultMessage onResultMessage = onResultMessageMap.get(req);
//
//                if (onResultMessage != null) {
//                    Log.e("zzy","onResultMessage----:"+payload);
//                    onResultMessage.resultMessage(payload);
//                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private static MyWebSocketConnection webSocketConnection = new MyWebSocketConnection();

    private static Map<String, OnResultMessage> onResultMessageMap = new HashMap<String, OnResultMessage>();
    //    public static  String SOCKET_URL = "ws://121.40.129.195:8080/echo";
    public static String SOCKET_URL = "ws://innertest.xinyusoft.com:85/xpush/echo";
    private static WebSocketHandler webSocketHandler;


    public static Map<String, OnResultMessage> getOnResultMessageMap() {
        return onResultMessageMap;
    }

    public static void removeResultMessage(String key) {
        onResultMessageMap.remove(key);
    }


    public static boolean hasConnected() {
        if (webSocketConnection == null) {  //代表正在断开连接，还没有重新连接 ,此时也返回true
            return true;
        }

        if (webSocketConnection.isConnected()) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean disConnect() {
        //webSocketConnection.disconnect();
        webSocketConnection.close();
        try {
            if (webSocketConnection.isConnected()) {
                return true;
            } else {
                LogUtils.e("zzy", "执行一次尝试连接");
                //webSocketHandler = null;
//            initWebSockeet();
                return false;
            }
        } catch (Exception e) {
            LogUtils.e("zzy", "Exception:" + e.toString());
            return false;
        }

    }

    public void connect(String url, OnSocketListener onSocketOpen) {
        SOCKET_URL = url;
        webSocketConnection = new MyWebSocketConnection();
        try {
            webSocketConnection.connect(SOCKET_URL, new MyWebSocketHandler(onSocketOpen));
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送要个普通的消息
     *
     * @param request         请求url
     * @param requestseq      键
     * @param onResultMessage 回调函数
     */
    public void sendTextMessage(String request, Handler handler, String requestseq, OnResultMessage onResultMessage) {
        mHandler = handler;
        onResultMessageMap.put(requestseq, onResultMessage);
        if (webSocketConnection.isConnected()) {
            LogUtils.e("huanghu", requestseq + "&requestseq=" + request);
            webSocketConnection.sendTextMessage(requestseq + "&requestseq=" + request);
        }

    }

    /**
     * 发送一个推送消息
     *
     * @param request         请求url
     * @param onResultMessage 回调函数
     */
    public static void sendPushMessage(String request, OnResultMessage onResultMessage) {
        String[] split = request.split("&");
        String temp = null;
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            if (s.contains("requestseq")) {
                temp = s;
                break;
            }
        }
        onResultMessageMap.put(temp.split("=")[1], onResultMessage);
        if (webSocketConnection.isConnected()) {
            webSocketConnection.sendTextMessage(request);
        }
    }

    /**
     * 发送一个订阅消息
     *
     * @param request         请求url
     * @param onResultMessage 回调函数
     */
    public static void sendsubscriptionMessage(String request, OnResultMessage onResultMessage) {
        String[] split = request.split("&");
        String temp = null;
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            if (s.contains("topic")) {
                temp = s;
                break;
            }
        }
        onResultMessageMap.put(temp.split("=")[1], onResultMessage);
        if (webSocketConnection.isConnected()) {
            webSocketConnection.sendTextMessage(request);
        }
    }

    public static boolean isConnected() {
        try {
            if (webSocketConnection.isConnected()) {
                return true;
            } else {
                LogUtils.e("zzy", "执行一次尝试连接");
                //webSocketHandler = null;
//            initWebSockeet();
                return false;
            }
        } catch (Exception e) {
            LogUtils.e("zzy", "Exception:" + e.toString());
            return false;
        }
    }
}

