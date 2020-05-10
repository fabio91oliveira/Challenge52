package oliveira.fabio.challenge52.data.mapper.impl

import oliveira.fabio.challenge52.data.mapper.BalanceEntityMapper
import oliveira.fabio.challenge52.persistence.model.entity.BalanceEntity
import oliveira.fabio.challenge52.presentation.vo.Balance

internal class BalanceEntityMapperImpl :
    BalanceEntityMapper {
    override fun invoke(
        balance: Balance
    ) = BalanceEntity(
        currentLocale = balance.currentLocale ?: throw NullPointerException(),
        startDate = balance.date ?: throw NullPointerException()
    )
}