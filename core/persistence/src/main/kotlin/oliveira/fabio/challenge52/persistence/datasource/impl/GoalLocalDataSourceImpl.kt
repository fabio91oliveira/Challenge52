package oliveira.fabio.challenge52.persistence.datasource.impl

import oliveira.fabio.challenge52.persistence.dao.GoalDao
import oliveira.fabio.challenge52.persistence.datasource.GoalLocalDataSource
import oliveira.fabio.challenge52.persistence.model.entity.GoalEntity
import oliveira.fabio.challenge52.persistence.model.enums.GoalStatusEnum

internal class GoalLocalDataSourceImpl(private val goalDao: GoalDao) : GoalLocalDataSource {
    override fun addGoal(goal: GoalEntity) = goalDao.addGoal(goal)
    override fun setGoalAsDone(idGoal: Long) = goalDao.setGoalStatus(idGoal, GoalStatusEnum.DONE)
    override fun setGoalAsInProgress(idGoal: Long) =
        goalDao.setGoalStatus(idGoal, GoalStatusEnum.IN_PROGRESS)

    override fun removeGoal(idGoal: Long) = goalDao.removeGoal(idGoal)
}