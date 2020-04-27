package oliveira.fabio.challenge52.persistence.model.converter

import androidx.room.TypeConverter
import oliveira.fabio.challenge52.persistence.model.enums.TransactionTypeEnum

internal class TransactionTypeEnumConverter {

    @TypeConverter
    fun fromTransactionTypeToString(transactionTypeEnum: TransactionTypeEnum?): String? {
        return transactionTypeEnum?.toString()
    }

    @TypeConverter
    fun fromStringToCategory(transactionType: String?): TransactionTypeEnum? {
        transactionType?.also {
            return TransactionTypeEnum.valueOf(it)
        }
        return null
    }
}