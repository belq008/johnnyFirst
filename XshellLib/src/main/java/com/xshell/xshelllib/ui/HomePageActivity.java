package com.xshell.xshelllib.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.xshell.xshelllib.R;
import com.xshell.xshelllib.application.AppConstants;
import com.xshell.xshelllib.utils.FulStatusBarUtil;
import com.xshell.xshelllib.utils.PhoneInfo;

import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.x5engine.X5WebView;

import java.io.File;

public class HomePageActivity extends XinyuHomeActivity {

    private static final int XINGE_JUMP = 1;

    private MYInnerReceiver homePageReceiver;

    private Handler tHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case XINGE_JUMP:
                    String jumpurl = (String) msg.obj;
                    String url = "javascript:" + "xgmsgPop" + "('" + jumpurl + "')";
                    Log.e("zzy", "myjjjjjurl:" + url);
                    loadUrl(url);
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FulStatusBarUtil.setcolorfulStatusBar(this);
//        uploadXLog();
        String url = null;
        String listUrl = getIntent().getStringExtra("projectListUrl");
        Log.i("zzy", "listUrl HomePageActivity:" + listUrl);
        if (listUrl == null) {   //代表此时不是项目（uniapp）的应用
            //url = "file:///android_asset/www/testLuckView.html";
            url = "file:///" + getFilesDir().getAbsolutePath() + File.separator + "index.html";
        } else {
            url = "file:///" + getFilesDir().getAbsolutePath() + File.separator + listUrl + File.separator + "index.html";
        }
        mRegReceiver();
        loadUrl(url);
        appView.getView().setLayerType(View.LAYER_TYPE_SOFTWARE, null);


    }

//    private void uploadXLog() {
//        XLogUpload.getInstance(this).uploadXLog();
//        try {
//            //   XMessageUpload.getInstance(this).uploadXMessage();
//        } catch (Exception e) {
//
//        }
//
//    }

    private void mRegReceiver() {
        homePageReceiver = new MYInnerReceiver();
        IntentFilter filter = new IntentFilter();
        // 刷新页面
        filter.addAction(AppConstants.RELOAD_HOME_PAGE);
        registerReceiver(homePageReceiver, filter);
    }


    /**
     * 广播接收器，在子类又写了一个，这个为了是通知所有的界面
     *
     * @author zzy, wn
     */
    private class MYInnerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (AppConstants.RELOAD_HOME_PAGE.equals(action)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    SystemWebView v = (SystemWebView) appView.getEngine().getView();
                    v.reload();
                    v.willNotDraw();
                } else {
                    X5WebView v = (X5WebView) appView.getEngine().getView();
                    v.reload();
                }

            }
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (intent.hasExtra("jumpurl")) {
            Message message = Message.obtain();
            message.what = XINGE_JUMP;
            message.obj = intent.getStringExtra("jumpurl");
            tHandler.sendMessageDelayed(message, 3000);
            appView.getView().setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            return;
        }

    }

    @Override
    public int getContentViewRes() {
        return R.layout.xinyusoft_main;
    }

    @Override
    public int getLinearLayoutId() {
        return R.id.linearLayout;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (homePageReceiver != null) {
            unregisterReceiver(homePageReceiver);
        }
    }


}
