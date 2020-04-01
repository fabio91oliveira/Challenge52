package oliveira.fabio.challenge52.goalcreate.domain.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class DateSuggestion(
    val presentationName: String,
    val date: Date
) : Parcelable