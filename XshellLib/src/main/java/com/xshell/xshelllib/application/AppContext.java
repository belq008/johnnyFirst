//package com.xshell.xshelllib.application;
//
//
//import android.app.Application;
//import android.content.Context;
//
//import com.xshell.xshelllib.utils.PreferenceUtil;
//
//
//public class AppContext extends Application {
//    @SuppressWarnings("unused")
//    private static final String TAG = "LCApplication";
//    public static Context CONTEXT;
//    public static AppContext APPCONTEXT;
//
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        CONTEXT = this;
//        APPCONTEXT = this;
//        PreferenceUtil.getInstance();
//        init();
//
//    }
//
//    //
//
//
//    /**
//     * 获取进程号对应的进程名
//     *
//     * @return 进程名
//     */
//
//
//    private void init() {
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
//    }
//
//
//}
