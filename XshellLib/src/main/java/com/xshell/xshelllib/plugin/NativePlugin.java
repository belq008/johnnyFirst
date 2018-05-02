package com.xshell.xshelllib.plugin;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;


import com.xshell.xshelllib.R;
import com.xshell.xshelllib.application.AppConstants;

import com.xshell.xshelllib.logutil.LogUtils;
import com.xshell.xshelllib.ui.NewBrowserActivity;
import com.xshell.xshelllib.ui.XinyuHomeActivity;
import com.xshell.xshelllib.utils.Log2FileUtil;
import com.xshell.xshelllib.utils.SharedPreferencesUtils;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;

/**
 * 原生插件，比如读写文件
 *
 * @author zzy
 */
public class NativePlugin extends CordovaPlugin {
    private static final String TAG = "NativePlugin";
    private Context context;
    private static HashMap<String, Integer> sounds = new HashMap<String, Integer>();// 音乐播放
    private File ROOT_FILE;
    private Handler handler = new Handler();
    private boolean isFlay = true;
    /**
     * 当打开一个页面时，需要调用回调函数名字
     */
    private String newBroserCallBcak;
    private CallbackContext mCallback;


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        context = cordova.getActivity();
        ROOT_FILE = new File(context.getFilesDir().getAbsolutePath());
    }

    @Override
    public boolean execute(String action, final JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.e("huang", "00000000----:" + action);
        mCallback = callbackContext;
    /*else if ("startToChangeOrientation".equals(action)) { // 横竖屏切换
                JSONObject jos = new JSONObject(result);
				final String callBackName = jos.getString("callbackName");
				String string = jos.getString("type");
				if("1".equals(jos.getString("type"))) {
					cordova.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
				} else {
					cordova.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
				}
				cordova.getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						webView.loadUrl("javascript:" + callBackName.trim() + "('{\"result\":1}')");
					}
				});
				return true;
			}*/
        if ("share".equals(action)) { // 分享功能
//				JSONObject jo = args.getJSONObject(0);
//				WeixinUtil.getInstance().weixinShare(jo, cordova.getActivity());
//				JSONObject json = new JSONObject();
//				json.put("result", 1);
//				callbackContext.success(json);
//				return true;
        } else if ("openWindow".endsWith(action)) { // 开启一个新的Activity
            if (isFlay) {
                isFlay = false;
                Log.e("huanghu", "NativePlugin:开启一个新的Activity");
                String url = args.getString(0);
//                if (jos.has("callbackName")) {
//                    newBroserCallBcak = jos.getString("callbackName");
//                }
//                Log.e("huanghu", "newBroserCallBcak:"+newBroserCallBcak);
//                String url = jos.getString("url");
                Intent intent1 = new Intent();
                Log2FileUtil.getInstance(context).saveCrashInfo2File("开启了一个新的Activity");
                intent1.setAction(AppConstants.ACTION_NEW_BROSER);
                intent1.putExtra("url", url);
                LocalBroadcastManager.getInstance(cordova.getActivity()).sendBroadcast(intent1);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isFlay = true;
                    }
                }, 500);
            }
            return true;
        } else if ("closeWindow".equals(action)) {
            // 关闭Activity
            try {
                if (!TextUtils.isEmpty(args.toString())) {
                    Log.e("huanghu", "args." + args.getJSONObject(0));
                    JSONObject jsonObject = args.getJSONObject(0);
                    boolean aBoolean = jsonObject.getBoolean("animate");
                    JSONObject aQuict = jsonObject.getJSONObject("value");
                    if (aQuict != null && !aQuict.toString().equals("null")) {
                        SharedPreferencesUtils.setParam(cordova.getActivity(), "quict", aQuict + "");
                        Log.e("huanghu", aQuict + ":999999999");
                    } else {
                        SharedPreferencesUtils.setParam(cordova.getActivity(), "quict", aQuict + "");
                        Log.e("huanghu", aQuict + ":666666666");
                    }
                    Log.e("huang", "关闭ActivityaBoolean：" + aQuict);
                    Intent intent1 = new Intent();
                    intent1.setAction(AppConstants.ACTION_CLOSE_BROSER);
                    intent1.putExtra("anima", aBoolean);
                    Log2FileUtil.getInstance(context).saveCrashInfo2File("关闭了一个新的Activity");
                    LocalBroadcastManager.getInstance(cordova.getActivity()).sendBroadcast(intent1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return true;
        } else if ("playSystemSoundsOrShack".equals(action)) {// 提示
            LogUtils.e("huang", "playSystemSoundsOrShack");
            // Intent intent = new Intent(AppConstants.ACTION_REMINDER);
            // LocalBroadcastManager.getInstance(cordova.getActivity()).sendBroadcast(intent);
            AudioManager audio = (AudioManager) cordova.getActivity().getSystemService(Context.AUDIO_SERVICE);
            int RingerMode = audio.getRingerMode();
            vibrator = (Vibrator) cordova.getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            // 注册音频通道
            cordova.getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = MediaPlayer.create(cordova.getActivity(), cordova.getActivity().getResources().getIdentifier("test", "raw", cordova.getActivity().getPackageName()));
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // 注册事件。当播放完毕一次后，重新指向流文件的开头，以准备下次播放。
            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.seekTo(0);
                }
            });
            switch (RingerMode) {
                case AudioManager.RINGER_MODE_NORMAL:// 铃声震动模式
                    vibrator.vibrate(new long[]{300, 500}, -1);
                    mediaPlayer.start();
                    break;
                case AudioManager.RINGER_MODE_SILENT:// 静音模式
                    break;
                case AudioManager.RINGER_MODE_VIBRATE:// 震动模式
                    vibrator.vibrate(new long[]{300, 500}, -1);
                    break;

                default:
                    break;
            }
            callbackContext.success();
            return true;

        } else if ("openLock".equals(action)) {
            ((XinyuHomeActivity) cordova.getActivity()).setIsAgainRegister(false);
            JSONObject jos = args.getJSONObject(0);
            int createLock = jos.getInt("isCreateLock");
//            String functionName = jos.getString("functionName");

            //NewBrowserCollector.addActivityOnlyName("UnlockGesturePasswordActivity1");

            ((XinyuHomeActivity) cordova.getActivity()).setIsAgainRegister(false);

//            Intent intent = new Intent(cordova.getActivity(), UnlockGesturePasswordActivity.class);
//            //createLock 为1则代表修改密码，0则是成功解锁
//            intent.putExtra("createLock", createLock);
//            //解锁成功后调用function的名字
////            intent.putExtra("functionName", functionName);
//            intent.putExtra("functionName", "functionName");
//            if (createLock == 0) {  //只有调用输入手势密码的时候，才需要这２个值。
//                if (jos.has("phoneNum")) {
//                    intent.putExtra("phoneNum", jos.getString("phoneNum"));
//                }
//                if (jos.has("imageURL")) {
//                    intent.putExtra("imageURL", jos.getString("imageURL"));
//                }
//            }
//            cordova.getActivity().startActivity(intent);
            return true;
        } else if ("resetLock".equals(action)) {
//            AppContext.APPCONTEXT.getLockPatternUtils().clearLock();
        } else if ("createWindow".equals(action) || "createNotFullWindow".equals(action)) {  //开启一个带title的activity
            Log.i("zzy", "------createWindow--------:");
            Intent in;
            in = new Intent(cordova.getActivity(), NewBrowserActivity.class);
            JSONObject jos = args.getJSONObject(0);
            String url = jos.getString("url");
            String title = jos.getString("title");
            in.putExtra("newBrowserUrl", url);
            in.putExtra("title", title);
            cordova.getActivity().startActivity(in);
            cordova.getActivity().overridePendingTransition(R.anim.xinyusoft_activity_right_in, R.anim.xinyusoft_activity_left_out);
            return true;
        }
        return false;
    }


    MediaPlayer mediaPlayer;
    Vibrator vibrator;

    @Override
    public void onPause(boolean multitasking) {
        super.onPause(multitasking);
        if (mediaPlayer != null) {
            mediaPlayer.release();

        }
        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);
        Log.e("huanghu", "=======================onResume");
//        if (newBroserCallBcak != null) {
        cordova.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                String quict = (String) SharedPreferencesUtils.getParam(cordova.getActivity(), "quict", "");
                Log.e("huanghu", "=======================+++++++++++++++++++++++" + quict + "===newBroserCallBcak:" + newBroserCallBcak);
                if ("null".equals(quict) || "".equals(quict)) {
//                        webView.loadUrl("javascript:" + newBroserCallBcak + "()");
                    mCallback.success("null");
                    newBroserCallBcak = null;
                    SharedPreferencesUtils.setParam(cordova.getActivity(), "quict", "");
                } else {
                    webView.loadUrl("javascript:" + newBroserCallBcak + "('" + quict + "')");
                    mCallback.success(quict);
                    newBroserCallBcak = null;
                    SharedPreferencesUtils.setParam(cordova.getActivity(), "quict", "");
                }
            }
        });
//        }

    }

    /**
     * 用来判断是否有我们开的第2个进程
     *
     * @return
     */
    public boolean isAppOnForeground() {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getPackageName();

        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName + ":xinyu_remote")) {
                if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;
                }

            }
        }

        return false;
    }

}
