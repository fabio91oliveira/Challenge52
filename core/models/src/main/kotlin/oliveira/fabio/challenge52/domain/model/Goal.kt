package oliveira.fabio.challenge52.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Goal(
    val id: Long,
    val status: Status,
    val name: String,
    val totalCompletedWeeks: Int,
    val percentCompleted: Int,
    val moneyToSave: Float
) : Parcelable {
    enum class Status {
        NEW,
        IN_PROGRESS,
        DONE
    }
}
