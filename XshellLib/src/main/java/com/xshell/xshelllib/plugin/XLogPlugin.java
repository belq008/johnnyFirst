package com.xshell.xshelllib.plugin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.xshell.xshelllib.application.AppConfig;

import com.xshell.xshelllib.greendao.PhoneInfo;

import com.xshell.xshelllib.utils.IpUtil;
import com.xshell.xshelllib.utils.NetUtil;
import com.xshell.xshelllib.utils.PreferenceUtil;
import com.xshell.xshelllib.utils.SharedPreferencesUtils;


import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by zzy on 2016/10/12.
 * XLog的插件
 */
public class XLogPlugin extends CordovaPlugin {

    private Context context;
    private String model;
    private String systemVersion;
    private String deviceId;
    private int pixels_w;
    private int pixels_h;
    private String versionName;
    private String packageName;

    private long startTime;


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        Log.e("zzy", "initialize:XLogPlugin");
        // loadTimeMap = new HashMap<>();
        context = cordova.getActivity();


        PhoneInfo phoneInfo = PhoneInfo.getInstance(context);
        //获取设备信息
        //手机型号
        model = phoneInfo.getModel();
        //版本
        systemVersion = "Android " + phoneInfo.getSystemVersion();
        //手机id
        deviceId = phoneInfo.getDeviceID();

        //分辨率
        pixels_w = phoneInfo.getPixels_w();
        pixels_h = phoneInfo.getPixels_h();
        //版本号
        versionName = PreferenceUtil.getInstance(context).getFileUpdateTime();
        //Log.i("zzy","verionName-----------------:"+versionName);
        packageName = phoneInfo.getPackageName();
    }


    @Override
    public Object onMessage(String id, Object data) {
        Log.i("zzy", "onMessage--------" + id + "-------------:" + data);
        if (id.equals("onPageStarted")) {
            //loadTimeMap.put((String)data, System.currentTimeMillis());
            startTime = System.currentTimeMillis();

        } else if (id.equals("onPageFinished")) {

            Intent intent = new Intent();
            intent.setAction("setStatusBar");
            cordova.getActivity().sendBroadcast(intent);

            //截取相对路径
            String url = (String) data;
            String relativePath;
            if (url.contains(packageName)) {
                // 1 斜杠， 5 files  1 斜杠
                int i = url.indexOf(packageName) + packageName.length() + 1 + 5 + 1;
                relativePath = url.substring(i);
            } else {
                int i = url.indexOf("android_asset") + "android_asset".length() + 1;
                relativePath = url.substring(i);
            }
            long loadTime = System.currentTimeMillis() - startTime;
            //获取网络情况
            String typeName = NetUtil.getTypeName(context);

            //拼接数据
            JSONObject mainObj = new JSONObject();
            JSONObject sysObj = new JSONObject();
            JSONObject pageObj = new JSONObject();
            JSONObject deviceObj = new JSONObject();
            try {
                sysObj.put("id", packageName);
                sysObj.put("version", versionName);
                sysObj.put("time", System.currentTimeMillis());
                sysObj.put("network", typeName);
                String ip = IpUtil.getIp();
                if (ip == null || "".equals(ip)) {
                    return null;
                }
                sysObj.put("ip", ip);

                Log.i("zzy", "ip:" + IpUtil.getIp());

                mainObj.put("sys", sysObj);

                pageObj.put("url", relativePath);
                pageObj.put("loadtime", loadTime);
                mainObj.put("page", pageObj);

                deviceObj.put("id", deviceId);
                deviceObj.put("os", systemVersion);
                deviceObj.put("model", model);
                deviceObj.put("width", pixels_w);
                deviceObj.put("height", pixels_h);
                mainObj.put("device", deviceObj);

                //保存到数据库
//                XLogDao xLogDao = GreenManager.getInstance(context).getXLogDao();
//                XLog xLog = new XLog(null, mainObj.toString());
//                xLogDao.insert(xLog);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return super.onMessage(id, data);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("recordLog".equals(action)) {
            Log.e("huang", "recordLog:");
            //拼接数据
            JSONObject mainObj = new JSONObject();
            JSONObject sysObj = new JSONObject();
            JSONObject pageObj = new JSONObject();
            JSONObject deviceObj = new JSONObject();
            try {
                sysObj.put("id", packageName);
                sysObj.put("version", versionName);
                sysObj.put("time", System.currentTimeMillis());
                sysObj.put("network", NetUtil.getTypeName(context));
                sysObj.put("ip", IpUtil.getIp());
                mainObj.put("sys", sysObj);

                pageObj.put("url", args.getString(0));
                pageObj.put("loadtime", 0);
                mainObj.put("page", pageObj);

                deviceObj.put("id", deviceId);
                deviceObj.put("os", systemVersion);
                deviceObj.put("model", model);
                deviceObj.put("width", pixels_w);
                deviceObj.put("height", pixels_h);
                mainObj.put("device", deviceObj);

                //保存到数据库
//                XLogDao xLogDao = GreenManager.getInstance(context).getXLogDao();
//                XLog xLog = new XLog(null, mainObj.toString());
//                Long insertResunt = xLogDao.insert(xLog);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        } else if ("getUserInfo".equals(action)) {
            JSONObject jsonObject = args.getJSONObject(0);
            Toast.makeText(context, "getUserInfo:" + jsonObject.toString(), Toast.LENGTH_SHORT).show();
            int statusCode = jsonObject.getInt("statusCode");//1是登入，-1是退出
            int sessionid;
            if (statusCode == 1) {
                sessionid = jsonObject.getInt("sessionid");
                SharedPreferencesUtils.setParam(context, "SESSIONID", sessionid);

                AppConfig.APP_STATUE = "App";
            } else {
                sessionid = (Integer) SharedPreferencesUtils.getParam(context, "SESSIONID", 0);
                SharedPreferencesUtils.setParam(context, "SESSIONID", 0);
                AppConfig.APP_STATUE = "";
            }
            return true;
        }
        return false;
    }


}
