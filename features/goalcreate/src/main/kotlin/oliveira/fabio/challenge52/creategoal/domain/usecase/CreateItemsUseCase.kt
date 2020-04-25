package oliveira.fabio.challenge52.creategoal.domain.usecase

import oliveira.fabio.challenge52.presentation.vo.GoalToSave

interface CreateItemsUseCase {
    suspend operator fun invoke(goalToSave: GoalToSave)
}