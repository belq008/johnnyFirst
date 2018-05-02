package com.xshell.xshelllib.plugin;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;


import com.xshell.xshelllib.application.AppConstants;
import com.xshell.xshelllib.utils.FileUtil;
import com.xshell.xshelllib.utils.RecorderUtil;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;

/**
 * 录音插件
 */
public class RecordPlugin extends CordovaPlugin {

    public static int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;
    private RecorderUtil mRecorderUtil = new RecorderUtil();
    private String name;
    private File path;
    Context mCtx;


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        mCtx = cordova.getActivity();
    }

    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
        super.onRequestPermissionResult(requestCode, permissions, grantResults);
        if (requestCode == 8) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(cordova.getActivity(), "授权!", Toast.LENGTH_SHORT).show();

                // Permission Granted
                starRecord();
            } else {
                // Permission Denied
                Toast.makeText(cordova.getActivity(), "由于您拒绝了授权，录音将无法进行！", Toast.LENGTH_SHORT).show();

            }
        }

    }


    String mAction;
    JSONArray mArgs;
    CallbackContext mCallbackContext;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
//            final String callBackName = args.getString(0);
//            if (callBackName == null) {
//                return false;
//            }
        com.xshell.xshelllib.logutil.LogUtils.e("huang", "execute");
        this.mAction = action;
        this.mArgs = args;
        this.mCallbackContext = callbackContext;

        if (!cordova.hasPermission(Manifest.permission.RECORD_AUDIO)) {
            cordova.requestPermissions(this, 8, new String[]{Manifest.permission.RECORD_AUDIO});
        } else {
            if (starRecord()) return true;
        }

        return false;
    }

    private boolean starRecord() throws JSONException {

//        final String callBackName = mArgs.getString(0);
        if ("startRecord".equals(mAction)) {//开始录音
            Log.e("huang", "startRecord");
            name = System.currentTimeMillis() + ".3gp";// 生成录音保存文件名
            try {
                path = FileUtil.getInstance(mCtx).createFileInSDCard(AppConstants.APP_ROOT_DIR + "/" + AppConstants.APP_AUDIO_DIR + "/" + name);// 生成录音保存路径
            } catch (IOException e) {
                e.printStackTrace();
            }
            mRecorderUtil.stopRecorder();
            mRecorderUtil.startRecorder(path.getAbsolutePath());// 开始录音
            //startTime = System.currentTimeMillis();
            // 回调js方法告诉页面开始录音
//            cordova.getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    webView.loadUrl("javascript:" + callBackName.trim() + "('1')");
//                }
//            });
            return true;
        } else if ("endRecord".equals(mAction)) {//结束录音
            com.xshell.xshelllib.logutil.LogUtils.e("huang", "endRecord");
            // long duration = System.currentTimeMillis() - startTime;
            //LogUtils.e("startTime==" + startTime + "duration==" + duration);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mRecorderUtil.stopRecorder();
            mCallbackContext.success(path.getAbsolutePath());
//            UploadUtil.upLoad(path, mArgs.getString(1), new RequestCallBack<String>() {
//                        @Override
//                        public void onSuccess(ResponseInfo<String> arg0) {
//                            String result = arg0.result;
//                            try {
//                                LogUtils.e(result);
//                                if (!TextUtils.isEmpty(result)) {
//                                    //上传之后就删除
//                                    if (path.exists()) {
//                                        path.delete();
//                                    }
////                            webView.loadUrl("javascript:" + callBackName.trim() + "('" + result + "')");
//                                    Log2FileUtil.getInstance().saveCrashInfo2File("录音上传成功：" + result.toString());
//
//                                } else {
//                                    LogUtils.e("result为空");
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                }
//
//                @Override
//                public void onFailure(HttpException arg0, String arg1) {
//                    LogUtils.e("arg0==" + arg0 + "arg1==" + arg1);
//                    Log2FileUtil.getInstance().saveCrashInfo2File("录音上传失败：" + arg0.toString() + "&&&&" + arg1.toString());
//                }
//
//                @Override
//                public void onLoading(long total, long current, boolean isUploading) {
//                    super.onLoading(total, current, isUploading);
//                }
//            });


            return true;
        }
        return false;
    }
}
