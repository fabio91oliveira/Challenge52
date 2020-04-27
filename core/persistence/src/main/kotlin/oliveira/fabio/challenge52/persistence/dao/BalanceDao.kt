package oliveira.fabio.challenge52.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import oliveira.fabio.challenge52.persistence.model.entity.BalanceEntity

@Dao
interface BalanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addBalance(balanceEntity: BalanceEntity): Long
}