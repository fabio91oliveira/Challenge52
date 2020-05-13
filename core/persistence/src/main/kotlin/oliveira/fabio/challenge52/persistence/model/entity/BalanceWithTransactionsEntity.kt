package oliveira.fabio.challenge52.persistence.model.entity

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable

class BalanceWithTransactionsEntity : Serializable {
    @Embedded
    lateinit var balance: BalanceEntity

    @Relation(
        parentColumn = "id",
        entityColumn = "idBalance",
        entity = TransactionEntity::class
    )
    lateinit var transactions: List<TransactionEntity>
}