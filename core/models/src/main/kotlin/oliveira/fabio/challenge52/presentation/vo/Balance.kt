package oliveira.fabio.challenge52.presentation.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Balance(
    val id: Long? = null,
    var date: Date? = null,
    var currentLocale: Locale = Locale.getDefault(),
    val total: Double = 0.0,
    val totalIncomes: Double = 0.0,
    val totalSpent: Double = 0.0,
    val totalAllFilter: Int = 0,
    val totalIncomeFilter: Int = 0,
    val totalSpentFilter: Int = 0,
    var isHide: Boolean = false,
    val transactions: MutableList<Transaction>? = null
) : Parcelable