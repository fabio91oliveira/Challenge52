package oliveira.fabio.challenge52.persistence.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import oliveira.fabio.challenge52.persistence.model.entity.GoalWithItemsEntity
import oliveira.fabio.challenge52.persistence.model.enums.GoalStatusEnum

@Dao
interface GoalWithItemsDao {
    @Transaction
    @Query("SELECT * FROM goal WHERE goalStatus != :goalStatusDone")
    fun getAllOpenedGoalsWithWeeks(goalStatusDone: GoalStatusEnum = GoalStatusEnum.DONE): List<GoalWithItemsEntity>

    @Transaction
    @Query("SELECT * FROM goal WHERE goalStatus = :goalStatusDone")
    fun getAllDoneGoalsWithWeeks(goalStatusDone: GoalStatusEnum = GoalStatusEnum.DONE): List<GoalWithItemsEntity>
}