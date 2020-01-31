package oliveira.fabio.challenge52.persistence.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import oliveira.fabio.challenge52.persistence.model.entity.Goal

@Dao
interface GoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addGoal(goal: Goal): Long

    @Update
    fun updateGoal(goal: Goal)

    @Delete
    fun deleteGoal(goal: Goal): Int

    @Delete
    fun deleteGoals(goalsList: List<Goal>): Int
}