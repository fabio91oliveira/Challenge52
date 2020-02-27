package oliveira.fabio.challenge52.domain.mapper.impl

import oliveira.fabio.challenge52.domain.mapper.GoalMapper
import oliveira.fabio.challenge52.domain.model.Goal
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks

class GoalMapperImpl : GoalMapper {
    override fun invoke(goalWithWeeks: GoalWithWeeks) = Goal(
        id = goalWithWeeks.goal.id,
        status = getStatus(goalWithWeeks),
        name = goalWithWeeks.goal.name,
        totalCompletedWeeks = getTotalCompletedWeeks(goalWithWeeks),
        percentCompleted = getPercentCompleted(goalWithWeeks),
        moneyToSave = getTotal(goalWithWeeks)
    )

    private fun getStatus(goalWithWeeks: GoalWithWeeks) = Goal.Status.NEW
    private fun getTotalCompletedWeeks(goalWithWeeks: GoalWithWeeks) =
        goalWithWeeks.weeks.filter { it.isDeposited }.size

    private fun getPercentCompleted(goalWithWeeks: GoalWithWeeks) =
        (((getTotalCompletedWeeks(goalWithWeeks).toFloat()) / goalWithWeeks.weeks.size.toFloat()) * PERCENT).toInt()

    private fun getTotal(goalWithWeeks: GoalWithWeeks): Float {
        var total = 0f
        goalWithWeeks.weeks.forEach {
            total += it.spittedValue
        }
        return total
    }

    companion object {
        private const val PERCENT = 100
    }
}