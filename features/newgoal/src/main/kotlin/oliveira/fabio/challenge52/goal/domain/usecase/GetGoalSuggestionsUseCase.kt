package oliveira.fabio.challenge52.goal.domain.usecase

import oliveira.fabio.challenge52.goal.presentation.vo.GoalSuggestion

interface GetGoalSuggestionsUseCase {
    suspend operator fun invoke(): List<GoalSuggestion>
}