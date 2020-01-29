package oliveira.fabio.challenge52.home.goalslists.domain.usecase.impl

import oliveira.fabio.challenge52.domain.GoalWithWeeksRepository
import oliveira.fabio.challenge52.home.goalslists.domain.usecase.GetAllDoneGoals

class GetAllDoneGoalsImpl(
    private val goalWithWeeksRepository: GoalWithWeeksRepository
) : GetAllDoneGoals {
    override suspend fun invoke() = goalWithWeeksRepository.getAllDoneGoalsWithWeeks()
}