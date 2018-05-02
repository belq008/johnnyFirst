package com.xshell.xshelllib.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by huang on 2017/10/29.
 * 数据库管理器
 */
public class NewDataLocalityHuManager {

    public static final String TABLE_VALUE = "f_value";
    public static final String TABLE_KEY = "f_key";

    public static final String TAG = "NewDataLocalityHuManager";

    public SQLiteDatabase database;
    private static NewDataLocalityHuManager manager;
    private String table = "t_c_map";
    private String tablearr = "t_c_list";//删除数组的（旧需求）
    private Context mContext;
    private String dbName;
    private Handler handler;
    boolean isFlay = true;
    private DataLocalityDatabaseHelper helper;

    private NewDataLocalityHuManager(Context context, String dbName) {
        this.mContext = context;
        this.dbName = dbName;
    }

    public void openSQLData() {
        synchronized (TAG) {
            helper = new DataLocalityDatabaseHelper(mContext, dbName);
            // database = helper.getWritableDatabase();
        }
    }

    public static NewDataLocalityHuManager getInstance(Context context, String dbName) {
        // if (manager == null) {
        //   synchronized (TAG) {
        if (manager == null) {
            manager = new NewDataLocalityHuManager(context, dbName);
        }
        //  }
        //   }
        return manager;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    //1.取出map集合数据库
    public synchronized String getData(String key) throws JSONException {
        String value = "";
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getReadableDatabase();
            //开启事务
            db.beginTransaction();
            String sql = "select " + TABLE_VALUE + " from " + table + " where " + TABLE_KEY + "=" + "'" + key + "'";//根据key查value
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    value = cursor.getString(cursor.getColumnIndex(TABLE_VALUE));
                    cursor.moveToNext();
                }
            }
            cursor.close();
            //设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("huang", "存入数据：" + e.toString());
        } finally {
            try {
                if (null != db) {
                    //结束事务
                    db.endTransaction();
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    //2.字符串/map集合存入数据库
    public synchronized void insertData(String key, String value) throws JSONException {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getWritableDatabase();
            //开启事务
            db.beginTransaction();
            //判断是否有key存在语句
//            String sql = "select " + TABLE_VALUE + " from " + table + " where " + TABLE_KEY + "=" + "'" + key + "'";//根据key查询value
//            cursor = db.rawQuery(sql, null);
//            if (cursor.getCount() != 0) {  //有的话，更新
//                ContentValues contentValues = new ContentValues();
//                contentValues.put(TABLE_VALUE, value);
//                int update = db.update(table, contentValues, TABLE_KEY + "=?", new String[]{key});
//                Log.e("huang", "更新：" + update);
//            } else {
//                ContentValues contentValues = new ContentValues();
//                contentValues.put(TABLE_KEY, key);
//                contentValues.put(TABLE_VALUE, value);
//                long insert = db.insert(table, null, contentValues);
//                Log.e("huang", "insert：" + insert);
//            }
//            cursor.close();
             String sql = "insert or replace into t_c_map (f_key, f_value) values (?, ?)";
             db.execSQL(sql, new Object[] { key, value });
            //设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("huang", "存入数据：" + e.toString());
        } finally {
            try {
                if (null != db) {
                    //结束事务
                    db.endTransaction();
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //3.将数据存入list中，存入f_id,f_key和value到数据库中
    public synchronized String setList(String disKey, JSONArray arrIdLsit, JSONArray arrList) {
        String table = tablearr;
        if (arrIdLsit.length() != arrList.length()) {
            return null;
        }
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getWritableDatabase();
            //开启事务
            db.beginTransaction();
            for (int i = 0; i < arrIdLsit.length(); i++) {
                int id = arrIdLsit.getInt(i);
                String value = arrList.getString(i);
                //判断是否有key
//                String sql = "select " + "f_value" + " from " + table + " where " + "f_id" + "=" + "'" + id + "'" + " and " + "f_key" + "=" + "'" + disKey + "'";//根据key查询value
//                cursor = db.rawQuery(sql, null);
//                if (cursor.getCount() != 0) {  //有的话，更新
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put("f_value", value);
//                    int update = db.update(table, contentValues, "f_id" + "=? and " + "f_key" + "=?", new String[]{id + "", disKey});
//                    Log.e("huang", "setList新需求更新：" + update);
//                } else {
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put("f_id", id);
//                    contentValues.put("f_key", disKey);
//                    contentValues.put("f_value", value);
//                    long insert = db.insert(table, null, contentValues);
//                    Log.e("huang", "setList新需求insert：" + insert);//t_c_list
//                }
                String sql = "insert or replace into t_c_list (f_id, f_key, f_value) values (?, ?, ?)";
                db.execSQL(sql, new Object[] {id, disKey, value });
            }
            //设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("huang", "存入数据：" + e.toString());
        } finally {
            try {
                if (null != db) {
                    //结束事务
                    db.endTransaction();
                    db.close();
                }
//                if (cursor != null) {
//                    cursor.close();
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return table;
    }

    //4.更新list中某一条数据
    public synchronized String updateData(String disKey, int id, String arrListObject) {
        String table = tablearr;
        SQLiteDatabase db = null;
      //  Cursor cursor = null;

        try {
            db = helper.getWritableDatabase();
            //开启事务
            db.beginTransaction();
            //判断是否有key
//            String sql = "select " + "f_value" + " from " + table + " where " + "f_id" + "=" + "'" + id + "'" + " and " + "f_key" + "=" + "'" + disKey + "'";//根据key查询value
//            cursor = db.rawQuery(sql, null);
//            if (cursor.getCount() != 0) {  //有的话，更新
//                ContentValues contentValues = new ContentValues();
//                contentValues.put("f_value", arrListObject);
//                int update = db.update(table, contentValues, "f_id" + "=? and " + "f_key" + "=?", new String[]{id + "", disKey});
//                Log.e("huang", "updateData新需求更新：" + update);
//            } else {
//                ContentValues contentValues = new ContentValues();
//                contentValues.put("f_id", id);
//                contentValues.put("f_key", disKey);
//                contentValues.put("f_value", arrListObject);
//                long insert = db.insert(table, null, contentValues);
//                Log.e("huang", "updateData新需求insert：" + insert);
//            }
            String sql = "insert or replace into t_c_list (f_id, f_key, f_value) values (?, ?, ?)";
            db.execSQL(sql, new Object[] {id, disKey, arrListObject });
            //设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("huang", "存入数据：" + e.toString());
        } finally {
            try {
                if (null != db) {
                    //结束事务
                    db.endTransaction();
                    db.close();
                }
//                if (cursor != null) {
//                    cursor.close();
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return table;
    }

    //5.根据数组（id,key）来查询对应的value
    public synchronized JSONObject getDataByKeyID(String key, int id) {
        String table = tablearr;
        JSONObject jsonObject = null;
        SQLiteDatabase db = null;
        try {
            db = helper.getReadableDatabase();
            //开启事务
            db.beginTransaction();
            String sql = "select " + "f_value" + " from " + table + " where " + "f_id" + "=" + "'" + id + "'" + " and " + "f_key" + "=" + "'" + key + "'";//根据key查询value
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String value = cursor.getString(cursor.getColumnIndex("f_value"));
                    jsonObject = new JSONObject(value);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            //设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
            return jsonObject;
        } catch (Exception e) {
            Log.e("huang", "根据id找value：" + e.toString());
            return null;
        } finally {
            try {
                if (null != db) {
                    //结束事务
                    db.endTransaction();
                    if (db.isOpen()) {
                        db.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //6.根据数组（key）来查询对应的value
    public synchronized JSONArray getAllData(String key) {
        String table = tablearr;
        JSONArray jsonArray = new JSONArray();
        SQLiteDatabase db = null;
        try {
            db = helper.getReadableDatabase();
            //开启事务
            db.beginTransaction();
            String sql = "select " + "f_value" + " from " + table + " where " + "f_key" + "=" + "'" + key + "'";//根据key查询value
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String value = cursor.getString(cursor.getColumnIndex("f_value"));
                    JSONObject jsonObject = new JSONObject(value);
                    jsonArray.put(jsonObject);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            //设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
            return jsonArray;
        } catch (Exception e) {
            Log.e("huang", "根据id找value：" + e.toString());
            return null;
        } finally {
            try {
                if (null != db) {
                    //结束事务
                    db.endTransaction();
                    if (db.isOpen()) {
                        db.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //7.根据数组（key）来查询对应的value(倒序或正序)
    public synchronized JSONArray getAllDataByKey(String key, String descAndAsc) {
        String table = tablearr;
        JSONArray jsonArray = new JSONArray();
        SQLiteDatabase db = null;
        try {
            db = helper.getReadableDatabase();
            //开启事务
            db.beginTransaction();
            String sql = "select " + "f_value" + " from " + table + " where " + "f_key" + "=" + "'" + key + "'" + " order by f_id " + descAndAsc;//根据key查询value
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String value = cursor.getString(cursor.getColumnIndex("f_value"));
                    JSONObject jsonObject = new JSONObject(value);
                    jsonArray.put(jsonObject);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            //设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
            return jsonArray;
        } catch (Exception e) {
            Log.e("huang", "根据id找value：" + e.toString());
            return null;
        } finally {
            try {
                if (null != db) {
                    //结束事务
                    db.endTransaction();
                    if (db.isOpen()) {
                        db.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //8.获取最新的N条数据
    public synchronized JSONArray getNumDataByKey(String key, int num, String descAndAsc) {
        String table = tablearr;
        JSONArray jsonArray = new JSONArray();
        SQLiteDatabase db = null;
        try {
            db = helper.getReadableDatabase();
            //开启事务
            db.beginTransaction();
//            String sql = "select " + "f_value" + " from " + table + " where " + "f_key" + "=" + "'" + key + "'"+ " order by f_id " + descAndAsc;//根据key查询value
            String sql = "select " + "f_value" + " from " + table + " where " + "f_key" + "=" + "'" + key + "'" + " order by f_id " + descAndAsc + " limit " + num;//根据key查询value
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String value = cursor.getString(cursor.getColumnIndex("f_value"));
                    JSONObject jsonObject = new JSONObject(value);
                    jsonArray.put(jsonObject);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            //设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
            return jsonArray;
        } catch (Exception e) {
            Log.e("huang", "根据id找value：" + e.toString());
            return null;
        } finally {
            try {
                if (null != db) {
                    //结束事务
                    db.endTransaction();
                    if (db.isOpen()) {
                        db.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //9.根据单个id来查询此id的前或后的数量
    public synchronized JSONArray getNumDataByKeyID(String disKey, int id, int num, String direction) throws JSONException {
        String table = tablearr;
        JSONArray jsonArray = new JSONArray();
        SQLiteDatabase db = null;
        try {
            db = helper.getReadableDatabase();
            //开启事务
            db.beginTransaction();
            if ("after".equals(direction)) {
                String sql = "select " + "f_value" + " from " + table
                        + " where " + "f_id" + ">" + id + " and " + "f_key" + "=" + "'" + disKey + "'" + " order by" + " "
                        + "f_id" + " asc" + " limit " + num;//根据key查value
                Log.e("huang", "后sql:" + sql);
                Cursor cursor = db.rawQuery(sql, null);
                if (cursor != null && cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        String value = cursor.getString(cursor.getColumnIndex("f_value"));
                        JSONObject jsonObject = new JSONObject(value);
                        jsonArray.put(jsonObject);
                        cursor.moveToNext();
                    }
                }
                cursor.close();
            } else if ("before".equals(direction)) {
                String sql = "select " + "f_value" + " from " + table + " where "
                        + "f_id" + "<" + id + " and " + "f_key" + "=" + "'" + disKey + "'" + " order by" + " "
                        + "f_id" + " desc" + " limit " + num;//根据key查value
                Log.e("huang", "前sql:" + sql);
                Cursor cursor = db.rawQuery(sql, null);
                if (cursor != null && cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        String value = cursor.getString(cursor.getColumnIndex("f_value"));
                        JSONObject jsonObject = new JSONObject(value);
                        jsonArray.put(jsonObject);
                        cursor.moveToNext();
                    }
                }
                cursor.close();
            }
            //设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
            return jsonArray;
        } catch (Exception e) {
            Log.e("huang", "根据id找value：" + e.toString());
            return null;
        } finally {
            try {
                if (null != db) {
                    //结束事务
                    db.endTransaction();
                    if (db.isOpen()) {
                        db.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //10.根据id删除某条数据
    public synchronized void deleteDataByKeyID(String disKey, int id) {
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            //开启事务
            db.beginTransaction();
            // 执行删除操作
            int count = db.delete(tablearr, "f_id=? and f_key=?", new String[]{id + "", disKey});
            Log.e("huang", "根据id删除某条数据:" + count);
            //设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("huang", "根据id删除某条数据" + e.toString());
        } finally {
            try {
                if (null != db) {
                    //结束事务
                    db.endTransaction();
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //11.通过key删除一批id对应的数据
    public synchronized void deleteDataByKeySomeID(String disKey, String idArr) {
        SQLiteDatabase db = null;
        try {
            db = helper.getReadableDatabase();
            //开启事务
            db.beginTransaction();
            String sql = "delete from "+tablearr+" where f_key = ? and f_id in (" + idArr + ")";
            db.execSQL(sql);
//            String[] split = idArr.split(",");
//            for (int i = 0; i < split.length; i++) {
//                // 执行删除操作
//                int count = db.delete(tablearr, "f_id=? and f_key=?", new String[]{split[i], disKey});
//                Log.e("huang", "根据id删除某条数据:" + count);
//            }

            //设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("huang", "根据id删除某条数据" + e.toString());
        } finally {
            try {
                if (null != db) {
                    //结束事务
                    db.endTransaction();
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //12.删除key所对应所有数据
    public synchronized void deleteDataByKey(String disKey) {
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            //开启事务
            db.beginTransaction();

                // 执行删除操作
                int count = db.delete(tablearr, "f_key=?", new String[]{disKey});
                Log.e("huang", "根据id删除某条数据:" + count);
            //设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("huang", "根据id删除某条数据" + e.toString());
        } finally {
            try {
                if (null != db) {
                    //结束事务
                    db.endTransaction();
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //map/字符串表
    public synchronized void createTableSql() {
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
//        writableDatabase.execSQL("create table if not exists " + table + "(" +
//                "id" + " integer primary key autoincrement, " +
//                TABLE_KEY + " varchar, " +
//                TABLE_VALUE + " varchar, f_time timestamp not null default (datetime('now', 'localtime'))) ");
//        writableDatabase.execSQL("create table if not exists c_t_list (f_id integer, f_key varchar(255), f_value text, f_time timestamp not null default (datetime('now', 'localtime')), unique(f_id, f_key))");
        writableDatabase.execSQL("create table if not exists t_c_map (f_key varchar(255), f_value text, f_time timestamp not null default (datetime('now', 'localtime')), unique(f_key))");
        writableDatabase.close();
    }

    //list表
    public synchronized void createArrayKeyTableSql() {
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        writableDatabase.execSQL("create table if not exists t_c_list (f_id integer, f_key varchar(255), f_value text, f_time timestamp not null default (datetime('now', 'localtime')), unique(f_id, f_key))");
//        writableDatabase.execSQL("create table if not exists " + tablearr + "(" +
//                "id" + " integer primary key autoincrement, f_id integer, f_key varchar, f_value varchar, f_time timestamp not null default (datetime('now', 'localtime'))) ");
        writableDatabase.close();
    }

}