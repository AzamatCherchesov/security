package com.a1tt.security.shedulers

import android.os.Handler
import android.util.Log
import android.content.ContentValues
import com.a1tt.security.Consts
import com.a1tt.security.data.ScanedURL
import com.a1tt.security.data.ServicesResult


class DBWorker(val command: String, val handler: Handler?, val scanedURL: ScanedURL?, val selectionString : String?): Runnable {
    override fun run() {
        Log.e("A1tt", "DBWorker " + command)
        Thread.sleep(1000)

        when (command) {
            "add" -> {
                val cv = ContentValues()

                cv.put("url", scanedURL?.scanedURL)
                cv.put("scan_date", scanedURL?.scanDate)
                cv.put("verbose_msg", scanedURL?.verboseMsg)
                cv.put("number_positives", scanedURL?.numberPositives)
                cv.put("number_total", scanedURL?.numberTotal)
                // вставляем запись и получаем ее ID
                val rowID = DBSheduler.db.insert("mytable", null, cv)
                handler?.sendMessage(handler?.obtainMessage(Consts.SUCCESED_WRITE_TO_DB, rowID))

            }
            "read" -> {
                // делаем запрос всех данных из таблицы mytable, получаем Cursor
                val c = DBSheduler.db.query("mytable", null, null, null, null, null, null)

                // ставим позицию курсора на первую строку выборки
                // если в выборке нет строк, вернется false
                if (c.moveToFirst())
                {

                    // определяем номера столбцов по имени в выборке
                    val idColIndex = c.getColumnIndex("id")
//                    val nameColIndex = c.getColumnIndex("url")
//                    val emailColIndex = c.getColumnIndex("scan_date")
                    val urlColIndex = c.getColumnIndex("url")
                    val scanDateColIndex = c.getColumnIndex("scan_date")
                    val verboseMsgColIndex = c.getColumnIndex("verbose_msg")
                    val numberPositivesColIndex = c.getColumnIndex("number_positives")
                    val numberTotalColIndex = c.getColumnIndex("number_total")

                    val scans = mutableSetOf<Pair<String, ServicesResult>>()

                    do
                    {
                        val scanedURL = ScanedURL(c.getString(urlColIndex), c.getString(scanDateColIndex), c.getString(verboseMsgColIndex), c.getInt(numberPositivesColIndex),
                                c.getInt(numberTotalColIndex), scans)


                        handler?.sendMessage(handler?.obtainMessage(Consts.SUCCESED_READ_FROM_DB, scanedURL))
                    }
                    while (c.moveToNext())
                }

                else
                    Log.d("a1tt", "0 rows")
                c.close()
            }
            "select" -> {
                val c = DBSheduler.db.rawQuery("SELECT * FROM mytable WHERE number_positives = " + 0, null)
                if (c.moveToFirst())
                {
                    val idColIndex = c.getColumnIndex("id")
                    val urlColIndex = c.getColumnIndex("url")
                    val scanDateColIndex = c.getColumnIndex("scan_date")
                    val verboseMsgColIndex = c.getColumnIndex("verbose_msg")
                    val numberPositivesColIndex = c.getColumnIndex("number_positives")
                    val numberTotalColIndex = c.getColumnIndex("number_total")

                    val scans = mutableSetOf<Pair<String, ServicesResult>>()

                    do
                    {
                        val scanedURL = ScanedURL(c.getString(urlColIndex), c.getString(scanDateColIndex), c.getString(verboseMsgColIndex), c.getInt(numberPositivesColIndex),
                                c.getInt(numberTotalColIndex), scans)


                        handler?.sendMessage(handler?.obtainMessage(Consts.SUCCESED_READ_FROM_DB, scanedURL))
                    }
                    while (c.moveToNext())
                }
                c.close()
            }
        }
    }
}