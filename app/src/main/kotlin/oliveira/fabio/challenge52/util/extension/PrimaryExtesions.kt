package oliveira.fabio.challenge52.util.extension

import java.text.NumberFormat
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

fun Date.getYearNumber(): Int {
    val cal = Calendar.getInstance()
    cal.time = this

    return cal.get(Calendar.YEAR)
}

fun Date.getMonthNumber(): Int {
    val cal = Calendar.getInstance()
    cal.time = this

    return cal.get(Calendar.MONTH)
}

fun Date.getDayNumber(): Int {
    val cal = Calendar.getInstance()
    cal.time = this

    return cal.get(Calendar.DAY_OF_MONTH)
}

fun Float.toCurrency(): String = NumberFormat.getCurrencyInstance().format((this / 100))