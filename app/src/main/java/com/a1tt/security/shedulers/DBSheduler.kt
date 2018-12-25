package com.a1tt.security.shedulers

import android.content.ContentValues
import android.util.Log
import com.a1tt.security.DB.DBHelper

class DBSheduler(val dbHelper : DBHelper, val operation: String, val args: MutableList<Pair<String, String>>, val tableName: String) : Runnable {

    override fun run() {
        val cv  = ContentValues()
        val db = dbHelper.writableDatabase

        when (operation) {
            "add" -> {
                if (args.isNotEmpty()) {
                    for (i in args) {
                       cv.put(i.first, i.second)
                    }
                    db.insert(tableName, null, cv)
                }
            }
            "read" -> {
                // делаем запрос всех данных из таблицы mytable, получаем Cursor
                val cursor = db.query(tableName, null, null, null, null, null, null)

                // ставим позицию курсора на первую строку выборки
                // если в выборке нет строк, вернется false
                if (cursor.moveToFirst()) {

                    // определяем номера столбцов по имени в выборке
                    val idColIndex = cursor.getColumnIndex("id")
                    val nameColIndex = cursor.getColumnIndex("name")
                    val emailColIndex = cursor.getColumnIndex("email")
                    do {
                        // получаем значения по номерам столбцов и пишем все в лог
                        Log.d("A1tt",
                                "ID = " + cursor.getInt(idColIndex) +
                                        ", name = " + cursor.getString(nameColIndex) +
                                        ", email = " + cursor.getString(emailColIndex))
                        // переход на следующую строку
                        // а если следующей нет (текущая - последняя), то false - выходим из цикла
                    } while (cursor.moveToNext());
                }
                cursor.close()
            }
        }
    }
}