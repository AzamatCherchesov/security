package com.a1tt.security.shedulers

import android.content.ContentValues
import android.content.Context
import android.os.Handler
import android.util.Log
import com.a1tt.security.Constants
import com.a1tt.security.data.ScannedURL
import com.a1tt.security.data.URLScanServicesResult
import java.lang.Thread.sleep


class DBURLWorker(val context: Context, val command: String, val handler: Handler?, val scanedURL: ScannedURL?, val selectionString: String?) : Runnable {
    override fun run() {
        Log.e("A1tt", "DBWorker " + command)
        Thread.sleep(1000)

        when (command) {
            "add" -> {
                val cv = ContentValues()

                cv.put("url", scanedURL?.scannedURL)
                cv.put("scan_date", scanedURL?.scanDate)
                cv.put("verbose_msg", scanedURL?.verboseMsg)
                cv.put("number_positives", scanedURL?.numberPositives)
                cv.put("number_total", scanedURL?.numberTotal)
                // вставляем запись и получаем ее ID
                val rowID = DBScheduler.db.insert("mytable", null, cv)

                for (elem in scanedURL?.scans!!) {
                    val addCV = ContentValues()
                    addCV.put("url", scanedURL.scannedURL)
                    addCV.put("service", elem.serviceName)
                    addCV.put("detected", elem.detected.toString())
                    addCV.put("result", elem.result)
                    addCV.put("detail", elem.detail)
                    DBScheduler.db.insert("additional", null, addCV)
                }

                handler?.sendMessage(handler.obtainMessage(Constants.SUCCESED_WRITE_URL_TO_DB, rowID))

            }
            "read" -> {
                // делаем запрос всех данных из таблицы mytable, получаем Cursor
                val c = DBScheduler.db.query("mytable", null, null, null, null, null, null)

                // ставим позицию курсора на первую строку выборки
                // если в выборке нет строк, вернется false
                if (c.moveToFirst()) {

                    // определяем номера столбцов по имени в выборке
                    val urlColIndex = c.getColumnIndex("url")
                    val scanDateColIndex = c.getColumnIndex("scan_date")
                    val verboseMsgColIndex = c.getColumnIndex("verbose_msg")
                    val numberPositivesColIndex = c.getColumnIndex("number_positives")
                    val numberTotalColIndex = c.getColumnIndex("number_total")

                    val scans = mutableListOf<URLScanServicesResult>()

                    do {
                        val scanedURL = ScannedURL(c.getString(urlColIndex), c.getString(scanDateColIndex), c.getString(verboseMsgColIndex), c.getInt(numberPositivesColIndex),
                                c.getInt(numberTotalColIndex), scans)

                        handler?.sendMessage(handler.obtainMessage(Constants.SUCCESED_READ_URL_FROM_DB, scanedURL))
                    } while (c.moveToNext())
                } else
                    Log.d("a1tt", "0 rows")
                c.close()
            }
            "select" -> {
                val c = DBScheduler.db.rawQuery(String.format("SELECT * FROM mytable WHERE url = \"%s\"", selectionString), null)
                if (c.moveToFirst()) {
                    val urlColIndex = c.getColumnIndex("url")
                    val scanDateColIndex = c.getColumnIndex("scan_date")
                    val verboseMsgColIndex = c.getColumnIndex("verbose_msg")
                    val numberPositivesColIndex = c.getColumnIndex("number_positives")
                    val numberTotalColIndex = c.getColumnIndex("number_total")

                    val scans = mutableListOf<URLScanServicesResult>()

                    val addC = DBScheduler.db.rawQuery(String.format("SELECT * FROM additional WHERE url = \"%s\"", selectionString), null)
                    if (addC.moveToFirst()) {
                        val addServiceColIndex = addC.getColumnIndex("service")
                        val addDetectedColIndex = addC.getColumnIndex("detected")
                        val addResultColIndex = addC.getColumnIndex("result")
                        val addDetailColIndex = addC.getColumnIndex("detail")
                        do {
                            scans.add(URLScanServicesResult(addC.getString(addServiceColIndex), addC.getString(addDetectedColIndex)!!.toBoolean(),
                                    addC.getString(addResultColIndex), addC.getString(addDetailColIndex)))
                        } while (addC.moveToNext())
                    }
                    addC.close()

                    do {
                        val scanedURL = ScannedURL(c.getString(urlColIndex), c.getString(scanDateColIndex), c.getString(verboseMsgColIndex), c.getInt(numberPositivesColIndex),
                                c.getInt(numberTotalColIndex), scans)

                        handler?.sendMessage(handler.obtainMessage(Constants.SUCCESED_READ_URL_FROM_DB, scanedURL))
                    } while (c.moveToNext())
                    sleep(10000)
                }
                c.close()
            }
        }
    }
}