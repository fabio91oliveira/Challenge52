package oliveira.fabio.challenge52.model.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.persistence.dao.GoalDao
import oliveira.fabio.challenge52.persistence.dao.WeekDao
import oliveira.fabio.challenge52.persistence.model.entity.Goal
import oliveira.fabio.challenge52.persistence.model.entity.Week

class GoalWithWeeksRepository(private val goalDao: GoalDao, private val weekDao: WeekDao) {
    suspend fun addGoal(goal: Goal) = withContext(Dispatchers.Default) {
        goalDao.addGoal(goal)
    }

    suspend fun removeGoals(goalsList: List<Goal>) = withContext(Dispatchers.Default) {
        goalDao.deleteGoals(goalsList)
    }

    suspend fun addWeeks(weeks: List<Week>) = withContext(Dispatchers.Default) {
        weekDao.addWeeks(weeks)
    }

    suspend fun removeWeeks(weeks: List<Week>) = withContext(Dispatchers.Default) {
        weekDao.deleteWeeks(weeks)
    }

    suspend fun updateWeek(week: Week) = withContext(Dispatchers.Default) {
        weekDao.updateWeek(week)
    }

    suspend fun getAllGoalsWithWeeks() = withContext(Dispatchers.Default) {
        goalDao.getAllGoalsWithWeeks()
    }

    suspend fun getDoneAllGoalsWithWeeks() = withContext(Dispatchers.Default) {
        goalDao.getAllDoneGoalsWithWeeks()
    }
}
