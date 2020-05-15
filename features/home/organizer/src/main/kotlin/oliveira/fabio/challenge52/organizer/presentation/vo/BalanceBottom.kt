package oliveira.fabio.challenge52.organizer.presentation.vo

import oliveira.fabio.challenge52.presentation.vo.Transaction
import java.util.*

internal data class BalanceBottom(
    val currentLocale: Locale = Locale.getDefault(),
    val totalAllFilter: Int = 0,
    val totalIncomeFilter: Int = 0,
    val totalSpentFilter: Int = 0,
    val transactions: LinkedList<Transaction> = LinkedList()
)