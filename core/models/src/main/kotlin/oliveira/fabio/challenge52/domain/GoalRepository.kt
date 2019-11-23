package oliveira.fabio.challenge52.domain.repository

import oliveira.fabio.challenge52.persistence.model.entity.Goal

interface GoalRepository {
    suspend fun updateGoal(goal: Goal)
    suspend fun removeGoals(goals: List<Goal>): Int
    suspend fun removeGoal(goal: Goal): Int
    suspend fun addGoal(goal: Goal): Long
}