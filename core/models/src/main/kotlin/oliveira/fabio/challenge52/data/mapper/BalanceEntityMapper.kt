package oliveira.fabio.challenge52.data.mapper

import oliveira.fabio.challenge52.persistence.model.entity.BalanceEntity
import oliveira.fabio.challenge52.presentation.vo.Balance

internal interface BalanceEntityMapper {
    operator fun invoke(
        balance: Balance
    ): BalanceEntity
}