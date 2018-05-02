package com.xshell.xshelllib.plugin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.xshell.xshelllib.application.AppConstants;
import com.xshell.xshelllib.logutil.LogUtils;
import com.xshell.xshelllib.utils.FileUtil;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class FileOperationPlugin extends CordovaPlugin {
    private static final String TAG = "FileOperationPlugin";

    Context mctx;


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        mctx = cordova.getActivity();
        LogUtils.e("huang", "FileOperationPlugin");
    }

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        // TODO Auto-generated method stub

        FileUtil fileUtil = FileUtil.getInstance(mctx);
        try {
            if ("writeFile".equals(action)) {// 添加文件
                File mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + AppConstants.APP_ROOT_DIR);
                if (!mFile.exists()) {
                    mFile.mkdir();
                }

                String fileName = args.getString(0);
                String data = args.getString(1);
                boolean isAppend = args.getBoolean(2);
                int rt = 2;
                try {
                    fileUtil.write2SDFromString(AppConstants.APP_ROOT_DIR + "/" + fileName, data, isAppend);
                    rt = 1;
                } catch (Exception e) {
                    rt = 0;
                }
                JSONObject json = new JSONObject();
                json.put("result", rt);
                callbackContext.error(json.toString());
                return true;
            } else if ("deleteFile".equals(action)) {// 删除文件
                String fileName = args.getString(0);

                boolean tmpSign = fileUtil.delete(
                        new File(fileUtil.getPathSDCard()
                                + AppConstants.APP_ROOT_DIR), fileName);
                Log.d("dd", "被删除");
                int rt = tmpSign ? 1 : 0;

                JSONObject json = new JSONObject();
                json.put("result", rt);
                if (!tmpSign) {
                    callbackContext.error(json.toString());
                } else {
                    callbackContext.error(json.toString());
                }
                return true;

            } else if ("exist".equals(action)) {//文件是否存在
                String fileName = args.getString(0);
                boolean isExist = fileUtil.isFileExist(AppConstants.APP_ROOT_DIR + "/" + fileName);
                int rt = isExist ? 1 : 0;
                JSONObject json = new JSONObject();
                json.put("result", rt);
                callbackContext.success(isExist + "");
                return true;
            } else if ("readFile".equals(action)) {//读文件
                String fileName = args.getString(0);
                String fileContent = null;
                int rt;
                try {
                    fileContent = FileUtil.getInstance(mctx).readSringFromSD(
                            AppConstants.APP_ROOT_DIR + "/" + fileName);
                    LogUtils.e("dd", fileContent + "被读取出");
                    rt = 1;
                } catch (Exception e) {
                    rt = 0;
                    e.printStackTrace();
                }

                JSONObject json = new JSONObject();
                json.put("result", rt);
                json.put("content", fileContent);
                callbackContext.success(json.toString());
                return true;
            } else if ("openFile".equals(action)) {//浏览文件
                LogUtils.e("dd", "openFile:浏览文件");
                String string = args.getString(0);
                String s = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + AppConstants.APP_ROOT_DIR + "/" + string;
                File file = new File(s);
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri uri = Uri.fromFile(file);
                intent.setDataAndType(uri, "text/plain");
//                if(null==file || !file.exists()){
//                    callbackContext.error(false+"");
//                }
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.addCategory(Intent.CATEGORY_DEFAULT);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.setDataAndType(Uri.fromFile(file), "file/*");
//                try {
                cordova.getActivity().startActivity(intent);
//                    cordova.getActivity().startActivity(Intent.createChooser(intent,"选择浏览工具"));
//                } catch (ActivityNotFoundException e) {
//                    e.printStackTrace();
//                }
                return true;
            }

        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return false;
    }

}
