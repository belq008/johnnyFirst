package com.xshell.xshelllib.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.xshell.xshelllib.R;
import com.xshell.xshelllib.application.AppConstants;
import com.xshell.xshelllib.utils.FulStatusBarUtil;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.x5engine.X5WebView;

public class NewBrowserActivity extends XinyuHomeActivity {
    private NewBrowserInnerReceiver mReceiver;
    private CallbackContext mcallbackContext;
//    private MyReceiver mReceiver1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FulStatusBarUtil.setcolorfulStatusBar(this);
        String url = null;
        // SDK初始化 ，请放在游戏启动界面

        if (getIntent().hasExtra("newBrowserUrl")) {
            String temp = getIntent().getStringExtra("newBrowserUrl");
            if ("http".regionMatches(0, temp, 0, 4)) {
                url = temp;
            } else {
                if (getIntent().hasExtra("projectListUrl")) {
//                    url = "file:///" + getFilesDir().getAbsolutePath() + File.separator + getIntent().getStringExtra("projectListUrl") + File.separator + temp;
                    url = "file:///android_asset/www/" + temp;
                } else {
//                    url = "file:///" + getFilesDir().getAbsolutePath() + File.separator + temp;
                    url = "file:///android_asset/www/" + temp;
                }
                // url = "file:///android_asset/www/"+temp;
            }
        }

        loadUrl(url);
        Log.e("huanghu", "NewBrowserActivity:onCreate-------------------");
        appView.getView().setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        NewBrowserCollector.addActivityOnlyName(this.toString());

    }

//    public class MyReceiver extends BroadcastReceiver {
//        //  当sendbroadcast发送广播时，系统会调用onReceive方法来接收广播
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            // Toast.makeText(context, "成功接收广播：" , Toast.LENGTH_LONG).show();
//            //  判断是否为sendbroadcast发送的广播
//            if ("iaaapay".equals(intent.getAction())) {
//                Bundle bundle = intent.getExtras();
//                if (bundle != null) {
//                    String transid = bundle.getString("transid");
//                    //  mcallbackContext = (CallbackContext) bundle.getParcelable("mcallbackContext");
//                    IAppPay.startPay(NewBrowserActivity.this, "transid=" + transid + "&appid=" + PayConfig.appid, iPayResultCallback);
//                    //    Toast.makeText(context, "成功接收广播：" + transid, Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//    }

    /**
     * 支付结果回调
     */
//    IPayResultCallback iPayResultCallback = new IPayResultCallback() {
//
//        @Override
//        public void onPayResult(int resultCode, String signValue, String resultInfo) {
//            Intent intent = new Intent("iaaapayback");
//            switch (resultCode) {
//                case IAppPay.PAY_SUCCESS:
//                    intent.putExtra("resultCode", resultCode);
//                    intent.putExtra("resultInfo_SUCCESS", resultInfo);
//                    break;
//                case IAppPay.PAY_ERROR:
//                    intent.putExtra("resultCode", resultCode);
//                    intent.putExtra("resultInfo", resultInfo);
//                    break;
//                case IAppPay.PAY_CANCEL:
//
//                    break;
//                default:
//
//                    break;
//            }
//            intent.putExtra("resultCode", resultCode + "");
//            intent.putExtra("resultInfo", resultInfo);
//            sendBroadcast(intent);
//            Log.e("MainDemoActivity", "requestCode:" + resultCode + ",signvalue:" + signValue + ",resultInfo:" + resultInfo);
//        }
//    };
    @Override
    public int getContentViewRes() {
        return R.layout.xinyusoft_main;
    }

    @Override
    public int getLinearLayoutId() {
        return R.id.linearLayout;
    }


    @Override
    protected void onResume() {
        super.onResume();
        mRegReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
        // unregisterReceiver(mReceiver1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (NewBrowserCollector.getOnlyNamesFirstName().equals(this.toString())) {
//            NewBrowserCollector.removeAllNames();
//            System.exit(0);
//        }
        SystemWebView view = (SystemWebView) appView.getEngine().getView();
        if (view != null) {
            view.clearCache(true);
//        view.removeAllViews();
            ((ViewGroup) view.getParent()).removeView(view);
            view.destroy();
            view = null;
        }
        appView.clearHistory();
        appView.getView().destroyDrawingCache();

    }

    private void mRegReceiver() {
        mReceiver = new NewBrowserInnerReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstants.RELOAD_NEW_BROWSER_PAGE);
        registerReceiver(mReceiver, filter);

        //动态注册广播
        // mReceiver1 = new MyReceiver();
//        IntentFilter mFilter1 = new IntentFilter();
//        mFilter1.addAction("iaaapay");
//        registerReceiver(mReceiver1, mFilter1);
    }

    /**
     * 广播接收器,在子类又写了一个，这个是为了通知所有的界面
     *
     * @author zzy, wn
     */
    private class NewBrowserInnerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (AppConstants.RELOAD_NEW_BROWSER_PAGE.equals(action)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    SystemWebView v = (SystemWebView) appView.getEngine().getView();
                    v.reload();
                } else {
                    X5WebView v = (X5WebView) appView.getEngine().getView();
                    v.reload();
                }
            }
        }

    }


    @Override
    public void showTitleBar() {
        String title = getIntent().getStringExtra("title");
        if (title != null && !title.equals("")) {
            RelativeLayout rl_titleBar = (RelativeLayout) findViewById(R.id.rl_titleBar);
            rl_titleBar.setVisibility(View.VISIBLE);
            TextView tv_title = (TextView) findViewById(R.id.tv_page_title);
            tv_title.setText(title);
            TextView back = (TextView) findViewById(R.id.tv_back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
}
