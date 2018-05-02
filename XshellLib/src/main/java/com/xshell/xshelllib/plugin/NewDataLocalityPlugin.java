package com.xshell.xshelllib.plugin;

import android.content.Context;
import android.util.Log;

import com.xshell.xshelllib.sqlite.NewDataLocalityHuManager;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hushen on 2017/10/29.
 * 新的本地缓存测试
 */

public class NewDataLocalityPlugin extends CordovaPlugin {

    private Context context;
    private NewDataLocalityHuManager dataLocalityManager;
    private ConcurrentHashMap<CallbackContext, String> imgMap = new ConcurrentHashMap<CallbackContext, String>();
    private boolean isFist = true;//第二次打开数据库

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        context = cordova.getActivity();
        dataLocalityManager = NewDataLocalityHuManager.getInstance(context, "localstorage.db");
        Log.e("huanghu", "newdede初始化几次。。。。。。");
    }

    @Override
    public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        Log.e("huang", "--------------------------:" + action);
        if ("setValue".equals(action)) {//将字符串/map集合存入数据库中
            final String key = args.getString(0);
            final String jsonObject = args.getString(1);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        dataLocalityManager.openSQLData();
                        dataLocalityManager.createTableSql();
                        Log.e("huang", "--------------------------:key" + key + "===jsonObject:" + jsonObject);
                        dataLocalityManager.insertData(key, jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return true;
        } else if ("getByKey".equals(action)) {//从数据库中取字符串/map集合
            final String key = args.getString(0);
            Log.e("huang", "getByKey=========key:" + key);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    String data = "";
                    try {
                        dataLocalityManager.openSQLData();
                        dataLocalityManager.createTableSql();
                        data = dataLocalityManager.getData(key);
                        if (data == null) {
                            callbackContext.error("error");
                        }
                        JSONObject jsonObject = new JSONObject(data);
                        callbackContext.success(jsonObject);
                        Log.e("huang", "getByKey=========data:" + data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callbackContext.success(data);
                    }
                }
            });
            return true;
        } else if ("setList".equals(action)) {//存储Arr型数据
            final String disKey = args.getString(0);
            final JSONArray arrIdLsit = args.getJSONArray(1);
            final JSONArray arrList = args.getJSONArray(2);
            Log.e("huang", "setList ==== idlist与list的长度相等:" + arrIdLsit.length() + "==:" + arrList.length());
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    String data = "";
                    try {
                        dataLocalityManager.openSQLData();
                        dataLocalityManager.createArrayKeyTableSql();
                        data = dataLocalityManager.setList(disKey, arrIdLsit, arrList);
                        if (data == null) {
                            Log.e("huang", "setList ==== idlist与list的长度不相等");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return true;
        } else if ("updateData".equals(action)) { //更新某条数据
            final String disKey = args.getString(0);
            final int arrId = args.getInt(1);
            final String arrListObject = args.getString(2);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    String data = "";
                    try {
                        dataLocalityManager.openSQLData();
                        dataLocalityManager.createArrayKeyTableSql();
                        data = dataLocalityManager.updateData(disKey, arrId, arrListObject);
                        if (data == null) {
                            Log.e("huang", "setList ==== idlist与list的长度不相等");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return true;
        } else if ("getDataByKeyID".equals(action)) { //取出某一条数据
            final String disKey = args.getString(0);
            final int arrId = args.getInt(1);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = null;
                    try {
                        dataLocalityManager.openSQLData();
                        dataLocalityManager.createArrayKeyTableSql();
                        data = dataLocalityManager.getDataByKeyID(disKey, arrId);
                        Log.e("huang", "getDataByKeyID:" + data.toString());
                        if (data == null) {
                            callbackContext.error("error");
                        } else {
                            callbackContext.success(data);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return true;
        } else if ("getAllData".equals(action)) { //通过Key取出所有数据
            final String disKey = args.getString(0);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    JSONArray data = null;
                    try {
                        dataLocalityManager.openSQLData();
                        dataLocalityManager.createArrayKeyTableSql();
                        data = dataLocalityManager.getAllData(disKey);
                        Log.e("huang", "getAllData:" + data.toString());
                        if (data == null) {
                            callbackContext.error("error");
                        } else {
                            callbackContext.success(data);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return true;
        } else if ("getAllDataByKey".equals(action)) { //通过key获取(正序、倒序)数据
            final String disKey = args.getString(0);
            final String descAndAer = args.getString(1);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    JSONArray data = null;
                    try {
                        dataLocalityManager.openSQLData();
                        dataLocalityManager.createArrayKeyTableSql();
                        data = dataLocalityManager.getAllDataByKey(disKey, descAndAer);
                        Log.e("huang", "getAllDataByKey:" + data.toString());
                        if (data == null) {
                            callbackContext.error("error");
                        } else {
                            callbackContext.success(data);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return true;
        } else if ("getNumDataByKey".equals(action)) { //获取最新的N条数据
            final String disKey = args.getString(0);
            final int num = args.getInt(1);
            final String descAndAer = args.getString(2);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    JSONArray data = null;
                    try {
                        dataLocalityManager.openSQLData();
                        dataLocalityManager.createArrayKeyTableSql();
                        data = dataLocalityManager.getNumDataByKey(disKey, num, descAndAer);
                        Log.e("huang", "getNumDataByKey:" + data.toString());
                        if (data == null) {
                            callbackContext.error("error");
                        } else {
                            callbackContext.success(data);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return true;
        } else if ("getNumDataByKeyID".equals(action)) {//获取某个id之前/后的N条数据
            final String disKey = args.getString(0);
            final int id = args.getInt(1);
            final int num = args.getInt(2);
            final String state = args.getString(3);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    JSONArray data = null;
                    try {
                        dataLocalityManager.openSQLData();
                        dataLocalityManager.createArrayKeyTableSql();
                        data = dataLocalityManager.getNumDataByKeyID(disKey, id, num,state);
                        Log.e("huang", "getNumDataByKeyID:" + data.toString());
                        if (data == null) {
                            callbackContext.error("error");
                        } else {
                            callbackContext.success(data);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return true;
        }else if ("deleteDataByKeyID".equals(action)) { //删除某条数据
            final String disKey = args.getString(0);
            final int id = args.getInt(1);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    JSONArray data = null;
                    try {
                        dataLocalityManager.openSQLData();
                        dataLocalityManager.createArrayKeyTableSql();
                        dataLocalityManager.deleteDataByKeyID(disKey,id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return true;
        }else if ("deleteDataByKeySomeID".equals(action)) { //通过key删除一批id对应的数据
            final String disKey = args.getString(0);
            final String idString= args.getString(1);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    JSONArray data = null;
                    try {
                        dataLocalityManager.openSQLData();
                        dataLocalityManager.createArrayKeyTableSql();
                        dataLocalityManager.deleteDataByKeySomeID(disKey,idString);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return true;
        }else if ("deleteDataByKey".equals(action)) { //删除key对应的所有数据
            final String disKey = args.getString(0);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    JSONArray data = null;
                    try {
                        dataLocalityManager.openSQLData();
                        dataLocalityManager.createArrayKeyTableSql();
                        dataLocalityManager.deleteDataByKey(disKey);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return true;
        }
        return false;
    }
}
