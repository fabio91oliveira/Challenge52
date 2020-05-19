package oliveira.fabio.challenge52.domain.usecase.impl

import features.goaldetails.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.usecase.MountGoalsDetailsUseCase
import oliveira.fabio.challenge52.extensions.getCurrentYear
import oliveira.fabio.challenge52.extensions.getMonthName
import oliveira.fabio.challenge52.extensions.getMonthNumber
import oliveira.fabio.challenge52.extensions.toStringMoney
import oliveira.fabio.challenge52.presentation.adapter.AdapterItem
import oliveira.fabio.challenge52.presentation.vo.Goal
import oliveira.fabio.challenge52.presentation.vo.Item
import oliveira.fabio.challenge52.presentation.vo.ItemDetail
import oliveira.fabio.challenge52.presentation.vo.TopDetails
import oliveira.fabio.challenge52.presentation.vo.enums.PeriodEnum
import oliveira.fabio.challenge52.presentation.vo.enums.StatusEnum
import java.util.*

internal class MountGoalsDetailsUseCaseImpl : MountGoalsDetailsUseCase {
    override suspend operator fun invoke(goal: Goal) =
        withContext(Dispatchers.Default) {
            delay(1000)
            mount(goal)
        }

    private fun mount(goal: Goal): Pair<TopDetails, MutableList<AdapterItem<String, ItemDetail>>> {
        val mutableList = mutableListOf<AdapterItem<String, ItemDetail>>().apply {
            var lastMonth = 1
            goal.items.forEach {
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
                            second = createItemDetail(goal, it),
                            viewType = AdapterItem.ViewType.ITEM
                        )
                    )
                } else {
                    add(
                        AdapterItem(
                            second = createItemDetail(goal, it),
                            viewType = AdapterItem.ViewType.ITEM
                        )
                    )
                }
            }
        }

        return Pair(first = createTopDetails(goal), second = mutableList)
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
            goalName = goal.name,
            totalCompletedItems = totalCompletedWeeks,
            totalPercentsCompleted = (((totalCompletedWeeks.toFloat()) / goal.items.size.toFloat()) * PERCENT).toInt(),
            totalItems = goal.items.size,
            totalMoneySaved = totalMoneySaved.toStringMoney(currentLocale = goal.currentLocale),
            totalMoneyToSave = totalMoneyToSave.toStringMoney(currentLocale = goal.currentLocale),
            periodStrRes = getPeriodTopDetails(goal),
            statusStrRes = getStatus(goal)
        )
    }

    private fun createItemDetail(
        goal: Goal,
        item: Item
    ) = ItemDetail(
        id = item.id,
        idGoal = goal.id,
        periodRes = getPeriodItems(goal),
        isChecked = item.isChecked,
        position = item.position,
        date = item.date,
        moneyToSave = item.money.toStringMoney(currentLocale = goal.currentLocale)
    )

    private fun formatHeaderTitle(date: Date) = "${date.getMonthName()}/${date.getCurrentYear()}"

    private fun getPeriodTopDetails(goal: Goal) = when (goal.period) {
        PeriodEnum.DAILY -> R.string.goal_details_daily
        PeriodEnum.WEEKLY -> R.string.goal_details_weekly
        PeriodEnum.MONTHLY -> R.string.goal_details_monthly
    }

    private fun getPeriodItems(goal: Goal) = when (goal.period) {
        PeriodEnum.DAILY -> R.string.goal_details_day
        PeriodEnum.WEEKLY -> R.string.goal_details_week
        PeriodEnum.MONTHLY -> R.string.goal_details_month
    }

    private fun getStatus(goal: Goal) = when (goal.statusEnum) {
        StatusEnum.NEW -> R.string.goal_details_status_new
        StatusEnum.IN_PROGRESS -> R.string.goal_details_status_in_progress
        StatusEnum.DONE -> R.string.goal_details_status_done
    }

    companion object {
        private const val PERCENT = 100
    }
}