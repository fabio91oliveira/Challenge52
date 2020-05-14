package oliveira.fabio.challenge52.presentation.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopDetails(
    val goalName: String,
    val totalCompletedItems: Int,
    val totalPercentsCompleted: Int,
    val totalItems: Int,
    val totalMoneySaved: String,
    val totalMoneyToSave: String,
    val periodItemEnum: PeriodItemEnum
) : Parcelable