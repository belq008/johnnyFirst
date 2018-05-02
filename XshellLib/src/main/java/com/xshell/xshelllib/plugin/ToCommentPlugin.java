package com.xshell.xshelllib.plugin;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.xshell.xshelllib.logutil.LogUtils;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;


public class ToCommentPlugin extends CordovaPlugin {


    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {

        if ("toCommentOn".equals(action)) {
            try{
                LogUtils.e("huang","toCommentOn");
                Uri uri = Uri.parse("market://details?id="+cordova.getActivity().getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                cordova.getActivity().startActivity(intent);
            }catch(ActivityNotFoundException e){
                Toast.makeText(cordova.getActivity(), "Couldn't launch the market !", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return false;
    }




}
