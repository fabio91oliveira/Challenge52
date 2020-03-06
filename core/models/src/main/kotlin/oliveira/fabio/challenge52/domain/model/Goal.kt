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
    val weeks: ArrayList<Week>
) : Parcelable {
    enum class Status {
        NEW,
        IN_PROGRESS,
        DONE
    }

    fun getMoneySaved(): Float {
        var moneySaved = 0f
        weeks.forEach {
            if (it.isChecked) {
                moneySaved += it.moneyToSave
            }
        }

        return moneySaved
    }

    fun getTotalWeeks() = weeks.filter { it.isChecked }.size
    fun getTotalPercent() =
        (((getTotalWeeks().toFloat()) / weeks.size.toFloat()) * PERCENT).toInt()

    companion object {
        private const val PERCENT = 100
    }
}
