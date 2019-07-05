package oliveira.fabio.challenge52.domain.interactor

import oliveira.fabio.challenge52.domain.model.vo.HeaderItem
import oliveira.fabio.challenge52.domain.model.vo.Item
import oliveira.fabio.challenge52.domain.model.vo.SubItemDetails
import oliveira.fabio.challenge52.domain.model.vo.SubItemWeek
import oliveira.fabio.challenge52.extensions.getCurrentYear
import oliveira.fabio.challenge52.extensions.getMonthName
import oliveira.fabio.challenge52.extensions.getMonthNumber
import oliveira.fabio.challenge52.model.repository.GoalRepository
import oliveira.fabio.challenge52.model.repository.WeekRepository
import oliveira.fabio.challenge52.persistence.model.entity.Goal
import oliveira.fabio.challenge52.persistence.model.entity.Week
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks
import java.util.*

class GoalDetailsInteractorImpl(
    private val goalRepository: GoalRepository,
    private val weekRepository: WeekRepository
) : GoalDetailsInteractor {

    override fun parseToDetailsList(goalWithWeeks: GoalWithWeeks, week: Week?) = mutableListOf<Item>().apply {
        var lastMonth = 1

        week?.let {
            for (weekInner in goalWithWeeks.weeks) {
                if (weekInner.id == it.id) {
                    weekInner.isDeposited = it.isDeposited
                    break
                }
            }
        }

        add(
            SubItemDetails(
                goalWithWeeks.getPercentOfConclusion(),
                (goalWithWeeks.weeks.size - goalWithWeeks.getRemainingWeeksCount()),
                goalWithWeeks.weeks.size,
                goalWithWeeks.getTotalAccumulated(),
                goalWithWeeks.getTotal()
            )
        )

        goalWithWeeks.weeks.forEach {

            if (it.date.getMonthNumber() != lastMonth) {
                lastMonth = it.date.getMonthNumber()

                add(HeaderItem(formatHeaderTitle(it.date)))
                add(SubItemWeek(it))
            } else {
                add(SubItemWeek(it))
            }
        }
    }

    override suspend fun updateWeek(week: Week) = weekRepository.updateWeek(week)
    override suspend fun removeGoal(goal: Goal) = goalRepository.removeGoal(goal)
    override suspend fun removeWeeks(weeks: List<Week>) = weekRepository.removeWeeks(weeks)
    override suspend fun updateWeeks(weeks: List<Week>) = weekRepository.updateWeeks(weeks)
    override suspend fun updateGoal(goal: Goal) = goalRepository.updateGoal(goal)

    private fun formatHeaderTitle(date: Date) = "${date.getMonthName()}/${date.getCurrentYear()}"

}