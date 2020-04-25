package oliveira.fabio.challenge52.persistence.datasource.impl

import oliveira.fabio.challenge52.persistence.dao.GoalWithItemsDao
import oliveira.fabio.challenge52.persistence.datasource.GoalWithWeeksLocalDataSource

internal class GoalWithWeeksLocalDataSourceImpl(
    private val goalWithItemsDao: GoalWithItemsDao
) :
    GoalWithWeeksLocalDataSource {
    override fun getAllOpenedGoalsWithWeeks() = goalWithItemsDao.getAllOpenedGoalsWithWeeks()
    override fun getAllDoneGoalsWithWeeks() = goalWithItemsDao.getAllDoneGoalsWithWeeks()
}