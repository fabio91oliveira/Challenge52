package oliveira.fabio.challenge52.organizer.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.repository.BalanceRepository
import oliveira.fabio.challenge52.domain.usecase.GetCurrentLocaleUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.CreateBalanceUseCase
import oliveira.fabio.challenge52.presentation.vo.Balance

internal class CreateBalanceUseCaseImpl(
    private val balanceRepository: BalanceRepository,
    private val getCurrentLocaleUseCase: GetCurrentLocaleUseCase
) : CreateBalanceUseCase {
    override suspend fun invoke(balance: Balance) = withContext(Dispatchers.IO) {
        setCurrentLocale(balance)
        balanceRepository.addBalance(balance)
    }

    private suspend fun setCurrentLocale(balance: Balance) {
        balance.currentLocale = getCurrentLocaleUseCase()
    }
}