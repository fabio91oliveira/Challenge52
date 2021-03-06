package oliveira.fabio.challenge52.goalslists.openedgoalslist.presentation.action

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import oliveira.fabio.challenge52.presentation.vo.Goal

internal sealed class OpenedGoalsActions {
    data class OpenedGoalsList(val openedGoalsList: List<Goal>) : OpenedGoalsActions()
    data class ShowMessage(@StringRes val stringRes: Int) : OpenedGoalsActions()
    object RefreshList : OpenedGoalsActions()
    data class Empty(val openedGoalsStateResources: OpenedGoalsStateResources) :
        OpenedGoalsActions()

    data class Error(val openedGoalsStateResources: OpenedGoalsStateResources) :
        OpenedGoalsActions()

    data class OpenedGoalsStateResources(
        @DrawableRes val imageRes: Int,
        @StringRes val titleRes: Int,
        @StringRes val descriptionRes: Int,
        @StringRes val buttonTextRes: Int? = null
    )
}