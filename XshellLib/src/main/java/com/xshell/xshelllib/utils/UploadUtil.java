package com.xshell.xshelllib.utils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.File;

public class UploadUtil {
	
	/**
	 * @param uploadFile 上传文件
	 * @param url 上传url，测试地址 http://192.168.3.110:8080/jsupload/uploadAndroid
	 * @param requestCallBack 回调函数 onLoading(),onSuccess(),onFailure(),onStart()
	 */
	public static void upLoad(File uploadFile,String url,RequestCallBack<String> requestCallBack){
		// 设置请求参数的编码
        //RequestParams params = new RequestParams("GBK");
        RequestParams params = new RequestParams(); // 默认编码UTF-8

        //params.addQueryStringParameter("qmsg", "你好");
        //params.addBodyParameter("msg", "测试");

        // 添加文件
        params.addBodyParameter("file", uploadFile);
        //params.addBodyParameter("testfile", new File("/sdcard/test2.zip")); // 继续添加文件

        // 用于非multipart表单的单文件上传
        //params.setBodyEntity(new FileUploadEntity(new File("/sdcard/test.zip"), "binary/octet-stream"));

        // 用于非multipart表单的流上传
        //params.setBodyEntity(new InputStreamUploadEntity(stream ,length));

        HttpUtils http = new HttpUtils();

        // 设置返回文本的编码， 默认编码UTF-8
        //http.configResponseTextCharset("GBK");

        // 自动管理 cookie
//        http.configCookieStore(preferencesCookieStore);

        http.send(HttpRequest.HttpMethod.POST,
        		url,
                params,requestCallBack);
	}
}
