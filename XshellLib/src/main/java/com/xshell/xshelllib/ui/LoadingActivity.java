package com.xshell.xshelllib.ui;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;

import android.content.Context;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.util.LogUtils;

import com.xshell.xshelllib.R;
import com.xshell.xshelllib.application.AppConfig;
import com.xshell.xshelllib.application.AppConstants;
import com.xshell.xshelllib.utils.Assets2DataCardUtil;
import com.xshell.xshelllib.utils.FileUtil;
import com.xshell.xshelllib.utils.FulStatusBarUtil;
import com.xshell.xshelllib.utils.ParseConfig;
import com.xshell.xshelllib.utils.PreferenceUtil;
import com.xshell.xshelllib.utils.TimeUtil;
import com.xshell.xshelllib.utils.VersionUtil;
import com.xshell.xshelllib.utils.Write2SDCard;
import com.xshell.xshelllib.utils.ZIPUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.Call;

/**
 * 初始界面（包括下载，更新等初始化）
 *
 * @author zzy
 */
public class LoadingActivity extends Activity {
    private static final String TAG = "LoadingActivity";
    private Context context;
    private static Handler mHandler;


    private TextView showMessage;
    /**
     * 更新
     */
    private static final int UPDATE_MESSAGE = 222;
    private static final int SHOW_MESSAGE = 223;


    private final static int SWITCH_TWOACTIVITY = 1000; // 主页

    /**
     * 解析的配置文件集合
     */
    private Map<String, String> configInfo;
    /**
     * 主线程
     */
    private Thread mainThread;

    // 程序的开始时间
    private long startTime;
    private static int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!this.isTaskRoot()) { //判断该Activity是不是任务空间的源Activity
            if (getIntent().hasCategory(Intent.CATEGORY_LAUNCHER) && getIntent().getAction().equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
        //TODO 设置状态栏颜色
        //FulStatusBarUtil.setcolorfulStatusBar(this,R.color.actionsheet_blue);
        startTime = System.currentTimeMillis();
        setContentView(R.layout.xinyusoft_activity_splash);
        // 删除前台的检测更新包
        String[] temp = getPackageName().split("\\.");
        String fileName = FileUtil.getInstance(context).getFilePathInSDCard(AppConstants.XINYUSOFT_CACHE, temp[temp.length - 1] + "backgroundupdatehtml5.zip");
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }

        initView();

        mainThread = new Thread() {
            @Override
            public void run() {
                // -1解析appconfig.xml
                configInfo = ParseConfig.getInstance(LoadingActivity.this).getConfigInfo();
                //TODO 判断是否清理缓存了，如果是拿到以前的包进行解压
                try {
                    // 判断是否覆盖安装了，是的话，重新解压并且重新设置时间
                    if (VersionUtil.getVersionCode(context) > PreferenceUtil.getInstance(context).getAppThisCode()) {
                        // 设置app的code
                        PreferenceUtil.getInstance(context).setAppThisCode(VersionUtil.getVersionCode(context));
                        // 将Assets中数据写入data/data中
                        File cordovaFile = Assets2DataCardUtil.write2DataFromInput("cordova_android.zip", "cordova_android.zip", getApplicationContext());
                        //解压www的html5文件
                        //解压cordova需要的文件
                        ZIPUtils.unzipTest(cordovaFile.getAbsolutePath(), getFilesDir().getAbsolutePath());

                        if (AppConfig.DEBUG)
                            Log.e("zzy", "第一次解压完成！!!!!!!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


                //申请权限(用来使得sd卡获得写的权限)
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(LoadingActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    checkH5();
                }
            }
        };


        mainThread.start();


    }

    private void initView() {
        context = LoadingActivity.this;
        showMessage = (TextView) findViewById(R.id.showLoadingMessage);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE_MESSAGE:
                        showMessage.setText((String) msg.obj);
                        break;
                    case SHOW_MESSAGE:
                        showMessage.setVisibility(View.VISIBLE);
                        break;

                }

            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (permissions.length == 0) {
                checkH5();
            }
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkH5();
            } else {
                Toast.makeText(context, "由于您拒绝了授权，更新将无法进行！", Toast.LENGTH_SHORT).show();
                jump();
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle result = data.getBundleExtra("result");
        resultCode++;
        if (result != null && !TextUtils.isEmpty(result.getString(AccountManager.KEY_AUTHTOKEN))) {
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 检查html5更新
     */
    private void checkH5() {
        // 没有写下载列表就直接跳过这个
        if (configInfo.get("xversion-html-name") == null || "".equals(configInfo.get("xversion-html-name"))) {
            jump();
            return;
        }
        String url = configInfo.get("xversion-update-url") + "&appname=" + configInfo.get("xversion-html-name") + "&time=" + PreferenceUtil.getInstance(context).getFileUpdateTime() + "&platform=Android" + "&xshllversion="
                + VersionUtil.getVersionCode(context);

        Log.i("zzy", "checkH5-------------:" + url);

        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                Log.i(TAG, "检查html5失败:");
                Toast.makeText(LoadingActivity.this, "没有可用的网络连接", Toast.LENGTH_SHORT).show();
                jump();
            }

            @Override
            public void onResponse(String s, int i) {
                try {
                    if (AppConfig.DEBUG) {
                        Log.i("zzy", "开始检查html5:" + s);
                    }
                    showMessageUsehandler("请等待...");
                    toJsonFile(s);

                } catch (JSONException e) {
                    e.printStackTrace();
                    jump();
                }
            }
        });
    }

    /**
     * 下载html5
     *
     * @param res json的字符串
     * @throws JSONException
     */
    private void toJsonFile(String res) throws JSONException {
        JSONObject json = new JSONObject(res);
        JSONObject op = json.getJSONObject("op");
        String code = op.getString("code");

        final int count = json.getInt("count");
        final String changezip = json.getString("changezip");

        JSONArray array = json.getJSONArray("changelist");
        for (int i = 0; i < array.length(); i++) {
            JSONObject html = array.getJSONObject(i);
            String path = html.getString("path");
            String status = html.getString("status");
            if ("DELETE".equals(status)) {  //找到带有删除状态的并且删除
                File file = new File(getFilesDir() + path.replaceAll("\\\\", "/"));
                if (file.exists()) {
                    file.delete();
                }
            }
        }

        //执行完之后，设置可以显示引导页，等待下次安装覆盖的时候来显示
//        PreferenceUtil.getInstance().setShowGuidePage(true);

        if (code.equals("Y")) {// 检查更新成功
            if (count != 0) {// 有更新
                LogUtils.d("changeFileDao.addItem" + TimeUtil.now());

                zipUpdateH5(changezip);
            } else {// 没有更新
                if (AppConfig.DEBUG)
                    Log.e("zzy", "没有更新，直接跳转:" + PreferenceUtil.getInstance(context).getFileUpdateTime());

                jump();
            }
        } else {
            if (AppConfig.DEBUG)
                Log.e("zzy", "数据异常！code不为Y！");
            // 就算数据异常都是跳过
            jump();
        }
    }


    private void showMessageUsehandler(String showString) {
        Message msg = Message.obtain();
        msg.what = UPDATE_MESSAGE;
        msg.obj = showString;
        mHandler.sendMessage(msg);

    }


    /**
     * 跳转
     */
    private void jump() {
        PreferenceUtil.getInstance(context).setDownloadingFile(false);// 文件下载中状态
        // mHandler.sendEmptyMessage(HIDE_MESSAGE);
        // 获取当前时间，然后进行2秒之后进入
        long endTime = System.currentTimeMillis();
        long dTime = endTime - startTime;
        Log.i("zzy", "dTime:" + dTime);
        if (dTime < 2000) {
            mmHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startJump();
                }
            }, (2000 - dTime));
        } else {
            startJump();
        }


    }

    private void startJump() {
        //判断这个目录下有没有引导页，没有就直接进入主界面
        Intent mItent = getIntent();
        mItent.setClass(this, XinyuDemoActivity.class);
        startActivity(mItent);
        finish();

    }

    // 检测是否是第一次开启app
    public void checkIsFirst() {
        mmHandler.sendEmptyMessageDelayed(SWITCH_TWOACTIVITY, 0);
    }

    private Handler mmHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SWITCH_TWOACTIVITY:
                    Intent intent = new Intent(LoadingActivity.this, XinyuDemoActivity.class);
                    startActivity(intent);
                    break;

            }
            LoadingActivity.this.finish();

        }


    };


    /**
     * 用zip的方式更新html5
     *
     * @param changezip zip的名字
     */
    private void zipUpdateH5(String changezip) {
        try {
            final String time = changezip.split("-")[1].split("\\.")[0];

            if (AppConfig.DEBUG) {
                Log.i("zzy", "changezip:" + changezip);
                Log.i("zzy", "time:" + time);
            }

            if (!"".equals(changezip)) {
                changezip = changezip.replaceAll("\\\\", "/");
                if (changezip.startsWith("/")) {
                    changezip = changezip.substring(1);
                }
            } else {

                jump();
                return;
            }
            //拼接下载html的zip包的url
            String url = configInfo.get("xversion-download-url") + changezip;
            OkHttpUtils.get().url(url).build().execute(new FileCallBack(FileUtil.getInstance(context).getSDCardRoot() + AppConstants.XINYUSOFT_CACHE, "updatehtml5.zip") {
                @Override
                public void onError(Call call, Exception e, int i) {
                    Toast.makeText(context, "下载失败，请稍候再试！", Toast.LENGTH_SHORT).show();
                    jump();
                }

                @Override
                public void inProgress(float progress, long total, int id) {
                    float allFileSize = (float) (Math.round(((float) (total * 1.0 / 1000000)) * 10) / 10.0);
                    float currentFileSize = (float) (Math.round(((float) (progress * total * 1.0 / 1000000)) * 10) / 10.0);
                    showMessageUsehandler("请稍等..." + currentFileSize + "M /" + allFileSize + "M");
                }

                @Override
                public void onResponse(File file, int i) {
                    try {
                        ZIPUtils.unzipTest(file.getAbsolutePath(), getApplicationContext().getFilesDir().getAbsolutePath());
                        PreferenceUtil.getInstance(context).setFileUpdateTime(time);// 设置上次更新时间（设置最新版本号）
                        // 修改所有的文件为只读权限 （zip下载不能单个的设置只读）
                        // FileUtil.getInstance().setReadOnlyFiles(getFilesDir().getAbsolutePath());
                        //是第一次安装，不需要改变状态。
                        PreferenceUtil.getInstance(context).setHtmlUpdate(true);
                        jump();
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (AppConfig.WIRTE_SDCARD) {
//                            Write2SDCard.getInstance().writeMsg(e.toString());
                        }

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            if (AppConfig.DEBUG) {
                Log.e("amtf", "e.toString():" + e.toString());
            }
            jump();
        }

    }


}
