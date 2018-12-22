package com.a1tt.security.shedulers

import android.os.Handler
import android.util.Log
import com.a1tt.security.Consts
import com.a1tt.security.Consts.Companion.GET_SCAN_URL_RESULT
import com.a1tt.security.Consts.Companion.GOT_SCAN_URL_RESULT
import com.a1tt.security.Consts.Companion.RESPONCE_CODE_INT
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class ScanURLSheduler(val isPOST: Boolean, val strURL: String, val args: MutableList<Pair<String, String>>, val handler: Handler) : Runnable {

    override fun run() {
        if (isPOST) {
            Log.e("A1tt---", "inPost")
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
            // Send the data
            outputWriter.flush()

            // Create an input stream to read the response
            val inputStream = BufferedReader(InputStreamReader(connection.inputStream)).use {
                // Container for input stream data
                val response = StringBuffer()
                var inputLine = it.readLine()
                // Add each line to the response container
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                it.close()
                Log.e("A1tt", String(response))
                val mainObject = JSONObject(String(response))
                val responce_code = mainObject.getInt(RESPONCE_CODE_INT)
                Log.e("A1tt----", "responce code " + responce_code)
                if (responce_code == 1) {
                    handler.sendMessage(handler.obtainMessage(GET_SCAN_URL_RESULT, mainObject.getString("scan_id")))
                }
            }
            connection.disconnect()
        } else {
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
            val inputStream = BufferedReader(InputStreamReader(connection.inputStream)).use {
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
                if (mainObject.getInt(Consts.RESPONCE_CODE_INT) == 1) {
                    handler.sendMessage(handler.obtainMessage(GOT_SCAN_URL_RESULT, mainObject))
                }

//                val url = mainObject.getString(Consts.URL_STR)
//                val scandate = mainObject.getString(Consts.SCAN_DATE_STR)
//                val scans = mainObject.getJSONObject(Consts.SCANS_ARR)

            }
            connection.disconnect()
        }
    }
}