package oliveira.fabio.challenge52.presentation.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class GoalToSave(
    val idChallenge: Long,
    val currentLocale: Locale,
    var name: String,
    var period: PeriodEnum?,
    var isAccumulative: Boolean = false,
    var totalPeriod: Int = 0,
    var moneyPerPeriod: Double = 0.toDouble(),
    var totalMoney: Double = 0.toDouble(),
    var items: List<ItemToSave>? = null
) : Parcelable