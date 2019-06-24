package oliveira.fabio.challenge52.extensions

import java.text.DateFormat
import java.text.DateFormatSymbols
import java.text.NumberFormat
import java.util.*

fun Calendar.toCurrentDateSystemString(dateFormat: Int): String {
    val sdf = DateFormat.getDateInstance(dateFormat)
    sdf.isLenient = false
    return sdf.format(time)
}

fun Date.toCurrentDateSystemString(dateFormat: Int): String {
    val sdf = DateFormat.getDateInstance(dateFormat)
    sdf.isLenient = false
    return sdf.format(time)
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

fun Float.toCurrency(): String = NumberFormat.getCurrencyInstance().format((this))

fun String.toFloatCurrency(): Float = (this.toFloat() / 100)