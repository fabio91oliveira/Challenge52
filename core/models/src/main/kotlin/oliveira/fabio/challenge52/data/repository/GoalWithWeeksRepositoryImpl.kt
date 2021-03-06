package oliveira.fabio.challenge52.data.repository

import oliveira.fabio.challenge52.data.mapper.GoalMapper
import oliveira.fabio.challenge52.domain.repository.GoalWithWeeksRepository
import oliveira.fabio.challenge52.persistence.datasource.GoalWithWeeksLocalDataSource

internal class GoalWithWeeksRepositoryImpl(
    private val goalWithWeeksLocalDataSource: GoalWithWeeksLocalDataSource,
    private val goalMapper: GoalMapper
) :
    GoalWithWeeksRepository {
    override fun getOpenedGoalsList() =
        goalWithWeeksLocalDataSource.getAllOpenedGoalsWithWeeks().map { goalMapper(it) }

    override fun getDoneGoalsList() =
        goalWithWeeksLocalDataSource.getAllDoneGoalsWithWeeks().map { goalMapper(it) }
}