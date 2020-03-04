package oliveira.fabio.challenge52.persistence.datasource.impl

import oliveira.fabio.challenge52.persistence.dao.GoalDao
import oliveira.fabio.challenge52.persistence.datasource.GoalLocalDataSource
import oliveira.fabio.challenge52.persistence.model.entity.GoalEntity

internal class GoalLocalDataSourceImpl(private val goalDao: GoalDao) : GoalLocalDataSource {
    override fun removeGoal(idGoal: Long) = goalDao.removeGoal(idGoal)
    override fun setGoalAsDone(idGoal: Long) = goalDao.setGoalAsDone(idGoal)

    override fun addGoal(goal: GoalEntity) = goalDao.addGoal(goal)
}