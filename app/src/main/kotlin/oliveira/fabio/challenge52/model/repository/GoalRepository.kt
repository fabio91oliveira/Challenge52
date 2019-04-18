package oliveira.fabio.challenge52.model.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.persistence.dao.GoalDao
import oliveira.fabio.challenge52.persistence.model.entity.Goal

class GoalRepository(private val goalDao: GoalDao) {
    suspend fun addGoal(goal: Goal) = withContext(Dispatchers.Default) {
        goalDao.addGoal(goal)
    }

    suspend fun updateGoal(goal: Goal) = withContext(Dispatchers.Default) {
        goalDao.updateGoal(goal)
    }

    suspend fun removeGoal(goal: Goal) = withContext(Dispatchers.Default) {
        goalDao.deleteGoal(goal)
    }

    suspend fun removeGoals(goalsList: List<Goal>) = withContext(Dispatchers.Default) {
        goalDao.deleteGoals(goalsList)
    }
}
