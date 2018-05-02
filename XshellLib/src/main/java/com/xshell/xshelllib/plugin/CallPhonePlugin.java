package com.xshell.xshelllib.plugin;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

import com.xshell.xshelllib.view.IOSDialog;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;

/**
 * Created by zzy on 2016/10/12.
 * <p>
 * 打电话插件
 */
public class CallPhonePlugin extends CordovaPlugin {

    private CordovaArgs args;

    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
        super.onRequestPermissionResult(requestCode, permissions, grantResults);
        if (requestCode == 9) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(cordova.getActivity(), "授权!", Toast.LENGTH_SHORT).show();
                // Permission Granted
                callPhone(args);
            } else {
                // Permission Denied
                Toast.makeText(cordova.getActivity(), "由于您拒绝了授权，录音将无法进行！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean execute(String action, final CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        if ("call".equals(action)) {
            this.args = args;
            if (!cordova.hasPermission(Manifest.permission.CALL_PHONE)) {
                cordova.requestPermissions(this, 9, new String[]{Manifest.permission.CALL_PHONE});
            } else {
               // callPhone(args);
                callPhone1(args);
            }
            return true;
        }
        return false;
    }

    private void callPhone(final CordovaArgs args) throws JSONException {
        final AlertDialog.Builder builder = new AlertDialog.Builder(cordova.getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setMessage(args.getString(0));
        builder.setCancelable(false);
        builder.setPositiveButton("呼叫", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String number = null;
                try {
                    number = args.getString(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
                if (ActivityCompat.checkSelfPermission(cordova.getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                cordova.getActivity().startActivity(intent);

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void callPhone1(final CordovaArgs args) throws JSONException {
       final IOSDialog dialog =  new IOSDialog(cordova.getActivity()).builder();
        dialog.setMsg(args.getString(0))
                .setPositiveButton("呼叫", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        String number = null;
                        try {
                            number = args.getString(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
                        if (ActivityCompat.checkSelfPermission(cordova.getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        cordova.getActivity().startActivity(intent);

                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        }).show();
    }


}
