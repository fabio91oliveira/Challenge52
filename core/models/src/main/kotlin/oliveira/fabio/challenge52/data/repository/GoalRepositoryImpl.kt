package oliveira.fabio.challenge52.data.repository

import oliveira.fabio.challenge52.domain.repository.GoalRepository
import oliveira.fabio.challenge52.persistence.datasource.GoalLocalDataSource
import oliveira.fabio.challenge52.persistence.model.entity.GoalEntity

internal class GoalRepositoryImpl(private val goalLocalDataSource: GoalLocalDataSource) : GoalRepository {
    override fun removeGoal(idGoal: Long) = goalLocalDataSource.removeGoal(idGoal)
    override fun setGoalAsDone(idGoal: Long) = goalLocalDataSource.setGoalAsDone(idGoal)

    override fun addGoal(goal: GoalEntity) =
        goalLocalDataSource.addGoal(goal)
}