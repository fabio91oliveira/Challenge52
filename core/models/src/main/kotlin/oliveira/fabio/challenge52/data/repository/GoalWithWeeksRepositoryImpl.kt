package oliveira.fabio.challenge52.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.GoalWithWeeksRepository
import oliveira.fabio.challenge52.persistence.datasource.GoalWithWeeksLocalDataSource

class GoalWithWeeksRepositoryImpl(
    private val goalWithWeeksLocalDataSource: GoalWithWeeksLocalDataSource
) :
    GoalWithWeeksRepository {
    override suspend fun getAllOpenedGoalsWithWeeks() = withContext(Dispatchers.Default) {
        goalWithWeeksLocalDataSource.getAllGoalsWithWeeks()
    }

    override suspend fun getAllDoneGoalsWithWeeks() = withContext(Dispatchers.Default) {
        goalWithWeeksLocalDataSource.getDoneAllGoalsWithWeeks()
    }
}