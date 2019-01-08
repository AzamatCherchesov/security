package com.a1tt.security.shedulers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.a1tt.security.DB.DBHelper
import java.util.concurrent.Executor

class DBScheduler(val executor: Executor, val context: Context) {

    init {
        val dbHelper = DBHelper(context)
        db = dbHelper.writableDatabase
    }

    companion object {
        lateinit var db: SQLiteDatabase
    }
}
