package pl.gauganian.mytrash.data

import pl.gauganian.mytrash.R

class TrashFraction(id: String, code: String, title: Int, name: Int, desc: Int, icon: Int) {
    var id: String
        get() = id
        private set

    var code: String
        get() = code
        private set

    var title: Int
        get() = title
        private set

    var name: Int
        get() = name
        private set

    var desc: Int
        get() = desc
        private set

    var icon: Int
        get() = icon
        private set

    init {
        this.id = id
        this.code = code
        this.title = title
        this.name = name
        this.desc = desc
        this.icon = icon
    }

    companion object {

        val BG = TrashFraction(
            "BG",
            "20 01 08",
            R.string.trash_fraction_bg_title,
            R.string.trash_fraction_bg_name,
            R.string.trash_fraction_bg_desc,
            R.drawable.ic_trash_fraction_bg
        )

        val BK = TrashFraction(
            "OZ",
            "20 02 01",
            R.string.trash_fraction_bk_title,
            R.string.trash_fraction_bk_name,
            R.string.trash_fraction_bk_desc,
            R.drawable.ic_trash_fraction_bk
        )

        val MT = TrashFraction(
            "MT",
            "15 01 06",
            R.string.trash_fraction_mt_title,
            R.string.trash_fraction_mt_name,
            R.string.trash_fraction_mt_desc,
            R.drawable.ic_trash_fraction_mt
        )

        val OP = TrashFraction(
            "OP",
            "15 01 01",
            R.string.trash_fraction_op_title,
            R.string.trash_fraction_op_name,
            R.string.trash_fraction_op_desc,
            R.drawable.ic_trash_fraction_op
        )

        val OS = TrashFraction(
            "OS",
            "15 01 07",
            R.string.trash_fraction_os_title,
            R.string.trash_fraction_os_name,
            R.string.trash_fraction_os_desc,
            R.drawable.ic_trash_fraction_os
        )

        val OZ = TrashFraction(
            "OZ",
            "20 02 01",
            R.string.trash_fraction_oz_title,
            R.string.trash_fraction_oz_name,
            R.string.trash_fraction_oz_desc,
            R.drawable.ic_trash_fraction_oz
        )

        val WG = TrashFraction(
            "WG",
            "20 03 07",
            R.string.trash_fraction_wg_title,
            R.string.trash_fraction_wg_name,
            R.string.trash_fraction_wg_desc,
            R.drawable.ic_trash_fraction_wg
        )

        val ZM = TrashFraction(
            "ZM",
            "20 02 01",
            R.string.trash_fraction_zm_title,
            R.string.trash_fraction_zm_name,
            R.string.trash_fraction_zm_desc,
            R.drawable.ic_trash_fraction_zm
        )

        private var list = arrayOf(BG, BK, MT, OP, OS, OZ, WG, ZM)

        fun getAll(): Array<TrashFraction> {

            return list
        }

        fun getById(id: String): TrashFraction? {
            return list.find { fraction -> fraction.id == id }
        }
    }
}