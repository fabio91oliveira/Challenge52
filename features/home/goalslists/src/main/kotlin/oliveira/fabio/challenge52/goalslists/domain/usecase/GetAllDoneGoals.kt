package oliveira.fabio.challenge52.goalslists.domain.usecase

import oliveira.fabio.challenge52.presentation.vo.Goal

interface GetAllDoneGoals {
    suspend operator fun invoke(): List<Goal>
}