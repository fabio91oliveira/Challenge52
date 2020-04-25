package oliveira.fabio.challenge52.presentation.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ItemDetail(
    val id: Long,
    val idGoal: Long,
    val periodItemEnum: PeriodItemEnum,
    var isChecked: Boolean,
    val position: Int,
    val date: Date,
    val moneyToSave: String
) : Parcelable