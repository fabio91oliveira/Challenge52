package oliveira.fabio.challenge52.data.repository

import oliveira.fabio.challenge52.domain.repository.GoalWithWeeksRepository
import oliveira.fabio.challenge52.persistence.datasource.GoalWithWeeksLocalDataSource

class GoalWithWeeksRepositoryImpl(
    private val goalWithWeeksLocalDataSource: GoalWithWeeksLocalDataSource
) :
    GoalWithWeeksRepository {
    override fun getAllOpenedGoalsWithWeeks() =
        goalWithWeeksLocalDataSource.getAllGoalsWithWeeks()

    override fun getAllDoneGoalsWithWeeks() =
        goalWithWeeksLocalDataSource.getDoneAllGoalsWithWeeks()
}