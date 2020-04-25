package oliveira.fabio.challenge52.extensions

import java.text.DateFormat
import java.text.NumberFormat
import java.util.*

fun String.toDate(dateFormat: Int): Date {
    val sdf = DateFormat.getDateInstance(dateFormat)
    sdf.isLenient = false
    return sdf.parse(this)
}

fun String.isOnlyNumber(): Boolean = this.matches(Regex("[0-9]+"))

fun String.onlyNumbers(): String = this.replace(Regex("[^0-9]"), "")

fun String.removeInitialFinancial(currentLocale: Locale = Locale.getDefault()): String =
    this.replace(Currency.getInstance(currentLocale).symbol, "")

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