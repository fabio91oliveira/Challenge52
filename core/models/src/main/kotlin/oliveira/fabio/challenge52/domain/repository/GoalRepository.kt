package oliveira.fabio.challenge52.domain.repository

import oliveira.fabio.challenge52.persistence.model.entity.GoalEntity

interface GoalRepository {
    fun addGoal(goal: GoalEntity): Long
    fun removeGoal(idGoal: Long)
    fun setGoalAsDone(idGoal: Long)
}