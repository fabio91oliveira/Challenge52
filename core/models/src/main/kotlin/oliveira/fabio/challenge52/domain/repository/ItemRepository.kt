package oliveira.fabio.challenge52.domain.repository

import oliveira.fabio.challenge52.presentation.vo.GoalToSave

interface ItemRepository {
    fun addItems(
        goalToSave: GoalToSave,
        idGoal: Long
    )

    fun updateItemStatus(
        idGoal: Long,
        idItem: Long,
        isChecked: Boolean
    )

    fun removeItemsByIdGoal(
        idGoal: Long
    )
}