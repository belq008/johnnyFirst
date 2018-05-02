package com.xshell.xshelllib.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xshell.xshelllib.R;
import com.xshell.xshelllib.application.AppConstants;
import com.xshell.xshelllib.utils.PreferenceUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class TestFragment extends Fragment {
    private static final String KEY_CONTENT = "TestFragment:Content";
    private static final String TAG = "TestFragment";
    private static Context context;
    public static String imageurl;
    private int index;
    private File[] files;
    RelativeLayout fragment_gui;

    public static TestFragment newInstance(int position) {
        TestFragment fragment = new TestFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.TAG_INDEX, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.xinyusoft_fragment_gui_view, container, false);

        fragment_gui = (RelativeLayout) view.findViewById(R.id.RelativeLayout1);
        context = getActivity();
        imageurl = context.getFilesDir().getAbsolutePath() + "/imagepage/android";
        Bundle b = getArguments();
        index = b.getInt(AppConstants.TAG_INDEX);
        ImageView iv = (ImageView) view.findViewById(R.id.iv_guide_bg);
        fragment_gui.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index == files.length - 1) {
                    Intent intent = new Intent();

                    try {
                        intent.setClass(context, Class.forName(PreferenceUtil.getInstance(context).getHomeActivityPath()));
                        context.startActivity(intent);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    getActivity().finish();
                }
            }
        });
        ImageDownloader load = new ImageDownloader();
        File file = new File(imageurl);
        files = file.listFiles();

        if (files != null) {
            load.download(files[index].getPath(), iv);
            sendBroadcast();
        }
        return view;
    }

    public class ImageDownloader {

        public void download(String url, ImageView imageView) {
            BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
            task.execute(url, imageView);
        }
    }

    class BitmapDownloaderTask extends AsyncTask {
        Hodler h = new Hodler();
        private final WeakReference<ImageView> imageViewReference; // 使用WeakReference解决内存问题

        public BitmapDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Hodler doInBackground(Object... params) { // 实际的下载线程，内部其实是concurrent线程，所以不会阻塞

            h.setPath((String) params[0]);
            h.iv = (ImageView) params[1];

            try {
                File file = new File(h.getPath());
                InputStream is = new FileInputStream(file);
                h.setBm(BitmapFactory.decodeStream(is));
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // sendBroadcast();

            return h;
        }

        @Override
        protected void onPostExecute(Object bitmap) { // 下载完后执行的
            Hodler h = (Hodler) bitmap;
            Bitmap bm = h.getBm();
            BitmapDrawable bd = new BitmapDrawable(bm);
            fragment_gui.setBackgroundDrawable(bd);
            // sendBroadcast();
            // h.iv.setImageBitmap((Bitmap)h.getBm());
            // //下载完设置imageview为刚才下载的bitmap对象
        }
    }

    private void sendBroadcast() {
        int type = 1;
        if (index == files.length - 1) {
            type = 0;
        } else {
            type = 1;
        }
        Intent intent = new Intent();
        intent.setAction(AppConstants.GUIDE_BROADCAST_NAME);
        intent.putExtra("type", type);
        intent.putExtra("posi", index);
        context.sendBroadcast(intent);
    }

    public class Hodler {
        String path;
        Bitmap bm;
        ImageView iv;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public Bitmap getBm() {
            // sendBroadcast();
            return bm;
        }

        public void setBm(Bitmap bm) {
            this.bm = bm;
        }

    }

}
