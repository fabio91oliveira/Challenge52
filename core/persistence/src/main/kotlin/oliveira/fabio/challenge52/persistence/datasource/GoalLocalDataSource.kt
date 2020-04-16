package oliveira.fabio.challenge52.persistence.datasource

import oliveira.fabio.challenge52.persistence.model.entity.GoalEntity

interface GoalLocalDataSource {
    fun addGoal(goal: GoalEntity): Long
    fun setGoalAsDone(idGoal: Long)
    fun removeGoal(idGoal: Long)
}