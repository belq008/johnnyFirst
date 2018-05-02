package com.xshell.xshelllib.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xshell.xshelllib.R;
import com.xshell.xshelllib.utils.FulStatusBarUtil;

/**
 * 普通的密码输出框的界面
 */
public class InputPasswordActivity extends Activity {
    public static final String BR_INPUT_PASSWORD = "br_inputPassword";
    public static final String BR_PASSWORD_STATE = "br_passwordState";
    public static final String BR_FORGET_PWD = "br_forget_pwd";
    public static final String BR_SKIP = "BR_SKIP";
    private MyReceiver mReceiver;
    private Toast mToast;
    private EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        FulStatusBarUtil.setcolorfulStatusBar(this,getResources().getColor(R.color.black));
        setContentView(R.layout.xinyusoft_activity_input_password);

        TextView btn_ok = (TextView) findViewById(R.id.btn_ok);
        et_password = (EditText) findViewById(R.id.et_normal_password);
        TextView tv_forget_pwd = (TextView) findViewById(R.id.tv_forget_pwd);
        tv_forget_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(BR_FORGET_PWD);
                LocalBroadcastManager.getInstance(InputPasswordActivity.this).sendBroadcast(intent);
                finish();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(BR_INPUT_PASSWORD);
                intent.putExtra("password",et_password.getText().toString());
                intent.putExtra("callbackName", getIntent().getStringExtra("callbackName"));
                LocalBroadcastManager.getInstance(InputPasswordActivity.this).sendBroadcast(intent);
            }
        });
        initBroadcast();
    }

    private void initBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        // addAction
        intentFilter.addAction(InputPasswordActivity.BR_PASSWORD_STATE);
        mReceiver = new MyReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, intentFilter);
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //停止返回键
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BR_PASSWORD_STATE.equals(action)) {
                int state = intent.getIntExtra("state",0);
                if (state == 1) {  //等于1代表密码相同，关闭此界面
                    finish();
                } else {
                    showToast("登录密码错误！请重新输入");
                }
            }
        }
    }


    private void showToast(CharSequence message) {
        if (null == mToast) {
            mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(message);
        }

        mToast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }
}
