package oliveira.fabio.challenge52.home.goalslists.domain.usecase

import oliveira.fabio.challenge52.domain.model.Goal

interface GetAllDoneGoals {
    suspend operator fun invoke(): List<Goal>
}