package oliveira.fabio.challenge52.data.repository

import oliveira.fabio.challenge52.data.mapper.BalanceEntityMapper
import oliveira.fabio.challenge52.domain.repository.BalanceRepository
import oliveira.fabio.challenge52.persistence.datasource.BalanceLocalDataSource
import oliveira.fabio.challenge52.presentation.vo.Balance

internal class BalanceRepositoryImpl(
    private val balanceLocalDataSource: BalanceLocalDataSource,
    private val balanceEntityMapper: BalanceEntityMapper
) :
    BalanceRepository {
    override fun addBalance(balance: Balance) =
        balanceLocalDataSource.addBalance(balanceEntityMapper(balance))

    override fun setHideOption(idBalance: Long, isHide: Boolean) {
        balanceLocalDataSource.setHideOption(idBalance, isHide)
    }
}