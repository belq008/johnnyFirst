package com.xshell.xshelllib.plugin;

import com.xshell.xshelllib.aesencrypt.AES;
import com.xshell.xshelllib.logutil.LogUtils;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;

/**
 * 加密解密插件
 * 
 * @author zzy
 *
 */
public class EncryptPlugin extends CordovaPlugin {
	@Override
	public boolean execute(String action, CordovaArgs args, final CallbackContext callbackContext) throws JSONException {
		
		final String content = args.getString(0);
		final AES mAes = new AES(args.getString(1));
		if("aes256Encrypt".equals(action)) {  //aes256Encrypt加密
			new Thread(new Runnable() {
				@Override
				public void run() {
					byte[] mBytes = null;
					try {
						mBytes = content.getBytes("UTF-8");
					} catch (Exception e) {
						e.printStackTrace();
					}
					callbackContext.success(mAes.encrypt(mBytes));
					LogUtils.e("huang","aes256Encrypt加密:"+mAes.encrypt(mBytes));
				}
			}).start();
			return true;
		} else if("aes256Decrypt".equals(action)) { //aes256Decrypt解密
			LogUtils.e("huang","aes256Decrypt解密");
			new Thread(new Runnable() {
				@Override
				public void run() {
					callbackContext.success(mAes.decrypt(content));
				}
			}).start();
			return true;
		}
		
		return false;
	}
}
