package oliveira.fabio.challenge52.presentation.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GoalResult(
    var hasChanged: Boolean = false,
    var type: GoalResultTypeEnum = GoalResultTypeEnum.NONE
) : Parcelable {
    fun setChangeUpdated() {
        hasChanged = true
        type =
            GoalResultTypeEnum.UPDATED
    }

    fun setChangeRemoved() {
        hasChanged = true
        type =
            GoalResultTypeEnum.REMOVED
    }

    fun setChangeCompleted() {
        hasChanged = true
        type =
            GoalResultTypeEnum.COMPLETED
    }
}

enum class GoalResultTypeEnum {
    NONE,
    UPDATED,
    REMOVED,
    COMPLETED
}