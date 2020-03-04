package oliveira.fabio.challenge52.persistence.datasource

import oliveira.fabio.challenge52.persistence.model.entity.WeekEntity

interface WeekLocalDataSource {
    fun updateWeekStatus(goalId: Long, weekId: Long, isChecked: Boolean)
    fun removeWeeksByIdGoal(goalId: Long)

    fun addWeeks(weeks: List<WeekEntity>): List<Long>
}