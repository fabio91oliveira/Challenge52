package oliveira.fabio.challenge52.domain.usecase

import oliveira.fabio.challenge52.domain.model.Goal
import oliveira.fabio.challenge52.presentation.vo.TopDetails

interface CreateTopDetailsUseCase {
    suspend operator fun invoke(goal: Goal): TopDetails
}