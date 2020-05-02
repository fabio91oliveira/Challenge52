package oliveira.fabio.challenge52.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Date.getDateStringByFormat(pattern: String): String {
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    return dateFormat.format(this)
}