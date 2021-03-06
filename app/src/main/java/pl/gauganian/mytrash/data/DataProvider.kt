package pl.gauganian.mytrash.data

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

typealias ArgMap = HashMap<String, String>

object DataProvider {
    private const val apiUrl = "https://warszawa19115.pl/harmonogramy-wywozu-odpadow"
    private const val magicString =
        "portalCKMjunkschedules_WAR_portalCKMjunkschedulesportlet_INSTANCE_o5AIb2mimbRJ"


    fun baseMapArg(): ArgMap {
        val map = ArgMap()

        map["p_p_id"] = magicString
        map["p_p_lifecycle"] = "2"
        map["p_p_state"] = "normal"
        map["p_p_mode"] = "view"
        map["p_p_cacheability"] = "cacheLevelPage"
        map["p_p_col_id"] = "column-1"
        map["p_p_col_count"] = "2"

        return map
    }

    fun downloadData(args: ArgMap, body: String): JSONArray {

        val params = ArrayList<String>()
        for ((key, value) in args)
            params.add("$key=$value")

//        Log.v("DataProvider", "$apiUrl?${params.joinToString("&")}")
//        Log.v("DataProvider", body)

        return try {
            URL("$apiUrl?${params.joinToString("&")}")
                .openConnection()
                .let {
                    it as HttpURLConnection
                }.apply {
                    //setRequestProperty("Content-Type", "application/json; charset=utf-8")
                    requestMethod = "POST"

                    doOutput = true
                    val outputWriter = OutputStreamWriter(outputStream)
                    outputWriter.write(body)
                    outputWriter.flush()
                }.let {
                    if (it.responseCode == 200) it.inputStream else it.errorStream
                }.let { streamToRead ->
                    BufferedReader(InputStreamReader(streamToRead)).use {
                        val response = StringBuffer()

                        var inputLine = it.readLine()
                        while (inputLine != null) {
                            response.append(inputLine)
                            inputLine = it.readLine()
                        }
                        it.close()
                        response.toString()
                    }
                }.let { string ->
                    JSONArray(string)
                }
        } catch (e: Exception) {
            throw DataProviderException()
        }
    }

    fun downloadSchedule(addressPointId: String): TrashSchedule {
        try {
            val args = baseMapArg()
            args["p_p_resource_id"] = "ajaxResourceURL"
            return TrashSchedule(
                downloadData(
                    args,
                    "_${magicString}_addressPointId=${addressPointId}"
                ).getJSONObject(0)
            )
        } catch (e: Exception) {
            throw DataProviderException()
        }
    }

    fun downloadAutocomplete(string: String): ArrayList<TrashAddressPoint> {
        try {
            val args = baseMapArg()
            args["p_p_resource_id"] = "autocompleteResourceURL"
            val jarray = downloadData(args, "_${magicString}_name=${string}")
            val array = ArrayList<TrashAddressPoint>()

            for (i in 0 until jarray.length())
                array.add(TrashAddressPoint(jarray.getJSONObject(i)))

            return array
        } catch (e: Exception) {
            throw DataProviderException()
        }
    }
}

class DataProviderException : Exception()