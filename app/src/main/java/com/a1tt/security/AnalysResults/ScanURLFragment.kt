package com.a1tt.security.AnalysResults

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import com.a1tt.security.Constants.Companion.APIKEY_STR
import com.a1tt.security.Constants.Companion.URL_STR
import com.a1tt.security.MainActivity.Companion.mainHandler
import com.a1tt.security.MainActivity.Companion.router
import com.a1tt.security.R
import com.a1tt.security.shedulers.ScanURLScheduler

class ScanURLFragment : Fragment() {

    private var scanURLButton: Button? = null
    private var inputText: TextView? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Scan URL"
        (activity as AppCompatActivity).findViewById<View>(R.id.search)?.visibility = GONE
        val view = inflater.inflate(R.layout.scan_url_fragment, container, false)
        scanURLButton = view.findViewById<View>(R.id.scanButton) as Button
        inputText = view.findViewById<View>(R.id.inputText) as TextView
        scanURLButton?.setOnClickListener {
            val firstPair = Pair(APIKEY_STR, "746cdb67b9f9ef1b202e04051b84bbec8756e908a0b6a7b6ed409b7f0a616225")
            val secondPair = Pair(URL_STR, inputText!!.text.toString())
            val args: MutableList<Pair<String, String>> = mutableListOf(firstPair, secondPair)
            Thread(ScanURLScheduler(true, "https://www.virustotal.com/vtapi/v2/url/scan", args, mainHandler)).start()
            (activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
            router.navigateBack()
        }
        return view
    }

//    fun makePostRequest() {
//        lateinit var scan_id: String
//
//        // Set up request
//        val connection: HttpsURLConnection = URL("https://www.virustotal.com/vtapi/v2/url/scan").openConnection() as HttpsURLConnection
//        // Default is GET so you must override this for post
//        connection.requestMethod = "POST"
//        // To send a post body, output must be true
//        connection.doOutput = true
//        connection.doInput = true
//        // Create the stream
//        val outputStream: OutputStream = connection.outputStream
//        // Create a writer container to pass the output over the stream
//        val outputWriter = OutputStreamWriter(outputStream)
//        // Add the string to the writer container
//        outputWriter.write(String.format("%s=746cdb67b9f9ef1b202e04051b84bbec8756e908a0b6a7b6ed409b7f0a616225;", APIKEY_STR))
//        outputWriter.write(String.format("%s=%s;", URL_STR, inputText!!.text))
//        // Send the data
//        outputWriter.flush()
//
//        // Create an input stream to read the response
//        val inputStream = BufferedReader(InputStreamReader(connection.inputStream)).use {
//            // Container for input stream data
//            val response = StringBuffer()
//            var inputLine = it.readLine()
//            // Add each line to the response container
//            while (inputLine != null) {
//                response.append(inputLine)
//                inputLine = it.readLine()
//            }
//            it.close()
//
//            val mainObject = JSONObject(String(response))
//            val parmalink = mainObject.getString("permalink")
//            scan_id = mainObject.getString("scan_id")
//            val responce_code = mainObject.getInt("response_code")
//            println(">>>> parmalink: $parmalink")
//            println(">>>> scan_id: $scan_id")
//            println(">>>> responce_code: $responce_code")
//        }
//        connection.disconnect()
//        makeGetRequest(scan_id)
//    }
//
//    fun makeGetRequest(resource: String) {
//        Log.e("A1tt", "test")
//        val connection: HttpsURLConnection = URL("https://www.virustotal.com/vtapi/v2/url/report").openConnection() as HttpsURLConnection
//        connection.requestMethod = "GET"
//        connection.connectTimeout = 5000
//        connection.readTimeout = 5000
//        connection.instanceFollowRedirects = true
//        connection.doOutput = true
//        connection.doInput = true
//        // Create the stream
//        val outputStream: OutputStream = connection.outputStream
//        // Create a writer container to pass the output over the stream
//        val outputWriter = OutputStreamWriter(outputStream)
//        // Add the string to the writer container
//        outputWriter.write(String.format("%s=746cdb67b9f9ef1b202e04051b84bbec8756e908a0b6a7b6ed409b7f0a616225;", APIKEY_STR))
//        outputWriter.write(String.format("%s=%s;", RESOURCE_STR, resource))
//        outputWriter.write(String.format("%s=true;", ALLINFO_BOOL))
//        // Send the data
//        outputWriter.flush()
//        val responseCode = connection.responseCode
//        Log.e("A1tt", "responce code $responseCode")
//
//        // Create an input stream to read the response
//        val inputStream = BufferedReader(InputStreamReader(connection.inputStream)).use {
//            // Container for input stream data
//            val response = StringBuffer()
//            var inputLine = it.readLine()
//            // Add each line to the response container
//            while (inputLine != null) {
//                response.append(inputLine)
//                inputLine = it.readLine()
//            }
//            it.close()
//            val mainObject = JSONObject(String(response))
//            val responce_code = mainObject.getString(RESPONCE_CODE_INT)
//            val url = mainObject.getString(URL_STR)
//            val scandate = mainObject.getString(SCAN_DATE_STR)
//            val scans = mainObject.getJSONObject(SCANS_ARR)
//            val i = 2
//
//        }
//        connection.disconnect()
//    }
}