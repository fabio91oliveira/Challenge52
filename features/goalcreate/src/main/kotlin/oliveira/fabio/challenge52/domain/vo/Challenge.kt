package oliveira.fabio.challenge52.domain.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Challenge(
    val id: Long,
    val name: String,
    val description: String,
    val isAccumulative: Boolean,
    val quantity: Int? = null,
    val type: Type
) : Parcelable {
    enum class Type {
        DAILY,
        WEEKLY,
        MONTHLY,
        NONE
    }
}