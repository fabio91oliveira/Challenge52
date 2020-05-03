package oliveira.fabio.challenge52.goal.domain.usecase

import oliveira.fabio.challenge52.presentation.vo.GoalToSave

interface AddGoalUseCase {
    suspend operator fun invoke(goalToSave: GoalToSave): Long
}