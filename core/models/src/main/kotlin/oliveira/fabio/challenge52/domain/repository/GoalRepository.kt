package oliveira.fabio.challenge52.domain.repository

import oliveira.fabio.challenge52.presentation.vo.GoalToSave

interface GoalRepository {
    fun addGoal(goalToSave: GoalToSave): Long
    fun setGoalAsDone(idGoal: Long)
    fun setGoalAsInProgress(idGoal: Long)
    fun removeGoal(idGoal: Long)
}