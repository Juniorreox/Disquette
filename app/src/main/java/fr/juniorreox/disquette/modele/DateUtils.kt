package fr.juniorreox.disquette.modele

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun fromMillisToTimeString(millis: Long) : String {
        val format = SimpleDateFormat("HH:mm", Locale.FRANCE)
        return format.format(millis)
    }
}