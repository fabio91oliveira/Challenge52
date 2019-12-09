package oliveira.fabio.challenge52.domain.usecase

import oliveira.fabio.challenge52.persistence.model.entity.Goal

interface AddWeeksUseCase {
    suspend operator fun invoke(goal: Goal, id: Long): List<Long>
}