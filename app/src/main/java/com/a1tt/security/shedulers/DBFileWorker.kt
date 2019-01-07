package com.a1tt.security.shedulers

import android.content.ContentValues
import android.content.Context
import android.os.Handler
import com.a1tt.security.Consts
import com.a1tt.security.data.FileScanServicesResult
import com.a1tt.security.data.ScanedFile

class DBFileWorker (val context: Context, val command: String, val handler: Handler?, val scanedFile: ScanedFile?, val selectionString : String?): Runnable {
    override fun run() {

        when (command) {
            "add" -> {
                val cv = ContentValues()
                cv.put("scaned_file", scanedFile?.scanedFile)
                cv.put("scan_date", scanedFile?.scanDate)
                cv.put("verbose_msg", scanedFile?.verboseMsg)
                cv.put("number_positives", scanedFile?.numberPositives)
                cv.put("number_total", scanedFile?.numberTotal)
                val rowID = DBSheduler.db.insert("files", null, cv)

                for (elem in scanedFile?.scans!!) {
                    val addCV = ContentValues()
                    addCV.put("scaned_file", scanedFile?.scanedFile)
                    addCV.put("service", elem.serviceName)
                    addCV.put("detected", elem.detected.toString())
                    addCV.put("result", elem.result)
                    addCV.put("version", elem.version)
                    addCV.put("update_field", elem.update)
                    DBSheduler.db.insert("filesAdd", null, addCV)
                }

                handler?.sendMessage(handler?.obtainMessage(Consts.SUCCESED_WRITE_FILE_TO_DB, scanedFile))

            }
            "select" -> {
                val c = DBSheduler.db.rawQuery("SELECT * FROM files WHERE scaned_file = \"" + selectionString + "\"", null)
                if (c.moveToFirst())
                {
                    val idColIndex = c.getColumnIndex("id")
                    val scanedFileColIndex = c.getColumnIndex("scaned_file")
                    val scanDateColIndex = c.getColumnIndex("scan_date")
                    val verboseMsgColIndex = c.getColumnIndex("verbose_msg")
                    val numberPositivesColIndex = c.getColumnIndex("number_positives")
                    val numberTotalColIndex = c.getColumnIndex("number_total")

                    val scans = mutableListOf<FileScanServicesResult>()

                    val addC = DBSheduler.db.rawQuery("SELECT * FROM filesAdd WHERE scaned_file = \"" + selectionString + "\"", null)
                    if (addC.moveToFirst())
                    {
                        val addIdColIndex = addC.getColumnIndex("id")
                        val addScanedFileColIndex = c.getColumnIndex("scaned_file")
                        val addServiceColIndex = addC.getColumnIndex("service")
                        val addDetectedColIndex = addC.getColumnIndex("detected")
                        val addResultColIndex = addC.getColumnIndex("result")
                        val addVersionColIndex = addC.getColumnIndex("version")
                        val addUpdateColIndex = addC.getColumnIndex("update_field")
                        do
                        {
                            scans.add(FileScanServicesResult(addC.getString(addServiceColIndex), addC.getString(addDetectedColIndex).toBoolean(),
                                    addC.getString(addVersionColIndex) ,addC.getString(addResultColIndex) ,addC.getString(addUpdateColIndex)))
                        } while(addC.moveToNext())
                    }
                    addC.close()

                    do
                    {
                        val scanedFile = ScanedFile(c.getString(scanedFileColIndex), c.getString(scanDateColIndex), c.getString(verboseMsgColIndex),
                                c.getInt(numberPositivesColIndex), c.getInt(numberTotalColIndex), scans)
                        handler?.sendMessage(handler?.obtainMessage(Consts.SUCCESED_READ_FILE_FROM_DB, scanedFile))
                    }
                    while (c.moveToNext())
                }
                c.close()
            }
        }
    }
}