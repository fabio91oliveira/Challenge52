package oliveira.fabio.challenge52.data.repository

import oliveira.fabio.challenge52.domain.repository.GoalRepository
import oliveira.fabio.challenge52.persistence.datasource.GoalLocalDataSource
import oliveira.fabio.challenge52.persistence.model.entity.Goal

class GoalRepositoryImpl(private val goalLocalDataSource: GoalLocalDataSource) : GoalRepository {
    override fun addGoal(goal: Goal) =
        goalLocalDataSource.addGoal(goal)

    override fun updateGoal(goal: Goal) =
        goalLocalDataSource.updateGoal(goal)

    override fun removeGoal(goal: Goal) =
        goalLocalDataSource.removeGoal(goal)
}