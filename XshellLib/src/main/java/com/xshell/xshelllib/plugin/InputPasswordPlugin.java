package com.xshell.xshelllib.plugin;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.xshell.xshelllib.ui.InputPasswordActivity;
import com.xshell.xshelllib.ui.XinyuHomeActivity;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;

/**
 * Created by zzy on 2016/8/9.
 *
 */
public class InputPasswordPlugin extends CordovaPlugin {

    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);

    }

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        try {
            if ("inputPassword".equals(action)) {
                String callbackName = args.getString(0);
                ((XinyuHomeActivity)cordova.getActivity()).setIsAgainRegister(false);
                Intent intent = new Intent(cordova.getActivity(),InputPasswordActivity.class);
                intent.setAction(InputPasswordActivity.BR_INPUT_PASSWORD);
                intent.putExtra("callbackName", callbackName);
                //LocalBroadcastManager.getInstance(cordova.getActivity()).sendBroadcast(intent);
                cordova.getActivity().startActivity(intent);
                return true;
            } else if("passwordState".equals(action)){
                int state = args.getInt(0);
                if (state == 1) {
                    ((XinyuHomeActivity)cordova.getActivity()).setIsAgainRegister(true);
                }
                Intent intent = new Intent();
                intent.setAction(InputPasswordActivity.BR_PASSWORD_STATE);
                intent.putExtra("state", state);
                LocalBroadcastManager.getInstance(cordova.getActivity()).sendBroadcast(intent);
                return true;
            }

        } catch (Exception e) {
            callbackContext.error(e.getMessage());
            return false;
        }
        return false;
    }
}
