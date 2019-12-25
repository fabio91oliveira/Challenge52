package oliveira.fabio.challenge52.domain.usecase.impl

import oliveira.fabio.challenge52.domain.repository.WeekRepository
import oliveira.fabio.challenge52.domain.usecase.ChangeWeekDepositStatusUseCase
import oliveira.fabio.challenge52.persistence.model.entity.Week

class ChangeWeekDepositStatusUseCaseImpl(
    private val weekRepository: WeekRepository
) : ChangeWeekDepositStatusUseCase {
    override suspend fun invoke(week: Week) {
        with(week) {
            isDeposited = !isDeposited
            weekRepository.updateWeek(this)
        }
    }
}