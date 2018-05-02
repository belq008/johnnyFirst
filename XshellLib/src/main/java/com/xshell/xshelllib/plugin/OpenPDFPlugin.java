package com.xshell.xshelllib.plugin;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.xshell.xshelllib.ui.ReadPdfActivity;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;

import java.io.File;

/**
 * 打开pdf
 */
public class OpenPDFPlugin extends CordovaPlugin {

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {

        String pdfUrl = args.getString(0);
        if (!"".equals(pdfUrl) && null != pdfUrl) {
//            Log.e("huanghu",args.getString(1)+"=="+args.getString(2)+"==path:"+Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+pdfUrl);
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+pdfUrl;
           // Intent intent = getPdfFileIntent(path);
            Intent intent = new Intent(cordova.getActivity(), ReadPdfActivity.class);
            intent.putExtra("emailAddr",args.getString(1));
            intent.putExtra("emailTitle",args.getString(2));
            intent.putExtra("path", path);
            cordova.getActivity().startActivity(intent);
            return true;
        }
        return false;
    }

    /**
     * Get PDF file Intent
     */
    public Intent getPdfFileIntent(String path) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(path));
        i.setDataAndType(uri, "application/pdf");
        return i;
    }




}
