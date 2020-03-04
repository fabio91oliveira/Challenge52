package oliveira.fabio.challenge52.persistence.datasource.impl

import oliveira.fabio.challenge52.persistence.dao.WeekDao
import oliveira.fabio.challenge52.persistence.datasource.WeekLocalDataSource
import oliveira.fabio.challenge52.persistence.model.entity.WeekEntity

internal class WeekLocalDataSourceImpl(private val weekDao: WeekDao) : WeekLocalDataSource {
    override fun updateWeekStatus(goalId: Long, weekId: Long, isChecked: Boolean) =
        weekDao.updateWeekStatus(
            goalId, weekId, isChecked
        )

    override fun removeWeeksByIdGoal(goalId: Long) = weekDao.removeWeeksByGoalId(goalId)

    override fun addWeeks(weeks: List<WeekEntity>) = weekDao.addWeeks(weeks)
}