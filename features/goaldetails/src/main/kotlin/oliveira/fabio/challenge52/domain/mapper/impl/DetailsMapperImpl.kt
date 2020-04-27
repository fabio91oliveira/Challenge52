package oliveira.fabio.challenge52.domain.mapper.impl

import oliveira.fabio.challenge52.domain.mapper.DetailsMapper
import oliveira.fabio.challenge52.extensions.getCurrentYear
import oliveira.fabio.challenge52.extensions.getMonthName
import oliveira.fabio.challenge52.extensions.getMonthNumber
import oliveira.fabio.challenge52.extensions.toStringMoney
import oliveira.fabio.challenge52.presentation.adapter.AdapterItem
import oliveira.fabio.challenge52.presentation.vo.Goal
import oliveira.fabio.challenge52.presentation.vo.Item
import oliveira.fabio.challenge52.presentation.vo.ItemDetail
import oliveira.fabio.challenge52.presentation.vo.enums.PeriodEnum
import oliveira.fabio.challenge52.presentation.vo.PeriodItemEnum
import oliveira.fabio.challenge52.presentation.vo.TopDetails
import java.util.*

internal class DetailsMapperImpl : DetailsMapper {
    override fun invoke(goal: Goal) =
        mutableListOf<AdapterItem<TopDetails, String, ItemDetail>>().apply {
            add(
                AdapterItem(
                    first = createTopDetails(goal),
                    viewType = AdapterItem.ViewType.DETAILS
                )
            )

            var lastMonth = 1
            goal.items.forEach {
                if (it.date.getMonthNumber() != lastMonth) {
                    lastMonth = it.date.getMonthNumber()
                    add(
                        AdapterItem(
                            second = formatHeaderTitle(it.date),
                            viewType = AdapterItem.ViewType.HEADER
                        )
                    )
                    add(
                        AdapterItem(
                            third = createItemDetail(goal, it),
                            viewType = AdapterItem.ViewType.ITEM
                        )
                    )
                } else {
                    add(
                        AdapterItem(
                            third = createItemDetail(goal, it),
                            viewType = AdapterItem.ViewType.ITEM
                        )
                    )
                }
            }
        }

    private fun createTopDetails(
        goal: Goal
    ): TopDetails {
        var totalCompletedWeeks = 0
        var totalMoneySaved = 0.toDouble()
        var totalMoneyToSave = 0.toDouble()

        goal.items.forEach {
            if (it.isChecked) {
                totalCompletedWeeks += 1
                totalMoneySaved += it.money
            }

            totalMoneyToSave += it.money
        }

        return TopDetails(
            totalCompletedItems = totalCompletedWeeks,
            totalPercentsCompleted = (((totalCompletedWeeks.toFloat()) / goal.items.size.toFloat()) * PERCENT).toInt(),
            totalItems = goal.items.size,
            totalMoneySaved = totalMoneySaved.toStringMoney(currentLocale = goal.currentLocale),
            totalMoneyToSave = totalMoneyToSave.toStringMoney(currentLocale = goal.currentLocale),
            periodItemEnum = getPeriod(goal)
        )
    }

    private fun createItemDetail(
        goal: Goal,
        item: Item
    ) = ItemDetail(
        id = item.id,
        idGoal = goal.id,
        periodItemEnum = getPeriod(goal),
        isChecked = item.isChecked,
        position = item.position,
        date = item.date,
        moneyToSave = item.money.toStringMoney(currentLocale = goal.currentLocale)
    )

    private fun formatHeaderTitle(date: Date) = "${date.getMonthName()}/${date.getCurrentYear()}"

    private fun getPeriod(goal: Goal) = when (goal.period) {
        PeriodEnum.DAILY -> PeriodItemEnum.DAY
        PeriodEnum.WEEKLY -> PeriodItemEnum.WEEK
        PeriodEnum.MONTHLY -> PeriodItemEnum.MONTH
    }

    companion object {
        private const val PERCENT = 100
    }
}