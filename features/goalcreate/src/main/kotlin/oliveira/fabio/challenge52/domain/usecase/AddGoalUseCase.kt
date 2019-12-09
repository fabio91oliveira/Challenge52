package oliveira.fabio.challenge52.domain.usecase

import oliveira.fabio.challenge52.persistence.model.entity.Goal

interface AddGoalUseCase {
    suspend operator fun invoke(goal: Goal): Long
}