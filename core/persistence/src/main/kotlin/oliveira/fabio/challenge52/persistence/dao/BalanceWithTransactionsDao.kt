package oliveira.fabio.challenge52.persistence.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import oliveira.fabio.challenge52.persistence.model.entity.BalanceWithTransactionsEntity

@Dao
interface BalanceWithTransactionsDao {
    @Transaction
    @Query("SELECT * FROM balance")
    fun getBalances(): List<BalanceWithTransactionsEntity>
}