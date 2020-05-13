package oliveira.fabio.challenge52.persistence.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import oliveira.fabio.challenge52.persistence.model.entity.BalanceWithTransactionsEntity
import java.util.*

@Dao
interface BalanceWithTransactionsDao {
    @Transaction
    @Query("SELECT * FROM balance WHERE CAST(strftime('%m', datetime(startDate/1000, 'unixepoch')) AS int) == CAST(strftime('%m', datetime(:date/1000, 'unixepoch')) AS int) AND CAST(strftime('%Y', datetime(startDate/1000, 'unixepoch')) AS int) == CAST(strftime('%Y', datetime(:date/1000, 'unixepoch')) AS int)")
    fun getBalanceByDate(date: Date): BalanceWithTransactionsEntity?
}