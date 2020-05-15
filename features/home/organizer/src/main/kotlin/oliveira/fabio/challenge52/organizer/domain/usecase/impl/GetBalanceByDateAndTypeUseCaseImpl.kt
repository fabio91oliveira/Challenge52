package oliveira.fabio.challenge52.organizer.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.repository.BalanceWithTransactionsRepository
import oliveira.fabio.challenge52.organizer.domain.usecase.GetBalanceByDateAndTypeUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.SortTransactionsUseCase
import java.util.*

internal class GetBalanceByDateAndTypeUseCaseImpl(
    private val balanceWithTransactionsRepository: BalanceWithTransactionsRepository,
    private val sortTransactionsUseCase: SortTransactionsUseCase
) : GetBalanceByDateAndTypeUseCase {
    override suspend fun invoke(
        calendar: Calendar
    ) =
        withContext(Dispatchers.IO) {
            delay(ONE_SECOND)
            balanceWithTransactionsRepository.getBalanceByDate(calendar.time).apply {
                sortTransactionsUseCase(transactionsFiltered)
            }
        }

    companion object {
        private const val ONE_SECOND = 1000L
    }
}