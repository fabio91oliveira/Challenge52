package oliveira.fabio.challenge52.goal.presentation.viewstate

import androidx.annotation.StringRes
import features.newgoal.R

internal data class CreateGoalViewState(
    val isCreateButtonEnable: Boolean = false,
    val isCalculating: Boolean = false,
    val money: String = "0",
    val totalMoney: String = "0",
    @StringRes val periodType: Int = R.string.create_goal_period_default
) {
    companion object {
        fun init() =
            CreateGoalViewState()
    }
}