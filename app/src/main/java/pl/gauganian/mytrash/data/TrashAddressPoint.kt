package pl.gauganian.mytrash.data

import org.json.JSONObject

class TrashAddressPoint {
    var id: String
        private set
    var fullName: String
        private set
    var customName: String
        private set

    constructor(id: String, fullName: String) {
        this.id = id
        this.fullName = fullName
        this.customName = fullName
    }

    constructor(id: String, fullName: String, customTitle: String) {
        this.id = id
        this.fullName = fullName
        this.customName = customTitle
    }

    constructor(json: String?) : this(if (json == null) JSONObject() else JSONObject(json))

    constructor(json: JSONObject?) {
        this.id = json?.optString("addressPointId", "null") ?: "null"
        this.fullName = json?.optString("fullName", "null") ?: "null"
        this.customName = json?.optString("customName", this.fullName) ?: this.fullName
    }

    fun toJSON(): JSONObject {
        val json = JSONObject()

        json.put("addressPointId", id)
        json.put("fullName", fullName)
        json.put("customName", customName)

        return json
    }
}