//package com.xshell.xshelllib.application;
//
//import android.app.ActivityManager;
//import android.app.Application;
//import android.content.Context;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.xshell.xshelllib.utils.PreferenceUtil;
//import com.xshell.xshelllib.utils.Write2SDCard;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.List;
//
//public class AppContext1 extends Application {
//    @SuppressWarnings("unused")
//    private static final String TAG = "LCApplication";
//    public static Context CONTEXT;
//    public static AppContext1 APPCONTEXT;
//    private String cookies = null;
//    private HashMap<String, String> CookieContiner = null;
//    private String sid;
//    private String custid;
//    private String curfundid;
//    private String cursecuid;
//    private Context context;
//
//
//    public HashMap<String, String> getCookieContiner() {
//        return CookieContiner;
//    }
//
//    public void setCookieContiner(HashMap<String, String> cookieContiner) {
//        CookieContiner = cookieContiner;
//    }
//
//    public String getCookies() {
//        return cookies;
//    }
//
//    public void setCookies(String cookies) {
//        this.cookies = cookies;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//       // UCSManager.init(this);
////		//Bugly的信息
////		context = getApplicationContext();
////		// 获取当前包名
////		String packageName = context.getPackageName();
////		// 获取当前进程名
//        //   	String processName = getProcessName(android.os.Process.myPid());
////		// 设置是否为上报进程
////		CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
////		strategy.setUploadProcess(processName == null || processName.equals(packageName));
////		// 初始化Bugly
////		CrashReport.initCrashReport(context, "bc80ff0ea8", false, strategy);
//
////		String processName = getProcessName(this, android.os.Process.myPid());
////		if (processName != null) {
////			boolean defaultProcess = processName.equals(getPackageName());
////			if (defaultProcess) {
////				initMainProcess();
////			} else if (processName.contains(":mqtt")) {
////				//TODO-处理mqtt进程的初始化
////			}
////		}
//        CONTEXT = this;
//        APPCONTEXT = this;
//        PreferenceUtil.getInstance(mctx);
//        init();
//        //UCSService.init(this, true);
//        Log.e("huanghu", "AppContext初始化几次。。。。。。");
//    }
//
//    //
//    public static String getProcessName(Context context, int pid) {
//        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
//        if (runningApps != null && !runningApps.isEmpty()) {
//            for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
//                Log.e("huang", "00000000000000000000");
//                if (procInfo.pid == pid) {
//                    return procInfo.processName;
//                }
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public void onTerminate() {
//        // 程序终止的时候执行
//        Log.e("huang", "onTerminate--------------");
//        super.onTerminate();
//    }
//
//    /**
//     * 获取进程号对应的进程名
//     *
//     * @param pid 进程号
//     * @return 进程名
//     */
//    private static String getProcessName(int pid) {
//        BufferedReader reader = null;
//        try {
//            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
//            String processName = reader.readLine();
//            if (!TextUtils.isEmpty(processName)) {
//                processName = processName.trim();
//            }
//            return processName;
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        } finally {
//            try {
//                if (reader != null) {
//                    reader.close();
//                }
//            } catch (IOException exception) {
//                exception.printStackTrace();
//            }
//        }
//        return null;
//    }
//
//
//
//    private void init() {
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
//    }
//
////	private void initVersion() {// 根据code值判断是否是覆盖安装
////		int codeOld = PreferenceUtil.getInstance().getAppThisCode();
////		int codeNew = VersionUtil.getVersionCode(CONTEXT);
////		if (codeNew != codeOld) { // code值不一样 代表刚刚进行覆盖安装
////			installNewAPK(codeNew);
////		} else if (VersionUtil.getPackLastUpdataTime(CONTEXT) != PreferenceUtil.getInstance().getAppPackUpdataTime()) {// 同版本覆盖安装
////			installNewAPK(codeNew);
////		}
////	}
////
////	private void installNewAPK(int codeNew) {
////		if (PreferenceUtil.getInstance().isNextToInstall()) {// 下载完成 需要安装
////			// 把时间修改为刚更新的时间
////			long cacheTime = TimeUtil.getUpdataTimeLong(PreferenceUtil.getInstance().getAppCacheTime());
////			long appTime = TimeUtil.getUpdataTimeLong(AppConstants.APP_UPDATE_START_TIME);
////			if (cacheTime > appTime) {
////				PreferenceUtil.getInstance().setAppUpdateTime(PreferenceUtil.getInstance().getAppCacheTime());
////			} else {
////				PreferenceUtil.getInstance().setAppUpdateTime(AppConstants.APP_UPDATE_START_TIME);
////			}
////		}
////
////		PreferenceUtil.getInstance().setAppThisCode(codeNew);
////		PreferenceUtil.getInstance().setNextToInstall(false);
////
////		if (AppConfig.NEW_DATA) {// 内置文件 强制安装到手机中
////			PreferenceUtil.getInstance().setInstall(false);
////			PreferenceUtil.getInstance().setFileUpdateTime(AppConstants.FILE_UPDATE_START_TIME);
////		}
////		// apk pack更新时间修改一下
////		PreferenceUtil.getInstance().setAppPackUpdataTime(VersionUtil.getPackLastUpdataTime(CONTEXT));
////	}
//
//    private void initErrorLog() {// 初始化错误手机日志
//        AppException appEcxeption = AppException.getInstance();
//        appEcxeption.init(CONTEXT);
//        if (AppConfig.WIRTE_SDCARD)
//            Write2SDCard.getInstance().writeMsg("开始写入");
//    }
//
//    // 打印log
//    public static void log(String tag, String msg) {
//        Log.d(tag, msg);
//    }
//
//    public String getSid() {
//        return sid;
//    }
//
//    public void setSid(String sid) {
//        this.sid = sid;
//    }
//
//    public String getCustid() {
//        return custid;
//    }
//
//    public void setCustid(String custid) {
//        this.custid = custid;
//    }
//
//    public String getCurfundid() {
//        return curfundid;
//    }
//
//    public void setCurfundid(String curfundid) {
//        this.curfundid = curfundid;
//    }
//
//    public String getCursecuid() {
//        return cursecuid;
//    }
//
//    public void setCursecuid(String cursecuid) {
//        this.cursecuid = cursecuid;
//    }
//}
