package oliveira.fabio.challenge52.organizer.presentation.vo

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import features.home.organizer.R

enum class FilterEnum(
    @StringRes val resStringDefault: Int,
    @StringRes val resStringParams: Int,
    @IdRes val resId: Int,
    val value: String
) {
    ALL(
        R.string.organizer_chip_all,
        R.string.organizer_chip_all_param,
        R.id.checkAll,
        "all"
    ),
    INCOME(
        R.string.organizer_chip_income,
        R.string.organizer_chip_income_param,
        R.id.checkIncome,
        "income"
    ),
    SPENT(
        R.string.organizer_chip_spent,
        R.string.organizer_chip_spent_param,
        R.id.checkSpent,
        "spent"
    );
}