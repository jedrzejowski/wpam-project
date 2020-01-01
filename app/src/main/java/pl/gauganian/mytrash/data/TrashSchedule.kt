package pl.gauganian.mytrash.data

import org.json.JSONObject
import java.time.LocalDate
import kotlin.collections.ArrayList
import pl.gauganian.mytrash.helper.jsonObjectArrayParse


class TrashSchedule(data: JSONObject) {

    val address: String = data.getString("adres")
    val district: String = data.getString("dzielnica")
    var items: ArrayList<TrashScheduleItem> = ArrayList()

//    tu są jeszcze inne pola ale nie wiem na co

//    notatka: "10934-105,gniazdo"
//    typn: "z"
//    uidmgo: "B818BAE8-3BBE-457F-932B-257375C65AD7"
//    x: 5777619
//    y: 7501458

    init {

        items = ArrayList(
            jsonObjectArrayParse(
                data.getJSONArray("harmonogramy")
            ) { item ->
                var date: LocalDate? = null
                if (!item.isNull("data"))
                    date = LocalDate.parse(item.getString("data"))

                val fractionId = item.getJSONObject("frakcja").getString("id_frakcja")
                val fraction: TrashFraction? = TrashFraction.getById(fractionId)

                TrashScheduleItem(date, fraction)
            }.sortedWith(Comparator { a, b ->
                val d1 = a.date
                val d2 = b.date

                if (d1 == null) {
                    if (d2 == null) return@Comparator 0
                    else return@Comparator 1
                } else {
                    if (d2 == null) return@Comparator -1
                    else return@Comparator d1.compareTo(d2)
                }
            })
        )
    }
}
