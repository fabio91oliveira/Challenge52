package oliveira.fabio.challenge52.persistence.datasource.impl

import oliveira.fabio.challenge52.persistence.dao.WeekDao
import oliveira.fabio.challenge52.persistence.datasource.WeekLocalDataSource
import oliveira.fabio.challenge52.persistence.model.entity.Week

class WeekLocalDataSourceImpl(private val weekDao: WeekDao): WeekLocalDataSource {
    override fun addWeeks(weeks: List<Week>) = weekDao.addWeeks(weeks)
    override fun removeWeeks(weeks: List<Week>) = weekDao.deleteWeeks(weeks)
    override fun updateWeek(week: Week) = weekDao.updateWeek(week)
    override fun updateWeeks(weeks: List<Week>) = weekDao.updateWeeks(weeks)
}