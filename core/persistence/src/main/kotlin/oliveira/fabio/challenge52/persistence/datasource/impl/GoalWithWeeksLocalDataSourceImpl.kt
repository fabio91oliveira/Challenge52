package oliveira.fabio.challenge52.persistence.datasource.impl

import oliveira.fabio.challenge52.persistence.dao.GoalWithWeeksDao
import oliveira.fabio.challenge52.persistence.datasource.GoalWithWeeksLocalDataSource

class GoalWithWeeksLocalDataSourceImpl(
    private val goalWithWeeksDao: GoalWithWeeksDao
) :
    GoalWithWeeksLocalDataSource {
    override fun getAllGoalsWithWeeks() = goalWithWeeksDao.getAllGoalsWithWeeks()
    override fun getDoneAllGoalsWithWeeks() = goalWithWeeksDao.getAllDoneGoalsWithWeeks()
}