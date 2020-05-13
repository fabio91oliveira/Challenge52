package oliveira.fabio.challenge52.persistence.datasource

import oliveira.fabio.challenge52.persistence.model.entity.BalanceEntity

interface BalanceLocalDataSource {
    fun addBalance(balanceEntity: BalanceEntity): Long
    fun setHideOption(idBalance: Long, isHide: Boolean)
}