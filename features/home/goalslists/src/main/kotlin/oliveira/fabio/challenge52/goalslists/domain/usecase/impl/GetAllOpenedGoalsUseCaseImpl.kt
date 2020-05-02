package oliveira.fabio.challenge52.goalslists.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.repository.GoalWithWeeksRepository
import oliveira.fabio.challenge52.goalslists.domain.usecase.GetAllOpenedGoalsUseCase

internal class GetAllOpenedGoalsUseCaseImpl(
    private val goalWithWeeksRepository: GoalWithWeeksRepository
) : GetAllOpenedGoalsUseCase {
    override suspend fun invoke() = withContext(Dispatchers.IO) {
        goalWithWeeksRepository.getOpenedGoalsList()
    }
}