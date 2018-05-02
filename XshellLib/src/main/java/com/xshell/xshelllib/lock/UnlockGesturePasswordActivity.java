//package com.xshell.xshelllib.lock;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.os.Handler;
//import android.support.v4.content.LocalBroadcastManager;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.view.WindowManager;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.xshell.xshelllib.R;
//import com.xshell.xshelllib.application.AppContext;
//import com.xshell.xshelllib.ui.InputPasswordActivity;
//import com.xshell.xshelllib.utils.PreferenceUtil;
//import com.xshell.xshelllib.view.DSAvatarImageView;
//
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URL;
//import java.util.List;
//
//
//public class UnlockGesturePasswordActivity extends Activity {
//
//    private int mFailedPatternAttemptsSinceLastTimeout = 0;
//    private CountDownTimer mCountdownTimer = null;
//    private Handler mHandler = new Handler();
//    private TextView mHeadTextView;
//    private TextView mForgetTextView;
//    private Animation mShakeAnim;
//
//    private Toast mToast;
//    private String userLogoUrl;
//    //是否退出应用程序
//    private boolean isExit;
//    //是否是验证，如果是验证，按返回键是不需要退出程序
//    private boolean isLogin;
//
//
//    private void showToast(CharSequence message) {
//        if (null == mToast) {
//            mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
//            mToast.setGravity(Gravity.CENTER, 0, 0);
//        } else {
//            mToast.setText(message);
//        }
//
//        mToast.show();
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        //去除title
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //去掉Activity上面的状态栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        super.onCreate(savedInstanceState);
////        FulStatusBarUtil.setcolorfulStatusBar(this,getResources().getColor(R.color.black));
//        setContentView(R.layout.xinyusoft_gesturepassword_unlock);
////		if(AppConfig.DEBUG)
////		Log.d("zzy", "nameonCreate:"+NewBrowserCollector.onlyNames.get(NewBrowserCollector.onlyNames.size()-1));
//
//        mLockPatternView = (LockPatternView) this.findViewById(R.id.gesturepwd_unlock_lockview);
//        mLockPatternView.setOnPatternListener(mChooseNewLockPatternListener);
//        mLockPatternView.setTactileFeedbackEnabled(true);
//        mHeadTextView = (TextView) findViewById(R.id.gesturepwd_unlock_text);
//        mForgetTextView = (TextView) findViewById(R.id.gesturepwd_unlock_forget);
//        mShakeAnim = AnimationUtils.loadAnimation(this, R.anim.xinyusoft_shake_x);
//
//        mForgetTextView.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent();
//                intent.setAction(InputPasswordActivity.BR_FORGET_PWD);
//                LocalBroadcastManager.getInstance(UnlockGesturePasswordActivity.this).sendBroadcast(intent);
//                finish();
//
//            }
//        });
//        TextView title = (TextView) findViewById(R.id.gesturepwd_creat_title);
//        TextView otherLogin = (TextView) findViewById(R.id.gesturepwd_other_login);
//        int createLock = getIntent().getIntExtra("createLock", -1);
//        if (createLock == 0) {
//            DSAvatarImageView uer_logo = (DSAvatarImageView) findViewById(R.id.iv_user_logo);
//            TextView user_phone = (TextView) findViewById(R.id.iv_user_phone);
//            if (getIntent().hasExtra("phoneNum")) {
//                String phoneNum = getIntent().getStringExtra("phoneNum");
//                String substring1 = phoneNum.substring(0, 3);
//                String substring2 = phoneNum.substring(7, 11);
//                user_phone.setText(substring1+"****"+substring2);
//            }
//
//            if (getIntent().hasExtra("imageURL")) {
//                userLogoUrl = getIntent().getStringExtra("imageURL");
//                //先去SharedPreferences 里面看这个是不是一样的url。
//                String userUrl = PreferenceUtil.getInstance().getUserLogoUrl();
//                File file = new File(getFilesDir() + "/userLogo.jpg");
//                if (userLogoUrl.equals(userUrl) && file.exists()) {  //代表一样
//                    try {
//                        BitmapDrawable imageDrawable = getImageDrawable(file.toString());
//                        uer_logo.setImageDrawable(imageDrawable);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    DownloadImageTask task = new DownloadImageTask(uer_logo);
//                    task.execute(userLogoUrl);
//                }
//            }
//
//            //其他账号密码登录，　实际的方式是跳过的方式
//            otherLogin.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent();
//                    intent.setAction(InputPasswordActivity.BR_SKIP);
//                    LocalBroadcastManager.getInstance(UnlockGesturePasswordActivity.this).sendBroadcast(intent);
////                    if (NewBrowserCollector.onlyNames.size() > 0) {
////                        NewBrowserCollector.onlyNames.remove(NewBrowserCollector.onlyNames.size() - 1);
////                    }
//                    //最后清除手势密码
//                    AppContext.APPCONTEXT.getLockPatternUtils().clearLock();
//                    finish();
//                }
//            });
//            title.setText("手势密码登录");
//            isLogin = true;
//        } else {
//            title.setText("验证手势密码");
//            otherLogin.setVisibility(View.INVISIBLE);
//            mForgetTextView.setVisibility(View.INVISIBLE);
//            isLogin = false;
//        }
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (!AppContext.APPCONTEXT.getLockPatternUtils().savedPatternExists()) {
//            Intent intent = new Intent(this, GuideGesturePasswordActivity.class);
//            intent.putExtra("functionName", getIntent().getStringExtra("functionName"));
//            startActivity(intent);
//            finish();
//        }
//    }
//
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        //禁止返回键
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            finish();
//            //android.os.Process.killProcess(android.os.Process.myPid());
//            //System.exit(0);
//            isExit = true;
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (mCountdownTimer != null)
//            mCountdownTimer.cancel();
//        if (isExit && isLogin) {
//            android.os.Process.killProcess(android.os.Process.myPid());
//        }
//
//    }
//
//    private Runnable mClearPatternRunnable = new Runnable() {
//        public void run() {
//            mLockPatternView.clearPattern();
//        }
//    };
//
//    protected LockPatternView.OnPatternListener mChooseNewLockPatternListener = new LockPatternView.OnPatternListener() {
//
//        public void onPatternStart() {
//            mLockPatternView.removeCallbacks(mClearPatternRunnable);
//            patternInProgress();
//        }
//
//        public void onPatternCleared() {
//            mLockPatternView.removeCallbacks(mClearPatternRunnable);
//        }
//
//        public void onPatternDetected(List<LockPatternView.Cell> pattern) {
//            if (pattern == null)
//                return;
//            if (AppContext.APPCONTEXT.getLockPatternUtils().checkPattern(pattern)) {
//                mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
//
//                Intent i = getIntent();
//                int createLock = i.getIntExtra("createLock",0);
//
//                if (createLock == 1) {
//                    Intent intent = new Intent(UnlockGesturePasswordActivity.this, GuideGesturePasswordActivity.class);
//                    // 打开新的Activity
//                    intent.putExtra("createLock", createLock);
//                    intent.putExtra("functionName", i.getStringExtra("functionName"));
//                    startActivity(intent);
//                    finish();
//
//                } else {
//                    //之后你想干啥就干啥
//                    Intent intent = new Intent();
//                    intent.setAction("unlock");
//                    intent.putExtra("functionName", i.getStringExtra("functionName"));
//                    LocalBroadcastManager.getInstance(UnlockGesturePasswordActivity.this).sendBroadcast(intent);
////                    if (NewBrowserCollector.onlyNames.size() > 0) {
////                        NewBrowserCollector.onlyNames.remove(NewBrowserCollector.onlyNames.size() - 1);
////                    }
//                    finish();
//                }
//            } else {
//                mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
//                if (pattern.size() >= LockPatternUtils.MIN_PATTERN_REGISTER_FAIL) {
//                    mFailedPatternAttemptsSinceLastTimeout++;
//                    int retry = LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT
//                            - mFailedPatternAttemptsSinceLastTimeout;
//                    if (retry >= 0) {
//                        if (retry == 0) {
//                            //showToast("您已5次输错密码，请30秒后再试");
//                            Intent intent = new Intent();
//                            intent.setAction(InputPasswordActivity.BR_FORGET_PWD);
//                            LocalBroadcastManager.getInstance(UnlockGesturePasswordActivity.this).sendBroadcast(intent);
//                            finish();
//                        }
//                        mHeadTextView.setText("密码错误，还可以再输入" + retry + "次");
//                        mHeadTextView.setTextColor(Color.RED);
//                        mHeadTextView.setVisibility(View.VISIBLE);
//                        mHeadTextView.startAnimation(mShakeAnim);
//                    }
//
//                } else {
//                    showToast("输入长度不够，请重试");
//                }
//
//                if (mFailedPatternAttemptsSinceLastTimeout >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT) {
//                    mHandler.postDelayed(attemptLockout, 2000);
//                } else {
//                    mLockPatternView.postDelayed(mClearPatternRunnable, 2000);
//                }
//            }
//        }
//
//        public void onPatternCellAdded(List<Cell> pattern) {
//
//        }
//
//        private void patternInProgress() {
//        }
//    };
//    Runnable attemptLockout = new Runnable() {
//
//        @Override
//        public void run() {
//            mLockPatternView.clearPattern();
//            mLockPatternView.setEnabled(false);
//            mCountdownTimer = new CountDownTimer(
//                    LockPatternUtils.FAILED_ATTEMPT_TIMEOUT_MS + 1, 1000) {
//
//                @Override
//                public void onTick(long millisUntilFinished) {
//                    int secondsRemaining = (int) (millisUntilFinished / 1000) - 1;
//                    if (secondsRemaining > 0) {
//                        mHeadTextView.setText(secondsRemaining + " 秒后重试");
//                    } else {
//                        mHeadTextView.setText("请绘制手势密码");
//                        mHeadTextView.setTextColor(Color.WHITE);
//                    }
//
//                }
//
//                @Override
//                public void onFinish() {
//                    mLockPatternView.setEnabled(true);
//                    mFailedPatternAttemptsSinceLastTimeout = 0;
//                }
//            }.start();
//        }
//    };
//
//    class DownloadImageTask extends AsyncTask<String, ImageView, Drawable> {
//
//        private ImageView urlImageView;
//
//        public DownloadImageTask(ImageView urlImageView) {
//            this.urlImageView = urlImageView;
//        }
//
//        protected Drawable doInBackground(String... urls) {
//            return loadImageFromNetwork(urls[0]);
//        }
//
//        protected void onPostExecute(Drawable result) {
//            if (result != null) {
//                urlImageView.setImageDrawable(result);
//            }
//        }
//
//        private Drawable loadImageFromNetwork(String imageUrl) {
//            Drawable drawable = null;
//            try {
//                drawable = Drawable.createFromStream(new URL(imageUrl).openStream(), "image.jpg");
//                drawableToFile(drawable, getFilesDir() + "/userLogo.jpg");
//                PreferenceUtil.getInstance().setUserLogoUrl(userLogoUrl);
//            } catch (IOException e) {
//                Log.d("test", e.getMessage());
//            }
//
//
//            return drawable;
//        }
//    }
//
//    public void drawableToFile(Drawable drawable, String path) {
//        //Log.i(TAG, "drawableToFile:"+path);
//        File file = new File(path);
//        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
//        byte[] bitmapdata = bos.toByteArray();
//
//        //write the bytes in file
//        FileOutputStream fos;
//        try {
//            fos = new FileOutputStream(file);
//            fos.write(bitmapdata);
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//
//    public BitmapDrawable getImageDrawable(String path) throws IOException {
//        //打开文件
//        File file = new File(path);
//        if (!file.exists()) {
//            return null;
//        }
//
//        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//        byte[] bt = new byte[1024];
//
//        //得到文件的输入流
//        InputStream in = new FileInputStream(file);
//
//        //将文件读出到输出流中
//        int readLength = in.read(bt);
//        while (readLength != -1) {
//            outStream.write(bt, 0, readLength);
//            readLength = in.read(bt);
//        }
//
//        //转换成byte 后 再格式化成位图
//        byte[] data = outStream.toByteArray();
//        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// 生成位图
//        BitmapDrawable bd = new BitmapDrawable(bitmap);
//
//        return bd;
//    }
//}
