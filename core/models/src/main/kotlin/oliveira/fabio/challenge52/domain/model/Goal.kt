package oliveira.fabio.challenge52.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Goal(
    val id: Long,
    val status: Status,
    val name: String,
    val moneyToSave: Float,
    val weeks: ArrayList<Week>,
    val totalCompletedWeeks: Int,
    val totalPercentCompleted: Int
) : Parcelable {
    enum class Status {
        NEW,
        IN_PROGRESS,
        DONE
    }

    companion object {
        private const val PERCENT = 100
    }
}
