package oliveira.fabio.challenge52.organizer.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.organizer.domain.usecase.ChangeTransactionFilterUseCase
import oliveira.fabio.challenge52.organizer.presentation.vo.TypeOfTransactionEnum
import oliveira.fabio.challenge52.presentation.vo.Transaction
import oliveira.fabio.challenge52.presentation.vo.enums.TypeTransactionEnum

internal class ChangeTransactionFilterUseCaseImpl : ChangeTransactionFilterUseCase {
    override suspend fun invoke(
        transactions: MutableList<Transaction>,
        id: Int
    ) = withContext(Dispatchers.Default) {
        val type = when (id) {
            TypeOfTransactionEnum.INCOME.value -> TypeTransactionEnum.INCOME
            TypeOfTransactionEnum.SPENT.value -> TypeTransactionEnum.SPENT
            else -> null
        }
        type?.also {
            return@withContext transactions.filter { it.typeTransaction == type }
        }

        return@withContext transactions
    }
}