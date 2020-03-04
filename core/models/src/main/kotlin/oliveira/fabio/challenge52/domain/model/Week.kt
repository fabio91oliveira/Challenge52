package oliveira.fabio.challenge52.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Week(
    val id: Long,
    val idGoal: Long,
    var isChecked: Boolean,
    val weekNumber: Int,
    val date: Date,
    val moneyToSave: Float
) : Parcelable