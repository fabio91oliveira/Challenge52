package oliveira.fabio.challenge52.organizer.presentation.vo

import java.util.*

internal data class BalanceTop(
    val currentLocale: Locale = Locale.getDefault(),
    val total: Double = 0.0,
    val totalIncomes: Double = 0.0,
    val totalSpent: Double = 0.0
)