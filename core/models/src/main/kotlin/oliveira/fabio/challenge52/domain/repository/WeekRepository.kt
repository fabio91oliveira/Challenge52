package oliveira.fabio.challenge52.domain.repository

import oliveira.fabio.challenge52.persistence.model.entity.ItemEntity

interface WeekRepository {
    fun updateWeekStatus(
        goalId: Long,
        weekId: Long,
        isChecked: Boolean
    )

    fun removeWeeksByIdGoal(idGoal: Long)

    fun addWeeks(items: List<ItemEntity>): List<Long>
}