package oliveira.fabio.challenge52.persistence.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeksEntity

@Dao
interface GoalWithWeeksDao {
    @Transaction
    @Query("SELECT * FROM goal WHERE goal.isDone = 0")
    fun getAllOpenedGoalsWithWeeks(): List<GoalWithWeeksEntity>

    @Transaction
    @Query("SELECT * FROM goal WHERE goal.isDone = 1")
    fun getAllDoneGoalsWithWeeks(): List<GoalWithWeeksEntity>
}