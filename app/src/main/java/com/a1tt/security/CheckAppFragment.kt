package com.a1tt.security

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection

class CheckAppFragment  : DialogFragment() {
    lateinit var appName: String

    fun setName(appName:String): CheckAppFragment {
        this.appName = appName
        return this
    }


    //TODO в ars fragment или через savedInstanceState

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity).setTitle("Отправка приложения на анализ")
                .setMessage("Отправить приложение \"$appName\" на анализ?")
                .setPositiveButton("отправить",object : DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        //TODO HTTP request
                        //746cdb67b9f9ef1b202e04051b84bbec8756e908a0b6a7b6ed409b7f0a616225
                        Thread(Runnable {
                            // Set up request
                            val connection: HttpsURLConnection = URL("https://www.virustotal.com/vtapi/v2/url/scan").openConnection() as HttpsURLConnection
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
                            outputWriter.write("apikey=746cdb67b9f9ef1b202e04051b84bbec8756e908a0b6a7b6ed409b7f0a616225;")
                            outputWriter.write("url=ya.ru")
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
                                // TODO: Add main thread callback to parse response

                                val mainObject = JSONObject(String(response))
                                val parmalink = mainObject.getString("permalink")
                                val scan_id = mainObject.getString("scan_id")
                                val responce_code = mainObject.getInt("response_code")
                                println(">>>> parmalink: $parmalink")
                                println(">>>> scan_id: $scan_id")
                                println(">>>> responce_code: $responce_code")
                            }
                            connection.disconnect()
                        }).start()


                    }

                })
                .setNegativeButton("отмена", null)
                .create()
    }
}