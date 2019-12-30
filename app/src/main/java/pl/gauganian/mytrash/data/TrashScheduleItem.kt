package pl.gauganian.mytrash.data

import java.time.LocalDate

class TrashScheduleItem(date: LocalDate?, fraction: TrashFraction?) {
    var date: LocalDate?
        get() = date
        private set

    var fraction: TrashFraction?
        get() = fraction
        private set

    init {
        this.date = date
        this.fraction = fraction
    }
}