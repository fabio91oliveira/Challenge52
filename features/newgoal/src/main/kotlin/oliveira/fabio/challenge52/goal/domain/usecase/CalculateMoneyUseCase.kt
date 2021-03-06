package oliveira.fabio.challenge52.goal.domain.usecase

import oliveira.fabio.challenge52.presentation.vo.GoalToSave

interface CalculateMoneyUseCase {
    suspend operator fun invoke(goalToSave: GoalToSave, money: String): Double
}