package oliveira.fabio.challenge52.home.goalslists.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.repository.WeekRepository
import oliveira.fabio.challenge52.home.goalslists.domain.usecase.RemoveWeeks
import oliveira.fabio.challenge52.persistence.model.entity.Week
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks

class RemoveWeeksImpl(
    private val weekRepository: WeekRepository
) : RemoveWeeks {
    override suspend fun invoke(goalWithWeeksList: MutableList<GoalWithWeeks>) {
        withContext(Dispatchers.IO) {
            arrayListOf<Week>().apply {
                goalWithWeeksList.forEach {
                    addAll(it.weeks)
                }
                weekRepository.removeWeeks(this)
            }
        }
    }
}