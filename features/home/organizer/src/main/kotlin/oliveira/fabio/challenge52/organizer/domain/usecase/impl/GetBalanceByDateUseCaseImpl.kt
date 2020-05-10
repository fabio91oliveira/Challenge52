package oliveira.fabio.challenge52.organizer.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.repository.BalanceWithTransactionsRepository
import oliveira.fabio.challenge52.organizer.domain.usecase.GetBalanceByDateUseCase
import java.util.*

internal class GetBalanceByDateUseCaseImpl(
    private val balanceWithTransactionsRepository: BalanceWithTransactionsRepository
) : GetBalanceByDateUseCase {
    override suspend fun invoke(calendar: Calendar) = withContext(Dispatchers.IO) {
        balanceWithTransactionsRepository.getBalanceByDate(calendar.time)
    }
}