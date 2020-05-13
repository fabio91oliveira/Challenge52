package oliveira.fabio.challenge52.organizer.domain.usecase

import oliveira.fabio.challenge52.presentation.vo.Transaction

interface ChangeTransactionFilterUseCase {
    suspend operator fun invoke(transactions: MutableList<Transaction>, id: Int): List<Transaction>
}