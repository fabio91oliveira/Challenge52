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

fun String.toFloatCurrency(): Float {
    return (Regex("[1-9]\\d*|0\\d+").find(this)?.value.orZero().toFloat())
}

fun String.removeMoneyMask(): String {
    val defaultCurrencySymbol = Currency.getInstance(Locale.getDefault()).symbol
    val regexPattern = "[$defaultCurrencySymbol,.]"

    return Regex(regexPattern).replace(this, "")
}

fun String.toDate(dateFormat: Int): Date {
    val sdf = DateFormat.getDateInstance(dateFormat)
    sdf.isLenient = false
    return sdf.parse(this)
}


//

fun String.isOnlyNumber(): Boolean = this.matches(Regex("[0-9]+"))

fun String.onlyNumbers(): String = this.replace(Regex("[^0-9]"), "")

fun String.removeInitialFinancial(currentLocale: Locale = Locale.getDefault()): String =
    this.replace(Currency.getInstance(currentLocale).symbol, "")

fun String?.orZero(): String = this ?: "0"

fun Number.toStringMoney(
    removeWhiteSpaces: Boolean = false,
    useCurrency: Boolean = true,
    currentLocale: Locale = Locale.getDefault()
): String {
    var formatted = NumberFormat.getCurrencyInstance(currentLocale).format((this))
    if (!useCurrency) {
        formatted = formatted.removeInitialFinancial(currentLocale)
    }

    return if (removeWhiteSpaces) formatted.replace(Regex("\\s"), "") else formatted
}