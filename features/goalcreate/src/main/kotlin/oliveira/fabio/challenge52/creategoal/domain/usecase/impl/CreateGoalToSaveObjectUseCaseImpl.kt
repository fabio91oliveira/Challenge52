package oliveira.fabio.challenge52.creategoal.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.creategoal.domain.usecase.CreateGoalToSaveObjectUseCase
import oliveira.fabio.challenge52.creategoal.domain.usecase.GetCurrentLocaleUseCase
import oliveira.fabio.challenge52.creategoal.presentation.vo.GoalSuggestion
import oliveira.fabio.challenge52.presentation.vo.Challenge
import oliveira.fabio.challenge52.presentation.vo.GoalToSave
import oliveira.fabio.challenge52.presentation.vo.PeriodEnum

internal class CreateGoalToSaveObjectUseCaseImpl(
    private val getCurrentLocaleUseCase: GetCurrentLocaleUseCase
) : CreateGoalToSaveObjectUseCase {
    override suspend fun invoke(
        goalSuggestion: GoalSuggestion,
        challenge: Challenge?
    ) = withContext(Dispatchers.Default) {
        challenge?.let {
            GoalToSave(
                idChallenge = it.id,
                name = goalSuggestion.name.orEmpty(),
                currentLocale = getCurrentLocaleUseCase(),
                period = getPeriodType(it),
                isAccumulative = it.isAccumulative,
                totalPeriod = it.quantity
            )
        } ?: throw Exception()
    }

    private fun getPeriodType(challenge: Challenge) = when (challenge.type) {
        Challenge.Type.DAILY -> PeriodEnum.DAILY
        Challenge.Type.WEEKLY -> PeriodEnum.WEEKLY
        Challenge.Type.MONTHLY -> PeriodEnum.MONTHLY
        else -> null
    }
}