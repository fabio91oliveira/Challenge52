package oliveira.fabio.challenge52.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.repository.GoalRepository
import oliveira.fabio.challenge52.persistence.datasource.GoalLocalDataSource
import oliveira.fabio.challenge52.persistence.model.entity.Goal

class GoalRepositoryImpl(private val goalLocalDataSource: GoalLocalDataSource) : GoalRepository {
    override suspend fun addGoal(goal: Goal) = withContext(Dispatchers.Default) {
        goalLocalDataSource.addGoal(goal)
    }
    override suspend fun updateGoal(goal: Goal) = withContext(Dispatchers.Default) {
        goalLocalDataSource.updateGoal(goal)
    }

    override suspend fun removeGoal(goal: Goal) = withContext(Dispatchers.Default) {
        goalLocalDataSource.removeGoal(goal)
    }
    override suspend fun removeGoals(goals: List<Goal>) = withContext(Dispatchers.Default) {
        goalLocalDataSource.removeGoals(goals)
    }
}