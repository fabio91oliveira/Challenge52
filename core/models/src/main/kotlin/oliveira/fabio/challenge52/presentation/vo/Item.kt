package oliveira.fabio.challenge52.presentation.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Item(
    val id: Long,
    val position: Int,
    val date: Date,
    val money: Double,
    var isChecked: Boolean
) : Parcelable