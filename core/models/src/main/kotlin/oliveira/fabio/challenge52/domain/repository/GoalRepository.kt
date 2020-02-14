package oliveira.fabio.challenge52.domain.repository

import oliveira.fabio.challenge52.persistence.model.entity.Goal

interface GoalRepository {
    fun updateGoal(goal: Goal)
    fun removeGoals(goals: List<Goal>): Int
    fun removeGoal(goal: Goal): Int
    fun addGoal(goal: Goal): Long
}