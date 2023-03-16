package com.zwk.trash_detective.Bean;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;

public class B_TrashDatabase extends SQLiteOpenHelper {


    public B_TrashDatabase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    String trashdata = "create table trashdata (" +
            "id integer primary key autoincrement, " +
            "name text, " +
            "sortId integer, " +
            "star integer, " +
            "root text, " +
            "description text, " +
            "url text)";

    String userdata = "create table user (" +
            "id integer primary key autoincrement, " +
            "name text, " +
            "password text, " +
            "email text, " +
            "phonenum text)";

    String stardata = "create table star (" +
            "uuid text, " +
            "name text, " +
            "root text, " +
            "description text)";

    String searchdata = "create table record(" +
            "id integer primary key autoincrement," +
            " name text)";


    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表的SQL语句
        db.execSQL(trashdata);
        db.execSQL(userdata);
        db.execSQL(stardata);
        db.execSQL(searchdata);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("drop table if exists trashdata ");
//        onCreate(db);//重新创建
    }

}