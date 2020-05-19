package oliveira.fabio.challenge52.presentation.viewstate

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import features.goaldetails.R
import oliveira.fabio.challenge52.presentation.adapter.AdapterItem
import oliveira.fabio.challenge52.presentation.vo.ItemDetail
import oliveira.fabio.challenge52.presentation.vo.TopDetails

internal data class GoalDetailsViewState(
    val isLoading: Boolean = false,
    val isItemsBeingUpdated: Boolean = false,
    val isContentVisible: Boolean = false,
    val topDetails: TopDetails = TopDetails(
        "",
        0,
        0,
        0,
        "",
        "",
        R.string.all_empty,
        R.string.all_empty
    ),
    val adapterList: MutableList<AdapterItem<String, ItemDetail>> = mutableListOf(),
    val dialog: Dialog = Dialog.NoDialog
) {
    companion object {
        fun init() = GoalDetailsViewState()
    }
}

sealed class Dialog {
    object NoDialog : Dialog()
    data class ConfirmationDialogDoneGoal(
        @DrawableRes val imageRes: Int,
        @StringRes val titleRes: Int,
        @StringRes val descriptionRes: Int
    ) :
        Dialog()

    data class ConfirmationDialogRemoveGoal(
        @DrawableRes val imageRes: Int,
        @StringRes val titleRes: Int,
        @StringRes val descriptionRes: Int
    ) :
        Dialog()

    data class DefaultDialogMoveToDone(
        @DrawableRes val imageRes: Int,
        @StringRes val titleRes: Int,
        @StringRes val descriptionRes: Int
    ) : Dialog()

    data class RegularErrorDialog(
        @DrawableRes val imageRes: Int,
        @StringRes val titleRes: Int,
        @StringRes val descriptionRes: Int
    ) : Dialog()
}