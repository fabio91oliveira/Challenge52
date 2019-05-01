package oliveira.fabio.challenge52.model.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.persistence.dao.WeekDao
import oliveira.fabio.challenge52.persistence.model.entity.Week

class WeekRepository(private val weekDao: WeekDao) {
    suspend fun addWeeks(weeks: List<Week>) = withContext(Dispatchers.Default) {
        weekDao.addWeeks(weeks)
    }

    suspend fun removeWeeks(weeks: List<Week>) = withContext(Dispatchers.Default) {
        weekDao.deleteWeeks(weeks)
    }

    suspend fun updateWeek(week: Week) = withContext(Dispatchers.Default) {
        weekDao.updateWeek(week)
    }

    suspend fun updateWeeks(weeks: List<Week>) = withContext(Dispatchers.Default) {
        weekDao.updateWeeks(weeks)
    }
}
