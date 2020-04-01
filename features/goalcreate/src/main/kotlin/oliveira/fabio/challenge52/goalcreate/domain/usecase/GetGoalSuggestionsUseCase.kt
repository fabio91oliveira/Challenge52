package oliveira.fabio.challenge52.goalcreate.domain.usecase

import oliveira.fabio.challenge52.goalcreate.domain.vo.GoalSuggestion

interface GetGoalSuggestionsUseCase {
    suspend operator fun invoke(): List<GoalSuggestion>
}