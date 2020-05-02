package oliveira.fabio.challenge52.goalslists.domain.usecase

import oliveira.fabio.challenge52.presentation.vo.Goal

interface GetAllOpenedGoalsUseCase {
    suspend operator fun invoke(): List<Goal>
}