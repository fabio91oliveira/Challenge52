package oliveira.fabio.challenge52.domain.usecase

import oliveira.fabio.challenge52.presentation.vo.Item

interface VerifyAllWeekAreCompletedUseCase {
    suspend operator fun invoke(items: List<Item>): Boolean
}