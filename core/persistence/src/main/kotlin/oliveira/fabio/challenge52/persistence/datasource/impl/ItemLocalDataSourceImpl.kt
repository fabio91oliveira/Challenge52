package oliveira.fabio.challenge52.persistence.datasource.impl

import oliveira.fabio.challenge52.persistence.dao.ItemDao
import oliveira.fabio.challenge52.persistence.datasource.ItemLocalDataSource
import oliveira.fabio.challenge52.persistence.model.entity.ItemEntity

internal class ItemLocalDataSourceImpl(private val itemDao: ItemDao) :
    ItemLocalDataSource {
    override fun addItems(itemsList: List<ItemEntity>) {
        itemDao.addItems(itemsList)
    }

    override fun updateItemStatus(idGoal: Long, idItem: Long, isChecked: Boolean) {
        itemDao.updateItemStatus(idGoal, idItem, isChecked)
    }

    override fun removeItemsByIdGoal(idGoal: Long) {
        itemDao.removeItemsByIdGoal(idGoal)
    }
}