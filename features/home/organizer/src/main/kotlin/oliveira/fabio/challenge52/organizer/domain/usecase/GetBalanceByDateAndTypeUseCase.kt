package oliveira.fabio.challenge52.organizer.domain.usecase

import oliveira.fabio.challenge52.presentation.vo.Balance
import java.util.*

interface GetBalanceByDateAndTypeUseCase {
    suspend operator fun invoke(calendar: Calendar): Balance
}