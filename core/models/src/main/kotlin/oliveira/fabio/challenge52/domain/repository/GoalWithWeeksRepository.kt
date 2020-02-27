package oliveira.fabio.challenge52.domain.repository

import oliveira.fabio.challenge52.domain.model.Goal
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks

interface GoalWithWeeksRepository {
    @Deprecated("Deprecated")
    fun getAllOpenedGoalsWithWeeks(): List<GoalWithWeeks>

    @Deprecated("Deprecated")
    fun getAllDoneGoalsWithWeeks(): List<GoalWithWeeks>

    fun getOpenedGoalsList(): List<Goal>
    fun getDoneGoalsList(): List<Goal>
}