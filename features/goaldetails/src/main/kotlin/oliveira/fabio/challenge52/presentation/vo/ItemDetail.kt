package oliveira.fabio.challenge52.presentation.vo

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ItemDetail(
    val id: Long,
    val idGoal: Long,
    @StringRes val periodRes: Int,
    var isChecked: Boolean,
    val position: Int,
    val date: Date,
    val moneyToSave: String
) : Parcelable