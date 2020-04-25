package oliveira.fabio.challenge52.presentation.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Challenge(
    val id: Long,
    val name: String,
    val description: String,
    val isAccumulative: Boolean = false,
    val quantity: Int = 0,
    val type: Type? = null
) : Parcelable {
    enum class Type {
        DAILY,
        WEEKLY,
        MONTHLY
    }
}