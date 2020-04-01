package oliveira.fabio.challenge52.persistence.datasource

import oliveira.fabio.challenge52.persistence.model.entity.ItemEntity

interface WeekLocalDataSource {
    fun updateWeekStatus(goalId: Long, weekId: Long, isChecked: Boolean)
    fun removeWeeksByIdGoal(goalId: Long)

    fun addWeeks(items: List<ItemEntity>): List<Long>
}