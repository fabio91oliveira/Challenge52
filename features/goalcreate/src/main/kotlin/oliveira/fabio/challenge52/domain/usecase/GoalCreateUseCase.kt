package oliveira.fabio.challenge52.domain.usecase

import oliveira.fabio.challenge52.persistence.model.entity.Goal

interface GoalCreateUseCase {
    suspend fun addGoal(goal: Goal): Long
    suspend fun addWeeks(goal: Goal, id: Long): List<Long>
}