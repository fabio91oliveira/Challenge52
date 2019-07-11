package oliveira.fabio.challenge52.domain.interactor

import oliveira.fabio.challenge52.persistence.model.entity.Goal

interface GoalCreateInteractor {
    suspend fun addGoal(goal: Goal): Long
    suspend fun addWeeks(goal: Goal, totalWeeks: Int): List<Long>
}