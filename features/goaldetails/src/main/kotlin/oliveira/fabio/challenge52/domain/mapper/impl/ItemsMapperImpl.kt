package oliveira.fabio.challenge52.domain.mapper.impl

import oliveira.fabio.challenge52.domain.mapper.ItemMapper
import oliveira.fabio.challenge52.domain.model.Goal
import oliveira.fabio.challenge52.domain.model.Week
import oliveira.fabio.challenge52.extensions.getCurrentYear
import oliveira.fabio.challenge52.extensions.getMonthName
import oliveira.fabio.challenge52.extensions.getMonthNumber
import oliveira.fabio.challenge52.presentation.adapter.vo.AdapterItem
import java.util.*

internal class ItemsMapperImpl : ItemMapper {
    override fun invoke(goal: Goal) = mutableListOf<AdapterItem<String, Week>>().apply {
        var lastMonth = 1
        goal.weeks.forEach {
            if (it.date.getMonthNumber() != lastMonth) {
                lastMonth = it.date.getMonthNumber()

                add(
                    AdapterItem(
                        first = formatHeaderTitle(it.date),
                        viewType = AdapterItem.ViewType.HEADER
                    )
                )
                add(
                    AdapterItem(
                        second = it,
                        viewType = AdapterItem.ViewType.ITEM
                    )
                )
            } else {
                add(
                    AdapterItem(
                        second = it,
                        viewType = AdapterItem.ViewType.ITEM
                    )
                )
            }
        }
    }
//
//    override fun invoke(goalWithWeeks: GoalWithWeeksEntity, week: WeekEntity?) =
//        mutableListOf<Item>().apply {
//            validateWeek(week, goalWithWeeks)
//            createSubItem(this, goalWithWeeks)
//            createItem(this, goalWithWeeks)
//        }
//
//    private fun validateWeek(week: WeekEntity?, goalWithWeeks: GoalWithWeeksEntity) {
//        week?.let {
//            for (weekInner in goalWithWeeks.weeks) {
//                if (weekInner.id == it.id) {
//                    weekInner.isDeposited = it.isDeposited
//                    break
//                }
//            }
//        }
//    }
//
//    private fun createSubItem(mutableList: MutableList<Item>, goalWithWeeks: GoalWithWeeksEntity) {
//        mutableList.add(
//            SubItemDetails(
//                goalWithWeeks.getPercentOfConclusion(),
//                (goalWithWeeks.weeks.size - goalWithWeeks.getRemainingWeeksCount()),
//                goalWithWeeks.weeks.size,
//                goalWithWeeks.getTotalAccumulated(),
//                goalWithWeeks.getTotal()
//            )
//        )
//    }
//
//    private fun createItem(mutableList: MutableList<Item>, goalWithWeeks: GoalWithWeeksEntity) {
//        var lastMonth = 1
//
//        goalWithWeeks.weeks.forEach {
//            if (it.date.getMonthNumber() != lastMonth) {
//                lastMonth = it.date.getMonthNumber()
//
//                mutableList.add(HeaderItem(formatHeaderTitle(it.date)))
//                mutableList.add(SubItemWeek(it))
//            } else {
//                mutableList.add(SubItemWeek(it))
//            }
//        }
//    }

    private fun formatHeaderTitle(date: Date) = "${date.getMonthName()}/${date.getCurrentYear()}"

}