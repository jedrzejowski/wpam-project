package pl.gauganian.mytrash.data

import org.json.JSONObject
import java.time.LocalDate
import kotlin.collections.ArrayList
import android.graphics.Bitmap
import android.net.TrafficStats
import android.os.AsyncTask
import android.util.Log
import org.json.JSONArray
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

        for (i in 0 until harmonogramy.length()) {
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
