package oliveira.fabio.challenge52.data.mapper.impl

import oliveira.fabio.challenge52.data.mapper.TransactionEntityMapper
import oliveira.fabio.challenge52.persistence.model.entity.TransactionEntity
import oliveira.fabio.challenge52.persistence.model.enums.TransactionTypeEnum
import oliveira.fabio.challenge52.presentation.vo.Transaction
import oliveira.fabio.challenge52.presentation.vo.enums.TypeTransactionEnum

internal class TransactionEntityMapperImpl :
    TransactionEntityMapper {
    override fun invoke(transaction: Transaction) =
        TransactionEntity(
            idBalance = transaction.idBalance,
            icoResource = transaction.icoResource,
            description = transaction.description,
            money = transaction.money,
            date = transaction.date,
            type = getType(transaction.typeTransaction)
        )

    private fun getType(transactionType: TypeTransactionEnum): TransactionTypeEnum {
        return when (transactionType) {
            TypeTransactionEnum.INCOME -> TransactionTypeEnum.INCOME
            else -> TransactionTypeEnum.SPENT
        }

    }
}