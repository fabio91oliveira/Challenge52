package oliveira.fabio.challenge52.creategoal.domain.usecase

import oliveira.fabio.challenge52.creategoal.presentation.vo.GoalSuggestion

interface GetGoalSuggestionsUseCase {
    suspend operator fun invoke(): List<GoalSuggestion>
}