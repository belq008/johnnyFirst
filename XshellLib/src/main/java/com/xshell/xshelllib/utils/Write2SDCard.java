package com.xshell.xshelllib.utils;

import android.content.Context;

import com.xshell.xshelllib.application.AppConfig;
import com.xshell.xshelllib.application.AppConstants;


import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Write2SDCard {
    private static Write2SDCard instance;
    private static String FILE_PATH = null;
    private static Object obj = new Object();
    private File dir;
    private String FILE_NAME;
    private FileOutputStream fos;
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private DateFormat nameFormat = new SimpleDateFormat("yyyy-MM-dd");
    static Context context;

    public static Write2SDCard getInstance(Context cxt) {
        context = cxt;
        if (instance == null) {
            synchronized (obj) {
                if (instance == null) {
                    instance = new Write2SDCard();
                }
            }
        }
        FILE_PATH = FileUtil.getInstance(context).getPathSDCard() + AppConstants.APP_ROOT_DIR_LOG;
        return instance;
    }

    private Write2SDCard() {
        dir = new File(FILE_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String[] temp = context.getPackageName().split("\\.");
        FILE_NAME = temp[temp.length - 1] + nameFormat.format(new Date()) + "-LOG.txt";
    }

    public void writeMsg(String msg) {
//        synchronized (obj) {
//            try {
//                fos = new FileOutputStream(FILE_PATH + FILE_NAME, true);
//                String time = formatter.format(new Date());
//                fos.write(("" + time + ":" + msg + "\n\r").getBytes());
//                fos.flush();
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void saveLog(String msg) {
        if (!AppConfig.WIRTE_SDCARD)
            return;
        writeMsg(msg);
    }
}
