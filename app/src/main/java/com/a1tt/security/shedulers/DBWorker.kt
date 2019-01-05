package com.a1tt.security.shedulers

import android.os.Handler
import android.util.Log
import android.content.ContentValues
import android.content.Context
import com.a1tt.security.AnalysResults.SingleURLResult
import com.a1tt.security.Consts
import com.a1tt.security.data.ScanedURL
import com.a1tt.security.data.ServicesResult
import java.lang.Thread.sleep


class DBWorker(val context: Context, val command: String, val handler: Handler?, val scanedURL: ScanedURL?, val selectionString : String?): Runnable {
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

                for (elem in scanedURL?.scans!!) {
                    val addCV = ContentValues()
                    addCV.put("url", scanedURL?.scanedURL)
                    addCV.put("service", elem.serviceName)
                    addCV.put("detected", elem.detected.toString())
                    addCV.put("result", elem.result)
                    addCV.put("detail", elem.detail)
                    DBSheduler.db.insert("additional", null, addCV)
                }

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

                    val scans = mutableListOf<ServicesResult>()

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
                val c = DBSheduler.db.rawQuery("SELECT * FROM mytable WHERE url = \"" + selectionString + "\"", null)
                if (c.moveToFirst())
                {
                    val idColIndex = c.getColumnIndex("id")
                    val urlColIndex = c.getColumnIndex("url")
                    val scanDateColIndex = c.getColumnIndex("scan_date")
                    val verboseMsgColIndex = c.getColumnIndex("verbose_msg")
                    val numberPositivesColIndex = c.getColumnIndex("number_positives")
                    val numberTotalColIndex = c.getColumnIndex("number_total")

                    val scans = mutableListOf<ServicesResult>()

                    val addC = DBSheduler.db.rawQuery("SELECT * FROM additional WHERE url = \"" + selectionString + "\"", null)
                    if (addC.moveToFirst())
                    {
                        val addIdColIndex = addC.getColumnIndex("id")
                        val addUrlColIndex = addC.getColumnIndex("url")
                        val addServiceColIndex = addC.getColumnIndex("service")
                        val addDetectedColIndex = addC.getColumnIndex("detected")
                        val addResultColIndex = addC.getColumnIndex("result")
                        val addDetailColIndex = addC.getColumnIndex("detail")
                        do
                        {
                            scans.add(ServicesResult(addC.getString(addServiceColIndex), addC.getString(addDetectedColIndex).toBoolean(),
                                    addC.getString(addResultColIndex) ,addC.getString(addDetailColIndex)))
                        } while(addC.moveToNext())
                    }
                    addC.close()

                    do
                    {
                        val scanedURL = ScanedURL(c.getString(urlColIndex), c.getString(scanDateColIndex), c.getString(verboseMsgColIndex), c.getInt(numberPositivesColIndex),
                                c.getInt(numberTotalColIndex), scans)


                        handler?.sendMessage(handler?.obtainMessage(Consts.SUCCESED_READ_FROM_DB, scanedURL))
                    }
                    while (c.moveToNext())
                    sleep(10000)
                }
                c.close()
            }
        }
    }
}