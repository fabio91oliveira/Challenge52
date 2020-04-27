package oliveira.fabio.challenge52.presentation.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import oliveira.fabio.challenge52.presentation.vo.enums.PeriodEnum
import oliveira.fabio.challenge52.presentation.vo.enums.StatusEnum
import java.util.*

@Parcelize
data class Goal(
    val id: Long,
    val statusEnum: StatusEnum,
    val name: String,
    val currentLocale: Locale,
    val totalMoney: Double,
    val period: PeriodEnum,
    val items: List<Item>
) : Parcelable {
    fun getTotalItems() = items.filter { it.isChecked }.size
    fun getTotalPercent() =
        (((getTotalItems().toFloat()) / items.size.toFloat()) * PERCENT).toInt()

    companion object {
        private const val PERCENT = 100
    }
}