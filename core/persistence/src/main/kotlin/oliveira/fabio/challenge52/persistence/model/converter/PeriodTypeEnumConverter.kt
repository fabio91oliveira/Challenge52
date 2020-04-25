package oliveira.fabio.challenge52.persistence.model.converter

import androidx.room.TypeConverter
import oliveira.fabio.challenge52.persistence.model.enums.PeriodTypeEnum

internal class PeriodTypeEnumConverter {

    @TypeConverter
    fun fromCategoryToString(type: PeriodTypeEnum?): String? {
        return type?.toString()
    }

    @TypeConverter
    fun fromStringToCategory(type: String?): PeriodTypeEnum? {
        type?.also {
            return PeriodTypeEnum.valueOf(type)
        }
        return null
    }
}