package oliveira.fabio.challenge52.util.extension

import java.text.DateFormatSymbols
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

    return cal.get(Calendar.MONTH) + 1
}

fun Date.getDayNumber(): Int {
    val cal = Calendar.getInstance()
    cal.time = this

    return cal.get(Calendar.DAY_OF_MONTH)
}

fun Date.getMonthName(): String {
    val month = DateFormatSymbols().months[this.getMonthNumber() - 1]
    val firsrString = month.substring(0, 1).toUpperCase()
    val restOfAll = month.substring(1, month.length)

    return firsrString + restOfAll
}

fun Float.toCurrency(): String = NumberFormat.getCurrencyInstance().format((this / 100))