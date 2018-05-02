package com.xshell.xshelllib.greendao;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;

/**
 * 获取设备ID ，AndroidID+packageName
 *
 * @author ZhangSong
 */
public class PhoneInfo {

    private Context context;
    private String android_id;
    private static PhoneInfo id;
    private static final String TAG = "AndroidID";

    private PhoneInfo(Context context) {
        android_id = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        this.context = context;
    }

    public static PhoneInfo getInstance(Context context) {
        if (id == null) {
            synchronized (TAG) {
                if (id == null) {
                    id = new PhoneInfo(context);
                }
            }
        }
        return id;
    }

    public String getDeviceID() {
        return android_id;
    }


    //获得手机型号
    public String getModel() {
        return android.os.Build.MODEL;

    }

    //获得Android版本
    public String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    public String getPixels() {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels + "*" + dm.heightPixels;
    }

    public int getPixels_w() {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public int getPixels_h() {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取APP当前版本号
     * @return 版本号
     */
    @SuppressWarnings("unused")
    public int getVersionCode() {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            // 当前应用的版本名称
            String versionName = info.versionName;
            // 当前版本的版本号
            int versionCode = info.versionCode;
            // 当前版本的包名
            String packageNames = info.packageName;
            return versionCode;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 获取APP当前版本名称
     *
     * @return 当前版本名称
     */
    @SuppressWarnings("unused")
    public String getVersionName() {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            // 当前应用的版本名称
            String versionName = info.versionName;
            // 当前版本的包名
           // String packageNames = info.packageName;
            // 当前版本的版本号
           // int versionCode = info.versionCode;
            return versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    /**
     * 获取APP当前版本名称
     *
     * @return 当前版本名称
     */
    @SuppressWarnings("unused")
    public String getPackageName() {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            // 当前应用的版本名称
           // String versionName = info.versionName;
            // 当前版本的包名
           String packageNames = info.packageName;
            // 当前版本的版本号
            // int versionCode = info.versionCode;
            return packageNames;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取APP的名称
     * @return App的名称
     */
    @SuppressWarnings("unused")
    public String getAppName() {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String appName = info.applicationInfo.loadLabel(manager).toString();
            return appName;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取手机品牌
     *
     * @return
     */
    public String getPhoneBrand() {
        String phoneBrand = android.os.Build.BRAND;
        if(phoneBrand.equals("acer")){
            return phoneBrand = "宏基";
        }else if(phoneBrand.equals("aiwa")){
            return phoneBrand = "爱华";
        }else if(phoneBrand.equals("asus")){
            return phoneBrand = "华硕";
        }else if(phoneBrand.equals("benq")){
            return phoneBrand = "明基";
        }else if(phoneBrand.equals("benten")){
            return phoneBrand = "巨腾";
        }else if(phoneBrand.equals("chase")){
            return phoneBrand = "采星";
        }else if(phoneBrand.equals("cect") || phoneBrand.equals("cec")){
            return phoneBrand = "中电通讯";
        }else if(phoneBrand.equals("ericsson" )){
            return phoneBrand = "爱立信";
        }else if(phoneBrand.equals("gionee") ){
            return phoneBrand = "金立";
        }else if(phoneBrand.equals("haier") || phoneBrand.equals("har")){
            return phoneBrand = "海尔";
        }else if(phoneBrand.equals("hisense")){
            return phoneBrand = "海信";
        }else if(phoneBrand.equals("jpg")){
            return phoneBrand = "金鹏";
        }else if(phoneBrand.equals("lenovo")){
            return phoneBrand = "联想";
        }else if(phoneBrand.equals("malata")){
            return phoneBrand = "万利达";
        }else if(phoneBrand.equals("motorola")){
            return phoneBrand = "摩托罗拉";
        }else if(phoneBrand.equals("nokia") || phoneBrand.equals("nki")){
            return phoneBrand = "诺基亚";
        }else if(phoneBrand.equals("nopo")){
            return phoneBrand = "星辰";
        }else if(phoneBrand.equals("philips")){
            return phoneBrand = "飞利浦";
        }else if(phoneBrand.equals("rowa")){
            return phoneBrand = "乐华";
        }else if(phoneBrand.equals("sanyo")){
            return phoneBrand = "三洋";
        }else if(phoneBrand.equals("samsung")){
            return phoneBrand = "三星";
        }else if(phoneBrand.equals("sony")){
            return phoneBrand = "索尼";
        }else if(phoneBrand.equals("tcl")){
            return phoneBrand = "王牌";
        }else if(phoneBrand.equals("telson")){
            return phoneBrand = "泰尔信";
        }else if(phoneBrand.equals("ztc")){
            return phoneBrand = "中天伟业";
        }else if(phoneBrand.equals("zte")){
            return phoneBrand = "中兴";
        }else if(phoneBrand.equals("vodafone")){
            return phoneBrand = "沃达丰";
        }else if(phoneBrand.equals("htc")){
            return phoneBrand = "宏达电";
        }else if(phoneBrand.equals("miui")){
            return phoneBrand = "小米";
        }else if(phoneBrand.equals("htc")){
            return phoneBrand = "宏达电";
        }else if(phoneBrand.equals("honor")){
            return phoneBrand = "华为荣耀";
        }else if(phoneBrand.equals("meizu")){
            return phoneBrand = "魅族";
        }else if(phoneBrand.equals("coolpad")){
            return phoneBrand = "酷派";
        }else if(phoneBrand.equals("uawei")){
            return phoneBrand = "华为";
        }
        return phoneBrand;
    }
}
