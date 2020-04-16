package oliveira.fabio.challenge52.persistence.datasource

import oliveira.fabio.challenge52.persistence.model.entity.ItemEntity

interface ItemLocalDataSource {
    fun addItems(itemsList: List<ItemEntity>)
    fun updateItemStatus(
        idGoal: Long,
        idItem: Long,
        isChecked: Boolean
    )

    fun removeItemsByIdGoal(
        idGoal: Long
    )
}