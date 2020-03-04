package oliveira.fabio.challenge52.home.goalslists.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.repository.GoalWithWeeksRepository
import oliveira.fabio.challenge52.home.goalslists.domain.usecase.GetAllOpenedGoals

internal class GetAllOpenedGoalsImpl(
    private val goalWithWeeksRepository: GoalWithWeeksRepository
) : GetAllOpenedGoals {
    override suspend fun invoke() = withContext(Dispatchers.IO) {
        goalWithWeeksRepository.getOpenedGoalsList()
    }
}