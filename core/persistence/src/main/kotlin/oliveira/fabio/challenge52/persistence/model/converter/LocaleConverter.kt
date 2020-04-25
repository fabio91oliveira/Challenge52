package oliveira.fabio.challenge52.persistence.model.converter

import androidx.room.TypeConverter
import java.util.*

internal class LocaleConverter {

    @TypeConverter
    fun fromCurrencyToString(locale: Locale?): String? {
        return locale?.let {
            "${it.language},${it.country}"
        }
    }

    @TypeConverter
    fun fromStringToCurrency(locale: String?): Locale? {
        locale?.also {
            val split = it.split(",")
            return Locale(split[0], split[1])
        }
        return null
    }
}