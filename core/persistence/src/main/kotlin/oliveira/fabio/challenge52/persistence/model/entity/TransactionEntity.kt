package oliveira.fabio.challenge52.persistence.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import oliveira.fabio.challenge52.persistence.model.enums.TransactionTypeEnum
import java.io.Serializable
import java.util.*

@Entity(tableName = "transaction")
data class TransactionEntity(
    var idBalance: Long,
    var icoResource: String,
    var description: String,
    var money: Double,
    var date: Date,
    var type: TransactionTypeEnum
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}