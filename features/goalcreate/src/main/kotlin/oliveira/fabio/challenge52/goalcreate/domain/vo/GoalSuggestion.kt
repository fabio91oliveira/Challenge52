package oliveira.fabio.challenge52.goalcreate.domain.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GoalSuggestion(
    val id: Long?,
    val icon: String?,
    val name: String?,
    val type: Type?
) : Parcelable {
    enum class Type {
        SUGGESTION,
        NOT_A_SUGGESTION
    }
}