package oliveira.fabio.challenge52.home.goalslists.domain.usecase

import oliveira.fabio.challenge52.presentation.vo.Goal

interface GetAllOpenedGoalsUseCase {
    suspend operator fun invoke(): List<Goal>
}