package oliveira.fabio.challenge52.goalslists.donegoalslist.presentation.action

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import oliveira.fabio.challenge52.presentation.vo.Goal

internal sealed class DoneGoalsActions {
    data class DoneGoalsList(val doneGoalsList: List<Goal>) : DoneGoalsActions()
    object RefreshList : DoneGoalsActions()
    data class ShowMessage(@StringRes val stringRes: Int) : DoneGoalsActions()
    data class Empty(val doneGoalsStateResources: DoneGoalsStateResources) :
        DoneGoalsActions()

    data class Error(
        val doneGoalsStateResources: DoneGoalsStateResources
    ) :
        DoneGoalsActions()
}

data class DoneGoalsStateResources(
    @DrawableRes val imageRes: Int,
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
    @StringRes val buttonTextRes: Int? = null
)