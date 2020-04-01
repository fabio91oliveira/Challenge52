package oliveira.fabio.challenge52.goalcreate.domain.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MoneySuggestion(
    val moneyPresentation: String
) : Parcelable