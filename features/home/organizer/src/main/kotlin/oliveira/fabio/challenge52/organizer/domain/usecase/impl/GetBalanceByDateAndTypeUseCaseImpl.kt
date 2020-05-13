package oliveira.fabio.challenge52.organizer.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.repository.BalanceWithTransactionsRepository
import oliveira.fabio.challenge52.organizer.domain.usecase.GetBalanceByDateAndTypeUseCase
import java.util.*

internal class GetBalanceByDateAndTypeUseCaseImpl(
    private val balanceWithTransactionsRepository: BalanceWithTransactionsRepository
) : GetBalanceByDateAndTypeUseCase {
    override suspend fun invoke(
        calendar: Calendar
    ) =
        withContext(Dispatchers.IO) {
            delay(ONE_SECOND)
            balanceWithTransactionsRepository.getBalanceByDate(calendar.time).apply {
                transactionsFiltered.sortByDescending { it.id }
            }
        }

    companion object {
        private const val ONE_SECOND = 1000L
    }
}