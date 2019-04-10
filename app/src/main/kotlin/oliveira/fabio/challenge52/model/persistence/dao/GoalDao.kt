package oliveira.fabio.challenge52.model.persistence.dao

import androidx.room.*
import oliveira.fabio.challenge52.model.entity.Goal
import oliveira.fabio.challenge52.model.vo.GoalWithWeeks

@Dao
interface GoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addGoal(goal: Goal): Long

    @Delete
    fun deleteGoals(goalsList: List<Goal>): Int

    @Query("SELECT * FROM goal")
    fun getAllGoalsWithWeeks(): List<GoalWithWeeks>

    @Query("SELECT * FROM goal WHERE goal.isDone = 1")
    fun getAllDoneGoalsWithWeeks(): List<GoalWithWeeks>
}