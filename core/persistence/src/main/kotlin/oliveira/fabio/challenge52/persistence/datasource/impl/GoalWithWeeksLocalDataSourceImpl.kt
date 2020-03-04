package oliveira.fabio.challenge52.persistence.datasource.impl

import oliveira.fabio.challenge52.persistence.dao.GoalWithWeeksDao
import oliveira.fabio.challenge52.persistence.datasource.GoalWithWeeksLocalDataSource

internal class GoalWithWeeksLocalDataSourceImpl(
    private val goalWithWeeksDao: GoalWithWeeksDao
) :
    GoalWithWeeksLocalDataSource {
    override fun getAllOpenedGoalsWithWeeks() = goalWithWeeksDao.getAllOpenedGoalsWithWeeks()
    override fun getAllDoneGoalsWithWeeks() = goalWithWeeksDao.getAllDoneGoalsWithWeeks()
}