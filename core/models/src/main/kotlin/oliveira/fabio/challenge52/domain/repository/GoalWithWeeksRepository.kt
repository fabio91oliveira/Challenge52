package oliveira.fabio.challenge52.domain

import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks

interface GoalWithWeeksRepository {
    suspend fun getAllGoalsWithWeeks(): List<GoalWithWeeks>
    suspend fun getDoneAllGoalsWithWeeks(): List<GoalWithWeeks>
}