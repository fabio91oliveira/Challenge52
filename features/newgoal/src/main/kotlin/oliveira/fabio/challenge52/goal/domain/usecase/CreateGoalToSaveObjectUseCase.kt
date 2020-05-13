package oliveira.fabio.challenge52.goal.domain.usecase

import oliveira.fabio.challenge52.goal.presentation.vo.GoalSuggestion
import oliveira.fabio.challenge52.presentation.vo.GoalToSave
import oliveira.fabio.challenge52.challenge.selectchallenge.presentation.vo.Challenge

interface CreateGoalToSaveObjectUseCase {
    suspend operator fun invoke(
        goalSuggestion: GoalSuggestion,
        challenge: Challenge?
    ): GoalToSave
}