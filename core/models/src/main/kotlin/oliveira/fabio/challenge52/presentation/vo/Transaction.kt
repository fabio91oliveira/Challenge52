package oliveira.fabio.challenge52.presentation.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import oliveira.fabio.challenge52.presentation.vo.enums.TypeTransactionEnum
import java.util.*

@Parcelize
data class Transaction(
    val id: Long = 0,
    val idBalance: Long,
    val icoResource: String,
    val description: String,
    val date: Date,
    val money: Double,
    val typeTransaction: TypeTransactionEnum
) : Parcelable