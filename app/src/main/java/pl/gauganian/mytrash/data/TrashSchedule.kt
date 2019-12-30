package pl.gauganian.mytrash.data

import org.json.JSONObject
import java.time.LocalDate
import kotlin.collections.ArrayList
import android.graphics.Bitmap
import android.os.AsyncTask
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL


class TrashSchedule(data: JSONObject) {

    var address: String
    var district: String
    var fractions: ArrayList<TrashScheduleItem> = ArrayList()

//    tu są jeszcze inne pola ale nie wiem na co

//    notatka: "10934-105,gniazdo"
//    typn: "z"
//    uidmgo: "B818BAE8-3BBE-457F-932B-257375C65AD7"
//    x: 5777619
//    y: 7501458

    init {
        address = data.getString("adres")
        district = data.getString("dzielnica")

        val harmonogramy = data.getJSONArray("harmonogramy")

        for (i in 0 until harmonogramy.length() - 1) {
            val item = harmonogramy.getJSONObject(i)

            var date: LocalDate? = null
            if (!item.isNull("data"))
                date = LocalDate.parse(item.getString("data"))

            val fractionId = item.getJSONObject("frakcja").getString("id_frakcja")
            val fraction: TrashFraction? = TrashFraction.getById(fractionId)

            fractions.add(TrashScheduleItem(date, fraction))
        }
    }
}

class TrashScheduleDownloader : AsyncTask<String, Void, TrashSchedule?>() {
//    override fun onPostExecute(bitmap: Bitmap) {
//        //Populate Ui
//        super.onPostExecute(bitmap)
//    }
//
//    override fun onPreExecute() {
//        // Show progress dialog
//        super.onPreExecute()
//    }

    override fun doInBackground(vararg params: String): TrashSchedule? {

        Log.v("MY tag", "start")
        try {

            val str = URL("https://google.com")
                .openConnection()
                .let {
                    it as HttpURLConnection
                }.apply {
                    //                setRequestProperty("Content-Type", "application/json; charset=utf-8")
                    requestMethod = "GET"

                    doOutput = true
                    val outputWriter = OutputStreamWriter(outputStream)
                    outputWriter.write("")
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
                }

            Log.v("MY tag", str)
        } catch (t: Throwable) {
            Log.w("MY TAG", "Failed to ${t.message}", t)
        }


        Log.v("MY tag", "end")

        return null
    }

    override fun onProgressUpdate(vararg values: Void) {
        // Show progress update
        super.onProgressUpdate(*values)
    }
}
