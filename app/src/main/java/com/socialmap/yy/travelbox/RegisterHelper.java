package com.socialmap.yy.travelbox;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by gxyzw_000 on 2015/1/12.
 */
public class RegisterHelper extends SQLiteOpenHelper {


    public static final String TB_NAME = "user";
    public static final String ID = "id";

    public static final String NAME = "userid";

    public static final String UerPwd = "userpwd";




    public static final String CREATE_Register = "CREATE TABLE IF NOT EXISTS "

                           + TB_NAME + " ("

                            + ID + "INTEGER PRIMARY KEY,"

                          + NAME + "VARCHAR,"

                            + UerPwd + " VARCHAR)";
    private Context mContext;

    public RegisterHelper(Context context, String name, SQLiteDatabase.CursorFactory
            factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_Register);
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void close() {
        this.getWritableDatabase().close();
    }

    //添加新用户
    public boolean AddUser(String userid, String userpwd) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(this.NAME, userid);//添加用户名
            cv.put(this.UerPwd, userpwd);//添加密码
            this.getWritableDatabase().insert(this.TB_NAME, null, cv);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }






}



