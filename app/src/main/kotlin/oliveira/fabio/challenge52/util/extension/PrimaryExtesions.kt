package oliveira.fabio.challenge52.util.extension

import java.text.SimpleDateFormat
import java.util.*

fun Calendar.toCurrentFormat(pattern: String): String {
    val sdf = SimpleDateFormat(pattern)
    return sdf.format(time)
}

fun Date.toCurrentFormat(pattern: String): String {
    val sdf = SimpleDateFormat(pattern)
    return sdf.format(time)
}