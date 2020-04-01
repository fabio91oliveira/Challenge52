package oliveira.fabio.challenge52.persistence.datasource.impl

import oliveira.fabio.challenge52.persistence.dao.ItemDao
import oliveira.fabio.challenge52.persistence.datasource.WeekLocalDataSource
import oliveira.fabio.challenge52.persistence.model.entity.ItemEntity

internal class WeekLocalDataSourceImpl(private val weekDao: ItemDao) : WeekLocalDataSource {
    override fun updateWeekStatus(goalId: Long, weekId: Long, isChecked: Boolean) =
        weekDao.updateWeekStatus(
            goalId, weekId, isChecked
        )

    override fun removeWeeksByIdGoal(goalId: Long) = weekDao.removeWeeksByGoalId(goalId)

    override fun addWeeks(items: List<ItemEntity>) = weekDao.addItems(items)
}