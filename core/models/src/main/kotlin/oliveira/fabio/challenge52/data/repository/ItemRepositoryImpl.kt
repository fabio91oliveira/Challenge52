package oliveira.fabio.challenge52.data.repository

import oliveira.fabio.challenge52.domain.mapper.ItemEntityMapper
import oliveira.fabio.challenge52.domain.repository.ItemRepository
import oliveira.fabio.challenge52.persistence.datasource.ItemLocalDataSource
import oliveira.fabio.challenge52.presentation.vo.GoalToSave

internal class ItemRepositoryImpl(
    private val itemLocalDataSource: ItemLocalDataSource,
    private val itemEntityMapper: ItemEntityMapper
) : ItemRepository {
    override fun addItems(
        goalToSave: GoalToSave,
        idGoal: Long
    ) {
        itemLocalDataSource.addItems(itemEntityMapper(goalToSave, idGoal))
    }

    override fun updateItemStatus(
        idGoal: Long,
        idItem: Long,
        isChecked: Boolean
    ) = itemLocalDataSource.updateItemStatus(idGoal, idItem, isChecked)

    override fun removeItemsByIdGoal(idGoal: Long) = itemLocalDataSource.removeItemsByIdGoal(idGoal)
}