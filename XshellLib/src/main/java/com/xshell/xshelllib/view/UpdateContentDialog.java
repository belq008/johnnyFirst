package com.xshell.xshelllib.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xshell.xshelllib.R;
import com.xshell.xshelllib.utils.DensityUtil;

/**
 * Created by zzy on 2016/11/28.
 */

public class UpdateContentDialog extends Dialog {
    private Context context;
    private String url;

    public UpdateContentDialog(Context context, String url) {
        super(context);
        this.context = context;
        this.url = url;
    }

    public UpdateContentDialog(Context context, int themeResId, String url) {
        super(context, themeResId);
        this.context = context;
        this.url = url;
    }

    public void myShow() {
        show();
        hide();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.xinyusoft_update_dialog, null);
        setContentView(view);

        final RelativeLayout rl_main = (RelativeLayout) findViewById(R.id.rl_dialog_main);

        final CusWebView webView = (CusWebView) findViewById(R.id.wv_update);
        webView.setRadius(DensityUtil.dip2px(context, 15));


//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        rl_main.setVisibility(View.VISIBLE);
//                        show();
//                    }
//                }, 1000);
//            }
//        });
        //String url = "file:///" + context.getFilesDir().getAbsolutePath() + File.separator + "upgradeinfo.html\n";
        webView.loadUrl(url);
        TextView btn = (TextView) findViewById(R.id.tv_update);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.destroy();
                dismiss();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              //  rl_main.setVisibility(View.VISIBLE);
                show();
            }
        }, 3000);
    }


}
