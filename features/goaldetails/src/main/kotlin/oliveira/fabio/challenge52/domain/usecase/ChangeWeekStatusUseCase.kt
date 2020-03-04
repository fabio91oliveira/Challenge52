package oliveira.fabio.challenge52.domain.usecase

import oliveira.fabio.challenge52.domain.model.Week

interface ChangeWeekStatusUseCase {
    suspend operator fun invoke(week: Week)
}