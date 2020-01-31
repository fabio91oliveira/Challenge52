package oliveira.fabio.challenge52.persistence.datasource

import oliveira.fabio.challenge52.persistence.model.entity.Goal

interface GoalLocalDataSource {
    fun addGoal(goal: Goal): Long
    fun updateGoal(goal: Goal)
    fun removeGoal(goal: Goal): Int
    fun removeGoals(goalsList: List<Goal>): Int
}