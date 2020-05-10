package oliveira.fabio.challenge52.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import oliveira.fabio.challenge52.persistence.model.entity.TransactionEntity

@Dao
interface TransactionDao {
    @Transaction
    @Query("DELETE FROM `transaction` WHERE idBalance = :idBalance and id = :idTransaction")
    fun removeTransactionByIdBalance(idBalance: Long, idTransaction: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTransaction(transactionEntity: TransactionEntity): Long
}