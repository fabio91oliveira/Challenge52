package oliveira.fabio.challenge52.data.repository

import oliveira.fabio.challenge52.data.mapper.GoalEntityMapper
import oliveira.fabio.challenge52.domain.repository.GoalRepository
import oliveira.fabio.challenge52.persistence.datasource.GoalLocalDataSource
import oliveira.fabio.challenge52.presentation.vo.GoalToSave

internal class GoalRepositoryImpl(
    private val goalLocalDataSource: GoalLocalDataSource,
    private val goalEntityMapper: GoalEntityMapper
) :
    GoalRepository {
    override fun addGoal(goalToSave: GoalToSave) =
        goalLocalDataSource.addGoal(goalEntityMapper(goalToSave))

    override fun setGoalAsDone(idGoal: Long) =
        goalLocalDataSource.setGoalAsDone(idGoal)

    override fun setGoalAsInProgress(idGoal: Long) =
        goalLocalDataSource.setGoalAsInProgress(idGoal)

    override fun removeGoal(idGoal: Long) {
        goalLocalDataSource.removeGoal(idGoal)
    }
}