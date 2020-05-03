package oliveira.fabio.challenge52.goal.domain.usecase

import oliveira.fabio.challenge52.presentation.vo.GoalToSave

interface CreateItemsUseCase {
    suspend operator fun invoke(goalToSave: GoalToSave)
}