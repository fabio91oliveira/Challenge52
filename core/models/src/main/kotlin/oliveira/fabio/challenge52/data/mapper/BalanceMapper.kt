package oliveira.fabio.challenge52.data.mapper

import oliveira.fabio.challenge52.persistence.model.entity.BalanceWithTransactionsEntity
import oliveira.fabio.challenge52.presentation.vo.Balance
import java.util.*

internal interface BalanceMapper {
    operator fun invoke(
        balanceWithTransactionsEntity: BalanceWithTransactionsEntity?,
        date: Date
    ): Balance
}