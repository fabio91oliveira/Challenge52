package oliveira.fabio.challenge52.goalslists.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.repository.GoalWithWeeksRepository
import oliveira.fabio.challenge52.goalslists.domain.usecase.GetAllDoneGoals

internal class GetAllDoneGoalsImpl(
    private val goalWithWeeksRepository: GoalWithWeeksRepository
) : GetAllDoneGoals {
    override suspend fun invoke() = withContext(Dispatchers.IO) {
        goalWithWeeksRepository.getDoneGoalsList()
    }
}