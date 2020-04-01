package oliveira.fabio.challenge52.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import oliveira.fabio.challenge52.persistence.model.entity.ItemEntity

@Dao
interface ItemDao {
    @Transaction
    @Query("UPDATE item SET isSaved = :isChecked WHERE id = :itemId AND idGoal = :goalId")
    fun updateWeekStatus(goalId: Long, itemId: Long, isChecked: Boolean)

    @Transaction
    @Query("DELETE FROM item WHERE idGoal = :goalId")
    fun removeWeeksByGoalId(goalId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addItems(items: List<ItemEntity>): List<Long>
}