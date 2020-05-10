package oliveira.fabio.challenge52.domain.repository

import oliveira.fabio.challenge52.presentation.vo.Balance

interface BalanceRepository {
    fun addBalance(balance: Balance): Long
    fun setHideOption(idBalance: Long, isHide: Boolean)
}