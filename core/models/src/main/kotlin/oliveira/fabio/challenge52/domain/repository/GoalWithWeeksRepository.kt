package oliveira.fabio.challenge52.domain

import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks

interface GoalWithWeeksRepository {
    suspend fun getAllOpenedGoalsWithWeeks(): List<GoalWithWeeks>
    suspend fun getAllDoneGoalsWithWeeks(): List<GoalWithWeeks>
}