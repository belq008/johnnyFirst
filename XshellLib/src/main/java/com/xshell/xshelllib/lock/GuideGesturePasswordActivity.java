//package com.xshell.xshelllib.lock;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.Window;
//import android.view.WindowManager;
//
//
//import com.xshell.xshelllib.application.AppContext;
//
//public class GuideGesturePasswordActivity extends Activity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        //去除title
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //去掉Activity上面的状态栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.xinyusoft_gesturepassword_guide);
////        if (1==getIntent().getIntExtra("createLock",-1)) {
////            AppContext.APPCONTEXT.getLockPatternUtils().clearLock();
////        }
////
////        Intent intent = new Intent(GuideGesturePasswordActivity.this,
////                CreateGesturePasswordActivity.class);
////        intent.putExtra("functionName", getIntent().getStringExtra("functionName"));
////        intent.putExtra("createLock", getIntent().getIntExtra("createLock",-1));
////        // 打开新的Activity
////        startActivity(intent);
//        finish();
//    }
//
//}
