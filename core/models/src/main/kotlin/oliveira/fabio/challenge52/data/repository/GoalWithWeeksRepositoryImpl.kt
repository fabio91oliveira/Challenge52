package oliveira.fabio.challenge52.data.repository

import oliveira.fabio.challenge52.domain.GoalWithWeeksRepository
import oliveira.fabio.challenge52.persistence.datasource.GoalWithWeeksLocalDataSource

class GoalWithWeeksRepositoryImpl(
    private val goalWithWeeksLocalDataSource: GoalWithWeeksLocalDataSource
) :
    GoalWithWeeksRepository {
    override suspend fun getAllOpenedGoalsWithWeeks() =
        goalWithWeeksLocalDataSource.getAllGoalsWithWeeks()

    override suspend fun getAllDoneGoalsWithWeeks() =
        goalWithWeeksLocalDataSource.getDoneAllGoalsWithWeeks()
}