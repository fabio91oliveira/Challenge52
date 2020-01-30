package oliveira.fabio.challenge52.data.repository

import oliveira.fabio.challenge52.domain.repository.WeekRepository
import oliveira.fabio.challenge52.persistence.datasource.WeekLocalDataSource
import oliveira.fabio.challenge52.persistence.model.entity.Week

class WeekRepositoryImpl(private val weekLocalDataSource: WeekLocalDataSource) : WeekRepository {
    override suspend fun addWeeks(weeks: List<Week>) =
        weekLocalDataSource.addWeeks(weeks)

    override suspend fun removeWeeks(weeks: List<Week>) =
        weekLocalDataSource.removeWeeks(weeks)

    override suspend fun updateWeek(week: Week) =
        weekLocalDataSource.updateWeek(week)

    override suspend fun updateWeeks(weeks: List<Week>) =
        weekLocalDataSource.updateWeeks(weeks)
}