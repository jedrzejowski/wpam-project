package pl.gauganian.mytrash.data

import org.json.JSONObject

class TrashAddressPoint() {
    lateinit var id: String
        private set
    lateinit var fullName: String
        private set
    lateinit var customName: String
        private set

    constructor(id: String, fullName: String) : this() {
        this.id = id
        this.fullName = fullName
        this.customName = fullName
    }

    constructor(id: String, fullName: String, customTitle: String) : this() {
        this.id = id
        this.fullName = fullName
        this.customName = customTitle
    }

    constructor(json: String?) : this(if (json == null) JSONObject() else JSONObject(json))

    constructor(json: JSONObject?) : this() {
        this.id = json?.optString("id", "null") ?: "null"
        this.fullName = json?.optString("fullName", "null") ?: "null"
        this.customName = json?.optString("customName", "null") ?: "null"
    }

    fun toJSON(): JSONObject {
        val json = JSONObject()

        json.put("id", id)
        json.put("fullName", fullName)
        json.put("customName", customName)

        return json
    }
}