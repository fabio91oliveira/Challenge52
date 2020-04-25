package oliveira.fabio.challenge52.persistence.model.converter

import androidx.room.TypeConverter
import oliveira.fabio.challenge52.persistence.model.enums.GoalStatusEnum

internal class StatusEnumConverter {

    @TypeConverter
    fun fromCategoryToString(goalStatus: GoalStatusEnum?): String? {
        return goalStatus?.toString()
    }

    @TypeConverter
    fun fromStringToCategory(status: String?): GoalStatusEnum? {
        status?.also {
            return GoalStatusEnum.valueOf(status)
        }
        return null
    }
}