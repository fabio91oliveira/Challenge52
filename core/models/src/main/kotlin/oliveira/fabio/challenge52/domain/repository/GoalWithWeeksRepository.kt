package oliveira.fabio.challenge52.domain.repository

import oliveira.fabio.challenge52.domain.model.Goal
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithItemsEntity

interface GoalWithWeeksRepository {
    @Deprecated("Deprecated")
    fun getAllOpenedGoalsWithWeeks(): List<GoalWithItemsEntity>

    @Deprecated("Deprecated")
    fun getAllDoneGoalsWithWeeks(): List<GoalWithItemsEntity>

    fun getOpenedGoalsList(): List<Goal>
    fun getDoneGoalsList(): List<Goal>
}