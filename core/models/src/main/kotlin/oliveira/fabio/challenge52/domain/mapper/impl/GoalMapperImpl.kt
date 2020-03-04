package oliveira.fabio.challenge52.domain.mapper.impl

import oliveira.fabio.challenge52.domain.mapper.GoalMapper
import oliveira.fabio.challenge52.domain.model.Goal
import oliveira.fabio.challenge52.domain.model.Week
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeksEntity

internal class GoalMapperImpl : GoalMapper {
    override fun invoke(goalWithWeeks: GoalWithWeeksEntity) = Goal(
        id = goalWithWeeks.goal.id,
        status = getStatus(goalWithWeeks),
        name = goalWithWeeks.goal.name,
        moneyToSave = getTotal(goalWithWeeks),
        weeks = getWeeks(goalWithWeeks)
    )

    private fun getStatus(goalWithWeeks: GoalWithWeeksEntity) = Goal.Status.NEW
    private fun getTotal(goalWithWeeks: GoalWithWeeksEntity): Float {
        var total = 0f
        goalWithWeeks.weeks.forEach {
            total += it.spittedValue
        }
        return total
    }

    private fun getWeeks(goalWithWeeks: GoalWithWeeksEntity) = mutableListOf<Week>().apply {
        goalWithWeeks.weeks.forEach {
            add(
                Week(
                    id = it.id,
                    idGoal = goalWithWeeks.goal.id,
                    isChecked = it.isDeposited,
                    date = it.date,
                    weekNumber = it.position,
                    moneyToSave = it.spittedValue
                )
            )
        }
    }
}