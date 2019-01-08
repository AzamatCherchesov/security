package com.a1tt.security.shedulers

import android.content.ContentValues
import android.content.Context
import android.os.Handler
import com.a1tt.security.Constants
import com.a1tt.security.MainApplication
import com.a1tt.security.data.FileScanServicesResult
import com.a1tt.security.data.ScannedFile

class DBFileWorker(val context: Context, private val command: String, private val handler: Handler?, private val scannedFile: ScannedFile?, private val selectionString: String?) : Runnable {
    override fun run() {

        when (command) {
            "add" -> {
                val cv = ContentValues()
                cv.put("scanned_file", scannedFile?.scannedFile)
                cv.put("scan_date", scannedFile?.scanDate)
                cv.put("verbose_msg", scannedFile?.verboseMsg)
                cv.put("number_positives", scannedFile?.numberPositives)
                cv.put("number_total", scannedFile?.numberTotal)
                DBScheduler.db.insert("files", null, cv)

                for (elem in scannedFile?.scans!!) {
                    val addCV = ContentValues()
                    addCV.put("scanned_file", scannedFile.scannedFile)
                    addCV.put("service", elem.serviceName)
                    addCV.put("detected", elem.detected.toString())
                    addCV.put("result", elem.result)
                    addCV.put("version", elem.version)
                    addCV.put("update_field", elem.update)
                    DBScheduler.db.insert("filesAdd", null, addCV)
                }

                val positives = scannedFile.numberPositives
                if (positives == 0) {
                    var index = 0
                    while (index < MainApplication.appDataManager.getAllInstalledApp().size()) {
                        val app = MainApplication.appDataManager.getAllInstalledApp().get(index)
                        if (app.packageName.equals(scannedFile.scannedFile)) {
                            MainApplication.appDataManager.removeApp(app)
                            app.result = "clean"
                            MainApplication.appDataManager.addApp(app)
                        }
                        index++
                    }
                } else {
                    var index = 0
                    while (index < MainApplication.appDataManager.getAllInstalledApp().size()) {
                        val app = MainApplication.appDataManager.getAllInstalledApp().get(index)
                        if (app.appName.equals(scannedFile.scannedFile)) {
                            MainApplication.appDataManager.removeApp(app)
                            app.result = "bad"
                            MainApplication.appDataManager.addApp(app)
                        }
                        index++
                    }
                }

                handler?.sendMessage(handler.obtainMessage(Constants.SUCCESED_WRITE_FILE_TO_DB, scannedFile))

            }
            "select" -> {
                val c = DBScheduler.db.rawQuery(String.format("SELECT * FROM files WHERE scanned_file = \"%s\"", selectionString), null)
                if (c.moveToFirst()) {
                    val scannedFileColIndex = c.getColumnIndex("scanned_file")
                    val scanDateColIndex = c.getColumnIndex("scan_date")
                    val verboseMsgColIndex = c.getColumnIndex("verbose_msg")
                    val numberPositivesColIndex = c.getColumnIndex("number_positives")
                    val numberTotalColIndex = c.getColumnIndex("number_total")

                    val scans = mutableListOf<FileScanServicesResult>()

                    val addC = DBScheduler.db.rawQuery(String.format("SELECT * FROM filesAdd WHERE scanned_file = \"%s\"", selectionString), null)
                    if (addC.moveToFirst()) {
                        val addServiceColIndex = addC.getColumnIndex("service")
                        val addDetectedColIndex = addC.getColumnIndex("detected")
                        val addResultColIndex = addC.getColumnIndex("result")
                        val addVersionColIndex = addC.getColumnIndex("version")
                        val addUpdateColIndex = addC.getColumnIndex("update_field")
                        do {
                            scans.add(FileScanServicesResult(addC.getString(addServiceColIndex), addC.getString(addDetectedColIndex)!!.toBoolean(),
                                    addC.getString(addVersionColIndex), addC.getString(addResultColIndex), addC.getString(addUpdateColIndex)))
                        } while (addC.moveToNext())
                    }
                    addC.close()

                    do {
                        val scannedFile = ScannedFile(c.getString(scannedFileColIndex), c.getString(scanDateColIndex), c.getString(verboseMsgColIndex),
                                c.getInt(numberPositivesColIndex), c.getInt(numberTotalColIndex), scans)
                        handler?.sendMessage(handler.obtainMessage(Constants.SUCCESED_READ_FILE_FROM_DB, scannedFile))
                    } while (c.moveToNext())
                }
                c.close()
            }
        }
    }
}