package oliveira.fabio.challenge52.extensions

import java.text.DateFormat
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

@Deprecated("Use getDateStringByFormat for it")
fun Date.toStringCurrentDateWithFormat(dateFormat: Int): String {
    val sdf = DateFormat.getDateInstance(dateFormat)
    sdf.isLenient = false
    return sdf.format(time)
}

fun Date.getDateStringByFormat(pattern: String): String {
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    return dateFormat.format(this).capitalize()
}

fun Date.getMonthNumber(): Int {
    val cal = Calendar.getInstance()
    cal.time = this

    return cal.get(Calendar.MONTH) + 1
}

fun Date.getMonthName(): String {
    val month = DateFormatSymbols().months[this.getMonthNumber() - 1]
    val firstString = month.substring(0, 1).toUpperCase()
    val restOfAll = month.substring(1, month.length)

    return firstString + restOfAll
}

fun Date.getCurrentYear() = Calendar.getInstance().let {
    val cal = Calendar.getInstance()
    cal.time = this

    cal.get(Calendar.YEAR).toString()
}