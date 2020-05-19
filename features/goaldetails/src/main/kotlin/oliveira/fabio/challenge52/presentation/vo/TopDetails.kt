package oliveira.fabio.challenge52.presentation.vo

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopDetails(
    val goalName: String,
    val totalCompletedItems: Int,
    val totalPercentsCompleted: Int,
    val totalItems: Int,
    val totalMoneySaved: String,
    val totalMoneyToSave: String,
    @StringRes val periodStrRes: Int,
    @StringRes val statusStrRes: Int
) : Parcelable