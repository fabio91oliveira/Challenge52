package oliveira.fabio.challenge52.data.repository

import oliveira.fabio.challenge52.domain.repository.WeekRepository
import oliveira.fabio.challenge52.persistence.datasource.WeekLocalDataSource
import oliveira.fabio.challenge52.persistence.model.entity.WeekEntity

internal class WeekRepositoryImpl(private val weekLocalDataSource: WeekLocalDataSource) : WeekRepository {
    override fun updateWeekStatus(goalId: Long, weekId: Long, isChecked: Boolean) =
        weekLocalDataSource.updateWeekStatus(goalId, weekId, isChecked)

    override fun removeWeeksByIdGoal(idGoal: Long) = weekLocalDataSource.removeWeeksByIdGoal(idGoal)

    override fun addWeeks(weeks: List<WeekEntity>) =
        weekLocalDataSource.addWeeks(weeks)
}