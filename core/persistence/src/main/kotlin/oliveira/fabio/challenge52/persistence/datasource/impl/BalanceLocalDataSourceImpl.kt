package oliveira.fabio.challenge52.persistence.datasource.impl

import oliveira.fabio.challenge52.persistence.dao.BalanceDao
import oliveira.fabio.challenge52.persistence.datasource.BalanceLocalDataSource
import oliveira.fabio.challenge52.persistence.model.entity.BalanceEntity

internal class BalanceLocalDataSourceImpl(private val balanceDao: BalanceDao) :
    BalanceLocalDataSource {
    override fun addBalance(balanceEntity: BalanceEntity) = balanceDao.addBalance(balanceEntity)
    override fun setHideOption(idBalance: Long, isHide: Boolean) =
        balanceDao.setHideOption(idBalance, isHide)
}