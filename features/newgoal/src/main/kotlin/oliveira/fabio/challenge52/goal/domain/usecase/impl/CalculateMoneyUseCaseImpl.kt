package oliveira.fabio.challenge52.goal.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.goal.domain.usecase.CalculateMoneyUseCase
import oliveira.fabio.challenge52.extensions.onlyNumbers
import oliveira.fabio.challenge52.presentation.vo.GoalToSave

internal class CalculateMoneyUseCaseImpl : CalculateMoneyUseCase {
    override suspend fun invoke(goalToSave: GoalToSave, money: String): Double {
        return withContext(Dispatchers.Default) {
            var totalMoney = 0.toDouble()
            var accumulative = 0.toDouble()

            for (n in 0 until goalToSave.totalPeriod) {
                accumulative += money
                    .onlyNumbers()
                    .toDoubleOrNull()
                    ?.div(100) ?: 0.0

                totalMoney += if (goalToSave.isAccumulative) {
                    accumulative
                } else {
                    money.onlyNumbers()
                        .toDoubleOrNull()
                        ?.div(100) ?: 0.0
                }
            }

            goalToSave.moneyPerPeriod = money.onlyNumbers()
                .toDoubleOrNull()
                ?.div(100) ?: 0.0

            totalMoney
        }
    }
}