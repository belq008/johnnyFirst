package com.xshell.xshelllib.ui;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.xshell.xshelllib.R;
import com.xshell.xshelllib.application.AppConstants;
import com.xshell.xshelllib.plugin.RecordPlugin;
import com.xshell.xshelllib.utils.IpUtil;

import org.apache.cordova.CordovaActivity;

import java.io.File;

/**
 * 主界面
 *
 * @author zzy
 */
public abstract class XinyuHomeActivity extends CordovaActivity {

    private static final String TAG = "XinyuHomeActivity";
    private InnerReceiver receiver;
    private Context xinyuHomeContext;


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        xinyusoftInit();
    }

    private void xinyusoftInit() {
        if (IpUtil.getIp().equals("127.0.0.1")) {
            IpUtil.requestIp();
        }
        xinyuHomeContext = XinyuHomeActivity.this;
    }


    private void regReceiver() {
        if (receiver == null) {
            receiver = new InnerReceiver();
            IntentFilter filter = new IntentFilter();
            // 播放
            // 开启一个新的Activity
            filter.addAction(AppConstants.ACTION_NEW_BROSER);
            // 关闭Activity
            filter.addAction(AppConstants.ACTION_CLOSE_BROSER);
            LocalBroadcastManager.getInstance(XinyuHomeActivity.this).registerReceiver(receiver, filter);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordPlugin.WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
            } else {
                // Permission Denied
//                Toast.makeText(this, "由于您拒绝了授权，录音将无法进行！", Toast.LENGTH_SHORT).show();
                Log.e("hhh", "由于您拒绝了授权，录音将无法进行！");
            }
        }
    }


    public void setIsAgainRegister(boolean isRegisterPassword) {
        //this.isRegisterPassword = isRegisterPassword;
    }


    /**
     * 广播接收器
     *
     * @author zzy, wn
     */
    private class InnerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context mContext, Intent intent) {
            String action = intent.getAction();
            if (AppConstants.ACTION_NEW_BROSER.equals(action)) { // 开启一个新的Activity
                String localUrl = intent.getStringExtra("url");
                Intent in;
                in = new Intent(XinyuHomeActivity.this, NewBrowserActivity.class);
                in.putExtra("newBrowserUrl", localUrl);
                if (XinyuHomeActivity.this.getIntent().hasExtra("projectListUrl")) {
                    String listUrl = XinyuHomeActivity.this.getIntent().getStringExtra("projectListUrl");
                    in.putExtra("projectListUrl", listUrl);
                }
                startActivity(in);
                overridePendingTransition(R.anim.xinyusoft_activity_right_in, R.anim.xinyusoft_activity_left_out);
            } else if (AppConstants.ACTION_CLOSE_BROSER.equals(action)) { // 关闭那个新的broser
                finish();
                boolean anima = intent.getBooleanExtra("anima", false);
                Log.e("huanghu", "关闭那个新的NewBrowserActivity:anima:" + anima);
                if (anima) {
                    overridePendingTransition(R.anim.xinyusoft_activity_left_in, R.anim.xinyusoft_activity_right_out);
                } else {
                    overridePendingTransition(0, 0);
                }
            } else if ("textJumpUrl".equals(action)) {
                String url = null;
                url = "file:///" + getFilesDir().getAbsolutePath() + File.separator + "uufpBase.html";
                loadUrl(url);
            }
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i(TAG, "onConfigurationChanged:");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver != null) {
            LocalBroadcastManager.getInstance(xinyuHomeContext).unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        synchronized (TAG) {
//            XLogUpload.getInstance(this).uploadXLog();
//        }

        regReceiver();// 注册广播
        NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(101010);
    }

    @Override
    protected void onStop() {
        super.onStop();

        NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(101010);

    }


//    public boolean isAppOnForeground() {
//        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
//        String packageName = getApplicationContext().getPackageName();
//
//        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
//        if (appProcesses == null)
//            return false;
//
//        for (RunningAppProcessInfo appProcess : appProcesses) {
//            // The name of the process that this object is associated with.
//            if (appProcess.processName.equals(packageName) || appProcess.processName.equals(packageName + ":xinyu_remote") || appProcess.processName.contains("com.tencent.")) {
//                if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
//                    return true;
//                }
//            }
//        }
//
//        return false;
//
//    }


}
