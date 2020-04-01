package oliveira.fabio.challenge52.domain.mapper.impl

import oliveira.fabio.challenge52.domain.mapper.GoalMapper
import oliveira.fabio.challenge52.domain.model.Goal
import oliveira.fabio.challenge52.domain.model.Week
import oliveira.fabio.challenge52.persistence.model.enums.GoalStatusEnum
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithItemsEntity

internal class GoalMapperImpl : GoalMapper {
    override fun invoke(goalWithItems: GoalWithItemsEntity) = Goal(
        id = goalWithItems.goal.id,
        status = getStatus(goalWithItems),
        name = goalWithItems.goal.name,
        moneyToSave = getTotal(goalWithItems),
        weeks = getWeeks(goalWithItems)
    )

    private fun getStatus(goalWithItems: GoalWithItemsEntity): Goal.Status {
        when (goalWithItems.goal.goalStatus == GoalStatusEnum.DONE) {
            true -> {
                return Goal.Status.DONE
            }
            else -> {
                goalWithItems.items.forEach {
                    if (it.isSaved)
                        return Goal.Status.IN_PROGRESS
                }
                return Goal.Status.NEW
            }
        }
    }

    private fun getTotal(goalWithItems: GoalWithItemsEntity): Float {
        var total = 0f
        goalWithItems.items.forEach {
            total += it.value
        }
        return total
    }

    private fun getWeeks(goalWithItems: GoalWithItemsEntity) = arrayListOf<Week>().apply {
        goalWithItems.items.forEach {
            add(
                Week(
                    id = it.id,
                    idGoal = goalWithItems.goal.id,
                    isChecked = it.isSaved,
                    date = it.date,
                    weekNumber = it.position,
                    moneyToSave = it.value
                )
            )
        }
    }
}