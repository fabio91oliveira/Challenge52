package oliveira.fabio.challenge52.organizer.domain.usecase

import oliveira.fabio.challenge52.presentation.vo.Balance

interface GetTransactionsByFilter {
    suspend operator fun invoke(
        balance: Balance,
        value: String
    )
}