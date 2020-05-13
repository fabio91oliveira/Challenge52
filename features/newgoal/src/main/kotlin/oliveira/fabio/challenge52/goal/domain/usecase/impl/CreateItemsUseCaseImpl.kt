package oliveira.fabio.challenge52.goal.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.goal.domain.usecase.CreateItemsUseCase
import oliveira.fabio.challenge52.presentation.vo.GoalToSave
import oliveira.fabio.challenge52.presentation.vo.ItemToSave
import oliveira.fabio.challenge52.presentation.vo.enums.PeriodEnum
import java.util.*

internal class CreateItemsUseCaseImpl : CreateItemsUseCase {
    override suspend fun invoke(goalToSave: GoalToSave) =
        withContext(Dispatchers.IO) {
            Calendar.getInstance().let { calendar ->
                calendar.time = Date()

                var accumulative = 0.toDouble()
                val itemsList: MutableList<ItemToSave> = mutableListOf()
                val period = getPeriod(goalToSave)

                for (n in 1..goalToSave.totalPeriod) {
                    if (goalToSave.isAccumulative) {
                        accumulative += goalToSave.moneyPerPeriod

                        itemsList.add(
                            ItemToSave(
                                position = n,
                                date = calendar.time,
                                money = accumulative
                            )
                        )

                        goalToSave.totalMoney += accumulative

                    } else {
                        itemsList.add(
                            ItemToSave(
                                position = n,
                                date = calendar.time,
                                money = goalToSave.moneyPerPeriod
                            )
                        )

                        goalToSave.totalMoney += goalToSave.moneyPerPeriod
                    }
                    calendar.add(period, 1)
                }

                goalToSave.items = itemsList
            }
        }

    private fun getPeriod(goalToSave: GoalToSave) = when (goalToSave.period) {
        PeriodEnum.DAILY -> Calendar.DAY_OF_YEAR
        PeriodEnum.WEEKLY -> Calendar.WEEK_OF_YEAR
        PeriodEnum.MONTHLY -> Calendar.MONTH
        else -> throw IllegalArgumentException()
    }
}