package com.xshell.xshelllib.plugin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.Call;

import static android.app.Activity.RESULT_CANCELED;

/**
 * Created by zzy on 2017/2/23.
 * 在相册或者拍照获取图片并且上传
 */

public class SelectPicturePlugin extends CordovaPlugin {
    private Context context;
    private Activity activity;

    private String[] items = new String[]{"图库", "拍照"};
    /* 头像名称 */
    private static final String IMAGE_FILE_NAME = "face.jpg";
    /* 请求码 */
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int SELECT_PIC_KITKAT = 3;
    private static final int CAMERA_REQUEST_CODE = 1;
    private String uploadUrl;
    private CallbackContext callbackContext;
    private static final int PICTURE_REQUESTCODE = 8;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        context = cordova.getActivity();
        activity = cordova.getActivity();
    }

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        uploadUrl = args.getString(0);
        this.callbackContext = callbackContext;
        if (!cordova.hasPermission(Manifest.permission.RECORD_AUDIO)) {
            cordova.requestPermissions(this, PICTURE_REQUESTCODE, new String[]{Manifest.permission.RECORD_AUDIO});
        } else {
            showSettingFaceDialog();
        }
        return true;
    }

    private void showSettingFaceDialog() {
        new AlertDialog.Builder(context)
                .setTitle("图片来源")
                .setCancelable(true)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:// Local Image
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                intent.setType("image/*");
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                    cordova.startActivityForResult(SelectPicturePlugin.this, intent, SELECT_PIC_KITKAT);
                                } else {
                                    cordova.startActivityForResult(SelectPicturePlugin.this, intent, IMAGE_REQUEST_CODE);
                                }
                                break;
                            case 1:// Take Picture
                                Intent intentFromCapture = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                intentFromCapture.putExtra(
                                        MediaStore.EXTRA_OUTPUT,
                                        Uri.fromFile(new File(Environment
                                                .getExternalStorageDirectory(),
                                                IMAGE_FILE_NAME)));

                                cordova.startActivityForResult(SelectPicturePlugin.this, intentFromCapture,
                                        CAMERA_REQUEST_CODE);
                                break;
                        }
                    }
                })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("zzy", "-------------resultCode-------------------:" + resultCode);
        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    uploadPhoto(data.getData());
                    break;
                case SELECT_PIC_KITKAT:
                    uploadPhoto(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    File tempFile = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME);
                    uploadPhoto(Uri.fromFile(tempFile));
                    break;
            }
        } else {  //取消的时候，
            errorResult(0, "取消");
        }

    }

    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);

        Log.i("zzy", ":");
    }

    /**
     * 上传图片
     *
     * @param uri 图片的uri
     */
    private void uploadPhoto(Uri uri) {
        String path = getPath(activity, uri);
        if (path == null) {
            errorResult(-1, "获取图片失败");
            return;
        }

        JSONObject object = new JSONObject();
        try {
            object.put("code", 0);
            PluginResult result = new PluginResult(PluginResult.Status.OK, object);
            result.setKeepCallback(true);
            callbackContext.sendPluginResult(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        File file = new File(path);
        RequestCall build = OkHttpUtils.post()//
                .addFile("mFile", file.getName(), file)//
                .url(uploadUrl)
                .build();
        build.execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
            }

            @Override
            public void onResponse(String s, int i) {
                s = s.substring(s.indexOf("src") + 5, s.length() - 6);
                JSONObject successObj = new JSONObject();
                try {
                    successObj.put("result", s);
                    successObj.put("code", 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                PluginResult result = new PluginResult(PluginResult.Status.OK, successObj);
                callbackContext.sendPluginResult(result);
            }
        });
    }


    private void errorResult(int errCode, String errStr) {
        JSONObject object = new JSONObject();
        try {
            object.put("errCode", errCode);
            object.put("errStr", errStr);

            PluginResult result = new PluginResult(PluginResult.Status.ERROR, object);
            result.setKeepCallback(true);
            callbackContext.sendPluginResult(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //以下是关键，原本uri返回的是file:///...来着的，android4.4返回的是content:///...
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
        super.onRequestPermissionResult(requestCode, permissions, grantResults);
        if (requestCode == PICTURE_REQUESTCODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(cordova.getActivity(), "授权!", Toast.LENGTH_SHORT).show();
                // Permission Granted
                showSettingFaceDialog();
            } else {
                // Permission Denied
                Toast.makeText(cordova.getActivity(), "由于您拒绝了授权，操作将无法进行！", Toast.LENGTH_SHORT).show();
                errorResult(-1, "拒绝授权");
            }
        }

    }
}
