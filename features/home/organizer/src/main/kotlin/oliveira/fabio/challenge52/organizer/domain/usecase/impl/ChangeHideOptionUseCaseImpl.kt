package oliveira.fabio.challenge52.organizer.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.repository.BalanceRepository
import oliveira.fabio.challenge52.organizer.domain.usecase.ChangeHideOptionUseCase

internal class ChangeHideOptionUseCaseImpl(
    private val balanceRepository: BalanceRepository
) : ChangeHideOptionUseCase {
    override suspend fun invoke(idBalance: Long, isHide: Boolean) = withContext(Dispatchers.IO) {
        balanceRepository.setHideOption(idBalance, isHide)
    }
}