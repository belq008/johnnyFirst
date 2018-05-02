package com.xshell.xshelllib.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;


import com.xshell.xshelllib.R;
import com.xshell.xshelllib.application.AppConstants;
import com.xshell.xshelllib.utils.FulStatusBarUtil;

public class GuideActivity extends BaseSampleActivity {

    private String webindex;
    private GuideBroadcastRecevier recever;
    String imageurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        imageurl = this.getFilesDir().getAbsolutePath() + "/imagepage/android";
        FulStatusBarUtil.setcolorfulStatusBar(this);

        setContentView(R.layout.xinyusoft_activity_guide);

        if (getIntent() != null) {// 获取loading传过来的参数
            Intent inte = getIntent();
            webindex = inte.getStringExtra("jumpurl");
        }
        //mAdapter = new TestFragmentAdapter(getSupportFragmentManager(), GuideActivity.this, webindex,imageurl);
        // 注册guideactivity的广播接收者
        recever = new GuideBroadcastRecevier();
        registerReceiver(recever, new IntentFilter(AppConstants.GUIDE_BROADCAST_NAME));

        mPager = (ViewPager) findViewById(R.id.pager);
//        mPager.setAdapter(mAdapter);
//
//        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
//        mIndicator.setViewPager(mPager);
    }


    class GuideBroadcastRecevier extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int type = intent.getIntExtra("type", 0);
            int posi = intent.getIntExtra("posi", 0);
            switch (type) {
                case 0:
//                    if (posi == getFilLength()) {
//                        btn.setVisibility(View.VISIBLE);
//                        ifAll = true;
//                    }
                    break;
                case 1:
//                    if (posi == getFilLength()) {
//                        btn.setVisibility(View.VISIBLE);
//                    }
//                    btn.setVisibility(View.GONE);
                    break;
                case 3:
                    GuideActivity.this.finish();
                    break;
            }
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(recever);
    }


}
