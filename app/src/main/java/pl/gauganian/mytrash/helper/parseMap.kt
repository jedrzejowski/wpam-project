package pl.gauganian.mytrash.helper

import org.json.JSONArray
import org.json.JSONObject


fun <T> jsonObjectArrayParse(string: String, mapper: (obj: JSONObject) -> T): ArrayList<T> {
    return jsonObjectArrayParse(JSONArray(string), mapper)
}

fun <T> jsonObjectArrayParse(jarray: JSONArray, mapper: (obj: JSONObject) -> T): ArrayList<T> {
    val array = ArrayList<T>()

    for (i in 0 until jarray.length()) {
        val item = jarray.getJSONObject(i)

        array.add(mapper(item))
    }

    return array
}

fun <T> jsonObjectArrayDump(array: ArrayList<T>?, mapper: (obj: T) -> JSONObject): JSONArray {
    val jarray = JSONArray()

    if (array == null) return jarray

    for (i in 0 until array.size)
        jarray.put(mapper(array[i]))

    return jarray
}
