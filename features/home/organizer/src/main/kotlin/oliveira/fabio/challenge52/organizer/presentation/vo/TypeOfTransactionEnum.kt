package oliveira.fabio.challenge52.organizer.presentation.vo

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import features.home.organizer.R

enum class TypeOfTransactionEnum(
    @StringRes val resStringDefault: Int,
    @StringRes val resStringParams: Int,
    @IdRes val resId: Int,
    val value: Int
) {
    ALL(
        R.string.organizer_chip_all,
        R.string.organizer_chip_all_param,
        R.id.checkAll,
        0
    ),
    INCOME(
        R.string.organizer_chip_income,
        R.string.organizer_chip_income_param,
        R.id.checkIncome,
        1
    ),
    SPENT(
        R.string.organizer_chip_spent,
        R.string.organizer_chip_spent_param,
        R.id.checkSpent,
        2
    );

    fun getTypeById(value: Int): TypeOfTransactionEnum {
        enumValues<TypeOfTransactionEnum>().forEach {
            if (it.value == value) {
                return it
            }
        }
        return ALL
    }
}