//package com.xshell.xshelllib.view;
//
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.graphics.drawable.GradientDrawable;
//import android.os.Bundle;
//import android.os.Handler;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.KeyEvent;
//
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewTreeObserver;
//import android.view.WindowManager;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.xshell.xshelllib.R;
//import com.xshell.xshelllib.utils.SharedPreferencesUtils;
//
//import org.json.JSONObject;
//
//import java.util.List;
//
//import static android.content.Context.INPUT_METHOD_SERVICE;
//
///**
// * Created by hh on 2017/11/10.
// * 输入框的dialog
// */
//
//public class SoftKeyboardInputCommentsDialog extends Dialog {
//    private Context context;
//    private EditText et_editText_comment;
//    private TextView tv_fasong;
//    boolean isFlay = false;
//    private LinearLayout ll_input;
//    private String str;
//    private String title;
//    private String boxcolor;
//    private String btncolor;
//    private String placetitle;
//    private String btnbordercolor;
//    private String titlecolor;
//    private String text;
//    private LinearLayout ll_button_border;
//
//    public SoftKeyboardInputCommentsDialog(Context context, int themeResId, String title1, String boxcolor1, String btncolor1, String placetitle1, String btnbordercolor1, String titlecolor1, String text1) {
//        super(context, themeResId);
//        this.context = context;
//        this.title = title1;
//        this.boxcolor = boxcolor1;
//        this.btncolor = btncolor1;
//        this.placetitle = placetitle1;
//        this.btnbordercolor = btnbordercolor1;
//        this.titlecolor = titlecolor1;
//        this.text = text1;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setCancelable(false);
//
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View view = inflater.inflate(R.layout.soft_keyboard_comment_dialog, null);
//        setContentView(view);
//        getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);//让布局宽度充满
//        et_editText_comment = (EditText) findViewById(R.id.et_editText_comment);
//        et_editText_comment.setFocusable(true);
//        et_editText_comment.setFocusableInTouchMode(true);
//        et_editText_comment.requestFocus();
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//        tv_fasong = (TextView) findViewById(R.id.tv_fasong);
//        ll_input = (LinearLayout) findViewById(R.id.ll_input);
//        ll_button_border = (LinearLayout) findViewById(R.id.ll_button_border);
//        ll_input.setBackgroundColor(Color.parseColor(boxcolor));
//        et_editText_comment.setHint(placetitle);
//        tv_fasong.setText(title);
//        if (!"".equals(text)) {
//            et_editText_comment.setText(text);
//        }
//        et_editText_comment.setSelection(et_editText_comment.getText().length());
//        if (!"".equals(et_editText_comment.getText().toString().trim())) {
//            setBorderColor(btnbordercolor, btncolor, titlecolor);
//        }
//
//        et_editText_comment.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//
//            //当键盘弹出隐藏的时候会 调用此方法。
//            @Override
//            public void onGlobalLayout() {
//                final InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
//                if (imm.hideSoftInputFromWindow(et_editText_comment.getWindowToken(), 0)) {
//                    imm.showSoftInput(et_editText_comment, 0);
//                }
//            }
//        });
//
//        //动态改变buttom字体颜色
//        et_editText_comment.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                str = s.toString();
//                if (!str.isEmpty()) {
////                    tv_fasong.setTextColor(context.getResources().getColor(R.color.black));
//                    setBorderColor(btnbordercolor, btncolor, titlecolor);
//                    tv_fasong.setText(title);
//                    // 如果设置了回调，则设置点击事件
//                    tv_fasong.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (!str.isEmpty()) {
//                                Intent intent = new Intent("inputThing");
//                                intent.putExtra("send", str);
//                                intent.putExtra("statusCode", 1);
//                                context.sendBroadcast(intent);
//                                Log.e("huanghu", "str:" + str);
//                                et_editText_comment.setText("");
//                                SharedPreferencesUtils.setParam(context, "Input_Value", et_editText_comment.getText().toString().trim());
//                                //  dismiss();
//                            } else {
//                                //Toast.makeText(context, "评论不能为空!", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                } else {
//                    setBorderColor("#cfcfcf", boxcolor, "#c6c6c6");
//                }
//            }
//        });
//
//        //按返回键关闭dialog
//        et_editText_comment.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if ((event.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_BACK)) {
//                    dismiss();
//                    return true;
//                }
//                return false;
//            }
//        });
//
//    }
//
//
//    private void setBorderColor(String btnbordercolor, String btncolor, String titlecolor) {
//    /*设置边框颜色和宽度*/
//        GradientDrawable drawable = new GradientDrawable();
//        drawable.setShape(GradientDrawable.RECTANGLE); // 画框
//        drawable.setStroke(2, Color.parseColor(btnbordercolor)); // 边框粗细及颜色
//        drawable.setCornerRadius(15);
//        drawable.setColor(Color.parseColor(btncolor));//边框内部颜色
//        ll_button_border.setBackgroundDrawable(drawable);
//        tv_fasong.setBackgroundDrawable(drawable);
//        tv_fasong.setTextColor(Color.parseColor(titlecolor));
//    }
//
//    // 点击空白区域 自动隐藏软键盘
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (null != this.getCurrentFocus()) {
//
//            Intent intent = new Intent("inputThing");
//            intent.putExtra("send", et_editText_comment.getText().toString().trim());
//            intent.putExtra("statusCode", -1);
//            context.sendBroadcast(intent);
//            /**
//             * 点击空白位置 隐藏软键盘
//             */
//            InputMethodManager mInputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
//            dismiss();
//            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
//        }
//        return super.onTouchEvent(event);
//    }
//
//    private OnSendClickListener mOnSendClickListener;
//
//    public void setOnItemClickListener(OnSendClickListener mOnSendClickListener) {
//        this.mOnSendClickListener = mOnSendClickListener;
//    }
//
//    public interface OnSendClickListener {
//        void onSendClickThingClick(String send);
//    }
//}