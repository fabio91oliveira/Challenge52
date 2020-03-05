package oliveira.fabio.challenge52.domain.mapper.impl

import oliveira.fabio.challenge52.domain.mapper.WeekDetailsMapper
import oliveira.fabio.challenge52.domain.model.Week
import oliveira.fabio.challenge52.extensions.getCurrentYear
import oliveira.fabio.challenge52.extensions.getMonthName
import oliveira.fabio.challenge52.extensions.getMonthNumber
import oliveira.fabio.challenge52.extensions.toCurrency
import oliveira.fabio.challenge52.presentation.adapter.vo.AdapterItem
import oliveira.fabio.challenge52.presentation.vo.TopDetails
import java.util.*

internal class WeekDetailsMapperImpl : WeekDetailsMapper {
    override fun invoke(weeks: ArrayList<Week>) =
        mutableListOf<AdapterItem<TopDetails, String, Week>>().apply {
            add(
                AdapterItem(
                    first = createTopDetails(weeks),
                    viewType = AdapterItem.ViewType.DETAILS
                )
            )

            var lastMonth = 1
            weeks.forEach {
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
                            third = it,
                            viewType = AdapterItem.ViewType.ITEM
                        )
                    )
                } else {
                    add(
                        AdapterItem(
                            third = it,
                            viewType = AdapterItem.ViewType.ITEM
                        )
                    )
                }
            }
        }

    private fun createTopDetails(
        weeks: ArrayList<Week>
    ): TopDetails {
        var totalCompletedWeeks = 0
        var totalMoneySaved = 0f
        var totalMoneyToSave = 0f

        weeks.forEach {
            if (it.isChecked) {
                totalCompletedWeeks += 1
                totalMoneySaved += it.moneyToSave
            }

            totalMoneyToSave += it.moneyToSave
        }

        return TopDetails(
            totalCompletedWeeks = totalCompletedWeeks,
            totalPercentsCompleted = (((totalCompletedWeeks.toFloat()) / weeks.size.toFloat()) * PERCENT).toInt(),
            totalMoneySaved = totalMoneySaved.toCurrency(),
            totalMoneyToSave = totalMoneyToSave.toCurrency()
        )
    }

    private fun formatHeaderTitle(date: Date) = "${date.getMonthName()}/${date.getCurrentYear()}"

    companion object {
        private const val PERCENT = 100
    }
}