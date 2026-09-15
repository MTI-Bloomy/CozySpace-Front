package bloomy.cozyspace.home.components.utils

import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

fun Instant.toDisplayDate(): String {
    val date = this.toLocalDateTime(TimeZone.currentSystemDefault()).date
    val day = date.day.toString().padStart(2, '0')
    val month = date.month.number.toString().padStart(2, '0')
    return "$day/$month/${date.year}"
}
