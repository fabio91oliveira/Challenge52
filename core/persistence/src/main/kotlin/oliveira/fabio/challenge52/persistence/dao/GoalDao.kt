package oliveira.fabio.challenge52.persistence.dao

import androidx.room.*
import oliveira.fabio.challenge52.persistence.model.entity.Goal
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks

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

    @Query("SELECT * FROM goal WHERE goal.isDone = 0")
    fun getAllGoalsWithWeeks(): List<GoalWithWeeks>

    @Query("SELECT * FROM goal WHERE goal.isDone = 1")
    fun getAllDoneGoalsWithWeeks(): List<GoalWithWeeks>
}