package oliveira.fabio.challenge52.presentation.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ItemToSave(
    var position: Int,
    var date: Date,
    var money: Double = 0.toDouble()
) : Parcelable