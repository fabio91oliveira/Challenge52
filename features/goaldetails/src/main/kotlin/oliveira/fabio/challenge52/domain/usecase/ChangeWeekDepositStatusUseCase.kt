package oliveira.fabio.challenge52.domain.usecase

import oliveira.fabio.challenge52.persistence.model.entity.Week

interface ChangeWeekDepositStatusUseCase {
    suspend operator fun invoke(week: Week)
}