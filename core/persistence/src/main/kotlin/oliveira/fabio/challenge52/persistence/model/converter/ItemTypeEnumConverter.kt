package oliveira.fabio.challenge52.persistence.model.converter

import androidx.room.TypeConverter
import oliveira.fabio.challenge52.persistence.model.enums.ItemTypeEnum

internal class ItemTypeEnumConverter {

    @TypeConverter
    fun fromCategoryToString(type: ItemTypeEnum?): String? {
        return type?.toString()
    }

    @TypeConverter
    fun fromStringToCategory(type: String?): ItemTypeEnum? {
        type?.also {
            return ItemTypeEnum.valueOf(type)
        }
        return null
    }
}