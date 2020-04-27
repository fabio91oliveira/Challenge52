package oliveira.fabio.challenge52.persistence.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import oliveira.fabio.challenge52.persistence.model.enums.TransactionTypeEnum
import java.io.Serializable
import java.util.*

@Entity(tableName = "transaction")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val idBalance: Long,
    val money: Double,
    val date: Date,
    val type: TransactionTypeEnum
) : Serializable