package oliveira.fabio.challenge52.data.mapper

import oliveira.fabio.challenge52.persistence.model.entity.TransactionEntity
import oliveira.fabio.challenge52.presentation.vo.Transaction

internal interface TransactionEntityMapper {
    operator fun invoke(
        transaction: Transaction
    ): TransactionEntity
}