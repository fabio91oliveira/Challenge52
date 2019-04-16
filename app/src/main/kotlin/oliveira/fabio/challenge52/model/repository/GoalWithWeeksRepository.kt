package oliveira.fabio.challenge52.model.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.persistence.dao.GoalDao

class GoalWithWeeksRepository(private val goalDao: GoalDao) {
    suspend fun getAllGoalsWithWeeks() = withContext(Dispatchers.Default) {
        goalDao.getAllGoalsWithWeeks()
    }

    suspend fun getDoneAllGoalsWithWeeks() = withContext(Dispatchers.Default) {
        goalDao.getAllDoneGoalsWithWeeks()
    }
}
