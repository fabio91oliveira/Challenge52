package oliveira.fabio.challenge52.persistence.datasource.impl

import oliveira.fabio.challenge52.persistence.dao.GoalDao
import oliveira.fabio.challenge52.persistence.datasource.GoalLocalDataSource
import oliveira.fabio.challenge52.persistence.model.entity.Goal

class GoalLocalDataSourceImpl(private val goalDao: GoalDao) : GoalLocalDataSource {
    override fun addGoal(goal: Goal) = goalDao.addGoal(goal)
    override fun updateGoal(goal: Goal) = goalDao.updateGoal(goal)
    override fun removeGoal(goal: Goal) = goalDao.deleteGoal(goal)
}