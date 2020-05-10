package oliveira.fabio.challenge52.domain.repository

import oliveira.fabio.challenge52.presentation.vo.Balance
import java.util.*

interface BalanceWithTransactionsRepository {
    fun getBalanceByDate(date: Date): Balance
}