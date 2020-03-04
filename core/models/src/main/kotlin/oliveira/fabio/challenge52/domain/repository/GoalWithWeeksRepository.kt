package oliveira.fabio.challenge52.domain.repository

import oliveira.fabio.challenge52.domain.model.Goal
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeksEntity

interface GoalWithWeeksRepository {
    @Deprecated("Deprecated")
    fun getAllOpenedGoalsWithWeeks(): List<GoalWithWeeksEntity>

    @Deprecated("Deprecated")
    fun getAllDoneGoalsWithWeeks(): List<GoalWithWeeksEntity>

    fun getOpenedGoalsList(): List<Goal>
    fun getDoneGoalsList(): List<Goal>
}