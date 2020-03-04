package oliveira.fabio.challenge52.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import oliveira.fabio.challenge52.persistence.model.entity.WeekEntity

@Dao
interface WeekDao {
    @Transaction
    @Query("UPDATE week SET isDeposited = :isChecked WHERE id = :weekId AND idGoal = :goalId")
    fun updateWeekStatus(goalId: Long, weekId: Long, isChecked: Boolean)

    @Transaction
    @Query("DELETE FROM week WHERE idGoal = :goalId")
    fun removeWeeksByGoalId(goalId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addWeeks(weeks: List<WeekEntity>): List<Long>
}