package oliveira.fabio.challenge52.home.goalslists.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.repository.GoalRepository
import oliveira.fabio.challenge52.domain.repository.WeekRepository
import oliveira.fabio.challenge52.home.goalslists.domain.usecase.RemoveGoalsUseCase
import oliveira.fabio.challenge52.persistence.model.entity.Goal
import oliveira.fabio.challenge52.persistence.model.entity.Week
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks

class RemoveGoalsUseCaseImpl(
    private val goalRepository: GoalRepository,
    private val weekRepository: WeekRepository
) : RemoveGoalsUseCase {
    override suspend fun invoke(goalWithWeeksList: MutableList<GoalWithWeeks>) {
        withContext(Dispatchers.IO) {
            val goalsList = arrayListOf<Goal>()
            val weeksList = arrayListOf<Week>()
            goalWithWeeksList.forEach {
                goalsList.add(it.goal)
                weeksList.addAll(it.weeks)
            }
            goalRepository.removeGoals(goalsList)
            weekRepository.removeWeeks(weeksList)
        }
    }
}