package oliveira.fabio.challenge52.creategoal.domain.usecase

import oliveira.fabio.challenge52.creategoal.presentation.vo.GoalSuggestion
import oliveira.fabio.challenge52.presentation.vo.GoalToSave
import oliveira.fabio.challenge52.presentation.vo.Challenge

interface CreateGoalToSaveObjectUseCase {
    suspend operator fun invoke(
        goalSuggestion: GoalSuggestion,
        challenge: Challenge?
    ): GoalToSave
}