package oliveira.fabio.challenge52.domain.usecase

import oliveira.fabio.challenge52.domain.model.Goal

interface SetGoalAsDoneUseCase {
    suspend operator fun invoke(goal: Goal)
}