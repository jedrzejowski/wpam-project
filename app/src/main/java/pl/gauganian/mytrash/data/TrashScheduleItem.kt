package pl.gauganian.mytrash.data

import java.time.LocalDate

class TrashScheduleItem(date: LocalDate?, fraction: TrashFraction?) {
    var date: LocalDate?
        private set

    var fraction: TrashFraction?
        private set

    init {
        this.date = date
        this.fraction = fraction
    }
}