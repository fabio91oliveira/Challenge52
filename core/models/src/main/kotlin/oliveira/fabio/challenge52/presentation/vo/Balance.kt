package oliveira.fabio.challenge52.presentation.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Balance(
    val id: Long? = null,
    var date: Date? = null,
    var currentLocale: Locale = Locale.getDefault(),
    var total: Double = 0.0,
    var totalIncomes: Double = 0.0,
    var totalSpent: Double = 0.0,
    var totalAllFilter: Int = 0,
    var totalIncomeFilter: Int = 0,
    var totalSpentFilter: Int = 0,
    var isHide: Boolean = false,
    var transactionsFiltered: LinkedList<Transaction> = LinkedList(),
    val allTransactions: LinkedList<Transaction> = LinkedList(),
    var filter: String = "all"
) : Parcelable