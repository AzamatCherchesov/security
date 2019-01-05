package com.a1tt.security.DB

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class DBHelper(val context: Context) : SQLiteOpenHelper(context, "myDB", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        // создаем таблицу с полями
        db?.execSQL("create table mytable ("
                + "id integer primary key autoincrement,"
                + "url text,"
                + "scan_date text,"
                + "verbose_msg text,"
                + "number_positives number,"
                + "number_total number" + ");")

        db?.execSQL("create table additional ("
                + "id integer primary key autoincrement,"
                + "url text,"
                + "service text,"
                + "detected text,"
                + "result text,"
                + "detail text" + ");")

//        db?.execSQL("create table mytable ("
//                + "id integer primary key autoincrement,"
//                + "url text,"
//                + "scan_date text" + ");");
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }
}