package oliveira.fabio.challenge52.home.goalslists.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.repository.GoalRepository
import oliveira.fabio.challenge52.home.goalslists.domain.usecase.RemoveGoals
import oliveira.fabio.challenge52.persistence.model.entity.Goal
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks

class RemoveGoalsImpl(
    private val goalRepository: GoalRepository
) : RemoveGoals {
    override suspend fun invoke(goalWithWeeksList: MutableList<GoalWithWeeks>) {
        withContext(Dispatchers.IO) {
            arrayListOf<Goal>().apply {
                goalWithWeeksList.forEach {
                    add(it.goal)
                }
                goalRepository.removeGoals(this)
            }
        }
    }
}