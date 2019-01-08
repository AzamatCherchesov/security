package com.a1tt.security.shedulers

import android.os.Handler
import android.util.Log
import com.a1tt.security.Constants
import org.json.JSONObject
import java.io.*
import java.lang.Thread.sleep
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class ScanFileScheduler(private val isPOST: Boolean, private val strURL: String, private val args: MutableList<Pair<String, String>>, private val handler: Handler, private val apkFilePath: String, val app_name: String?) : Runnable {

    override fun run() {
        if (isPOST) {
            val fileInputStream = FileInputStream(File(apkFilePath))
            val bytes = fileInputStream.readBytes(fileInputStream.available())
            // Set up request
            val connection: HttpsURLConnection = URL(strURL).openConnection() as HttpsURLConnection
            // Default is GET so you must override this for post
            connection.requestMethod = "POST"
            // To send a post body, output must be true
            connection.doOutput = true
            connection.doInput = true
            // Create the stream
            val outputStream: OutputStream = connection.outputStream
            // Create a writer container to pass the output over the stream
            val outputWriter = OutputStreamWriter(outputStream)
            // Add the string to the writer container
            if (args.isNotEmpty()) {
                for (i in args) {
                    outputWriter.write(String.format("%s=%s;", i.first, i.second))
                }
            }
            outputWriter.write("file=")
            outputWriter.flush()
            outputStream.write(bytes)
            outputWriter.write(";")
            // Send the data
            outputWriter.flush()

            // Create an input stream to read the response
            BufferedReader(InputStreamReader(connection.inputStream)).use {
                // Container for input stream data
                val response = StringBuffer()
                var inputLine = it.readLine()
                // Add each line to the response container
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                it.close()
                val mainObject = JSONObject(String(response))
                val responce_code = mainObject.getInt(Constants.RESPONCE_CODE_INT)
                mainObject.putOpt("app_name", app_name)
                if (responce_code == 1) {
                    handler.sendMessage(handler.obtainMessage(Constants.GET_SCAN_FILE_RESULT, mainObject))
                }
            }
            connection.disconnect()
        } else {
            sleep(15000)
            val connection: HttpsURLConnection = URL(strURL).openConnection() as HttpsURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            connection.instanceFollowRedirects = true
            connection.doOutput = true
            connection.doInput = true
            // Create the stream
            val outputStream: OutputStream = connection.outputStream
            // Create a writer container to pass the output over the stream
            val outputWriter = OutputStreamWriter(outputStream)
            // Add the string to the writer container
            if (args.isNotEmpty()) {
                for (i in args) {
                    outputWriter.write(String.format("%s=%s;", i.first, i.second))
                }
            }

            // Send the data
            outputWriter.flush()
            val responseCode = connection.responseCode
            Log.e("A1tt", "responce code $responseCode")

            // Create an input stream to read the response
            BufferedReader(InputStreamReader(connection.inputStream)).use {
                // Container for input stream data
                val response = StringBuffer()
                var inputLine = it.readLine()
                // Add each line to the response container
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                it.close()
                val mainObject = JSONObject(String(response))
                mainObject.putOpt("app_name", app_name)
                if (mainObject.getInt(Constants.RESPONCE_CODE_INT) == 1) {
                    handler.sendMessage(handler.obtainMessage(Constants.GOT_SCAN_FILE_RESULT, mainObject))
                }
                if (mainObject.getInt(Constants.RESPONCE_CODE_INT) == -2) {
                    Thread(ScanFileScheduler(isPOST, strURL, args, handler, apkFilePath, app_name)).start()
                }
            }
            connection.disconnect()
        }
    }
}