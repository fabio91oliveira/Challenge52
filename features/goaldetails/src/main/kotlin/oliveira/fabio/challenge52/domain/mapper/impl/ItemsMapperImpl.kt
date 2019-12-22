package oliveira.fabio.challenge52.domain.mapper.impl

import oliveira.fabio.challenge52.domain.mapper.ItemMapper
import oliveira.fabio.challenge52.domain.model.vo.HeaderItem
import oliveira.fabio.challenge52.domain.model.vo.Item
import oliveira.fabio.challenge52.domain.model.vo.SubItemDetails
import oliveira.fabio.challenge52.domain.model.vo.SubItemWeek
import oliveira.fabio.challenge52.extensions.getCurrentYear
import oliveira.fabio.challenge52.extensions.getMonthName
import oliveira.fabio.challenge52.extensions.getMonthNumber
import oliveira.fabio.challenge52.persistence.model.entity.Week
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks
import java.util.*

class ItemsMapperImpl : ItemMapper {
    override fun invoke(goalWithWeeks: GoalWithWeeks, week: Week?) =
        mutableListOf<Item>().apply {
            validateWeek(week, goalWithWeeks)
            createSubItem(this, goalWithWeeks)
            createItem(this, goalWithWeeks)
        }

    private fun validateWeek(week: Week?, goalWithWeeks: GoalWithWeeks) {
        week?.let {
            for (weekInner in goalWithWeeks.weeks) {
                if (weekInner.id == it.id) {
                    weekInner.isDeposited = it.isDeposited
                    break
                }
            }
        }
    }

    private fun createSubItem(mutableList: MutableList<Item>, goalWithWeeks: GoalWithWeeks) {
        mutableList.add(
            SubItemDetails(
                goalWithWeeks.getPercentOfConclusion(),
                (goalWithWeeks.weeks.size - goalWithWeeks.getRemainingWeeksCount()),
                goalWithWeeks.weeks.size,
                goalWithWeeks.getTotalAccumulated(),
                goalWithWeeks.getTotal()
            )
        )
    }

    private fun createItem(mutableList: MutableList<Item>, goalWithWeeks: GoalWithWeeks) {
        var lastMonth = 1

        goalWithWeeks.weeks.forEach {
            if (it.date.getMonthNumber() != lastMonth) {
                lastMonth = it.date.getMonthNumber()

                mutableList.add(HeaderItem(formatHeaderTitle(it.date)))
                mutableList.add(SubItemWeek(it))
            } else {
                mutableList.add(SubItemWeek(it))
            }
        }
    }

    private fun formatHeaderTitle(date: Date) = "${date.getMonthName()}/${date.getCurrentYear()}"
}