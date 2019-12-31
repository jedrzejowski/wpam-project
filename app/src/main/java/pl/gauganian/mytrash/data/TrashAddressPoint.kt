package pl.gauganian.mytrash.data

import org.json.JSONObject

class TrashAddressPoint() {
    lateinit var id: String
        private set
    lateinit var fullName: String
        private set

    constructor(id: String, fullName: String) : this() {
        this.id = id
        this.fullName = fullName
    }

    constructor(json: String?) : this(if (json == null) JSONObject() else JSONObject(json))

    constructor(json: JSONObject?) : this() {
        this.id = json?.optString("id", "null") ?: "null"
        this.fullName = json?.optString("fullName", "null") ?: "null"
    }

    fun toJSON(): JSONObject {
        val json = JSONObject()

        json.put("id", id)
        json.put("fullName", fullName)

        return json
    }
}