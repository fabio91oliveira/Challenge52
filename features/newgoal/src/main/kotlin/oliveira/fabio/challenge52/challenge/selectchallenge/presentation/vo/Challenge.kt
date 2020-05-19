package oliveira.fabio.challenge52.challenge.selectchallenge.presentation.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Challenge(
    val id: Long,
    val name: String,
    val smallDescription: String,
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