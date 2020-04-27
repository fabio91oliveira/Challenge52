package oliveira.fabio.challenge52.presentation.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Balance(
    val id: Long,
    val date: Date,
    val currentLocale: Locale,
    val transactions: MutableList<Transaction>
) : Parcelable