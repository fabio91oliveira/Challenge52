package oliveira.fabio.challenge52.domain.usecase

import oliveira.fabio.challenge52.domain.model.Week

interface VerifyAllWeekAreCompletedUseCase {
    suspend operator fun invoke(weeks: List<Week>): Boolean
}