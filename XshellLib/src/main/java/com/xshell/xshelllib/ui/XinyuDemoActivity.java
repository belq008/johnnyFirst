package com.xshell.xshelllib.ui;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xshell.xshelllib.R;

import java.io.File;

/**
 * 芯与的功能入口Activity
 */
public class XinyuDemoActivity extends XinyuHomeActivity {

    TextView tVTitle;


    public int getContentViewRes() {
        return R.layout.xinyusoft_main;
    }

    @Override
    public int getLinearLayoutId() {
        return R.id.linearLayout;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        String url;
        if (getIntent().hasExtra("jumpUrl")) {
            url = "file:///" + getFilesDir().getAbsolutePath() + File.separator + getIntent().getStringExtra("jumpUrl");
        } else {
            url = "file:///" + getFilesDir().getAbsolutePath() + File.separator + "index.html";
        }
        loadUrl(url);
        //appView.getView().setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        initTitle();

    }

    private void initTitle() {
        this.findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tVTitle = this.findViewById(R.id.tv_page_title);
    }

    IntentFilter filter = new IntentFilter("webview_title");

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(titleReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(titleReceiver);
    }

    BroadcastReceiver titleReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("webview_title")) {
                if (!TextUtils.isEmpty(intent.getStringExtra("title"))) {
                    tVTitle.setText(intent.getStringExtra("title"));
                }
            }
        }
    };

}
