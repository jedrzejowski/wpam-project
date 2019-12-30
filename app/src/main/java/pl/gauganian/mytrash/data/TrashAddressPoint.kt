package pl.gauganian.mytrash.data

class TrashAddressPoint(id: String, fullName: String) {
    var id: String
    var fullName: String
    var shedule: TrashSchedule? = null

    init {
        this.id = id
        this.fullName = fullName
    }
}