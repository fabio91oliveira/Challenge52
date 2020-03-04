package oliveira.fabio.challenge52.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Goal(
    val id: Long,
    val status: Status,
    val name: String,
    val moneyToSave: Float,
    val weeks: List<Week>,
    val totalCompletedWeeks: Int = weeks.filter { it.isChecked }.size,
    val totalPercentCompleted: Int = (((totalCompletedWeeks.toFloat()) / weeks.size.toFloat()) * PERCENT).toInt()
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
