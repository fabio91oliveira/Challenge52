package oliveira.fabio.challenge52.persistence.datasource

import oliveira.fabio.challenge52.persistence.model.entity.GoalEntity

interface GoalLocalDataSource {
    fun removeGoal(idGoal: Long)
    fun setGoalAsDone(idGoal: Long)

    fun addGoal(goal: GoalEntity): Long
}