package oliveira.fabio.challenge52.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import oliveira.fabio.challenge52.persistence.model.entity.GoalEntity

@Dao
interface GoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addGoal(goal: GoalEntity): Long

    //
    @Transaction
    @Query("DELETE FROM goal WHERE id = :goalId")
    fun removeGoal(goalId: Long)

    @Transaction
    @Query("UPDATE goal SET isDone = 1 WHERE id = :goalId")
    fun setGoalAsDone(goalId: Long)
}